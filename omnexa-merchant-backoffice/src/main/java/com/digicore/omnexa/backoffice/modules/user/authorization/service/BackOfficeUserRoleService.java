/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.service;

import static com.digicore.omnexa.backoffice.modules.user.authorization.helper.AuthorizationHelper.retrieveSelectedPermissions;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Service
@RequiredArgsConstructor
public class BackOfficeUserRoleService implements RoleService {
  private final BackOfficeUserPermissionRepository backOfficeUserPermissionService;
  private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  @Override
  public void createRole(AuthorizationRequest request) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) request;
    roleCreationDTO.setActive(true);
    BackOfficeUserRole newRole = new BackOfficeUserRole();
    BeanUtils.copyProperties(roleCreationDTO, newRole);
    newRole.setPermissions(
        retrieveSelectedPermissions(
            roleCreationDTO.getPermissions(),
            messagePropertyConfig,
            backOfficeUserPermissionService));
    backOfficeUserRoleRepository.save(newRole);
  }
}
