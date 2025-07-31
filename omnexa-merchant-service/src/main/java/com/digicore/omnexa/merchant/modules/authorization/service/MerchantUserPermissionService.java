/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.service;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import com.digicore.omnexa.common.lib.authorization.contract.PermissionService;
import com.digicore.omnexa.common.lib.authorization.dto.request.PermissionCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserPermission;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserPermissionRepository;
import com.digicore.omnexa.merchant.modules.authorization.mapper.AuthorizationMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserPermissionService implements PermissionService {
  private final MerchantUserPermissionRepository merchantUserPermissionRepository;

  @Override
  public void createPermission(Set<AuthorizationRequest> newPermissions) {
    Set<PermissionCreationDTO> permissions =
        newPermissions.stream()
            .filter(PermissionCreationDTO.class::isInstance)
            .map(PermissionCreationDTO.class::cast)
            .collect(Collectors.toSet());
    // Retrieve all existing permission names from the repository
    List<String> existingPermissionNames =
        merchantUserPermissionRepository.retrieveAllPermissionName().stream()
            .map(PermissionDTO::getName)
            .toList();

    // Filter out permissions that already exist
    List<PermissionCreationDTO> permissionsToAdd =
        permissions.stream()
            .filter(permission -> !existingPermissionNames.contains(permission.getName()))
            .toList();

    // Convert to entities and save new permissions
    List<MerchantUserPermission> entitiesToAdd =
        permissionsToAdd.stream()
            .map(
                permissionDTO -> {
                  MerchantUserPermission permission = new MerchantUserPermission();
                  permission.setName(permissionDTO.getName());
                  permission.setDescription(permissionDTO.getDescription());
                  permission.setPermissionType(permissionDTO.getPermissionType());
                  return permission;
                })
            .toList();

    // Save all new permissions to the database
    merchantUserPermissionRepository.saveAll(entitiesToAdd);
  }

  @Override
  public Set<AuthorizationResponse> getAllPermissions() {
    return merchantUserPermissionRepository.findAll().stream()
        .map(AuthorizationMapper::mapEntityToDTO)
        .collect(Collectors.toSet());
  }
}
