/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserRole;
import com.digicore.omnexa.merchant.modules.authorization.helper.AuthorizationHelper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserRoleService implements RoleService {
  private final AuthorizationHelper authorizationHelper;

  @Override
  public void createRole(AuthorizationRequest request) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) request;
    authorizationHelper.validateRoleName(roleCreationDTO.getName());
    roleCreationDTO.setActive(true);
    MerchantUserRole newRole = new MerchantUserRole();
    newRole.setMerchantProfile(authorizationHelper.getMerchantProfileByReference());
    BeanUtils.copyProperties(roleCreationDTO, newRole);
    newRole.setPermissions(
        authorizationHelper.retrieveSelectedPermissions(roleCreationDTO.getPermissions()));
    authorizationHelper.getMerchantUserRoleRepository().save(newRole);
  }

  @Override
  public AuthorizationResponse retrieveRole(String roleName, String merchantId) {
    MerchantUserRole userRole =
        authorizationHelper
            .getMerchantUserRoleRepository()
            .findFirstByNameAndMerchantProfileMerchantId(roleName, merchantId)
            .orElseThrow(
                () ->
                    new OmnexaException(
                        authorizationHelper
                            .getMessagePropertyConfig()
                            .getRoleMessage(NOT_FOUND)
                            .replace(ROLE_NAME, roleName)));
    return new RoleDTO(
        userRole.getName(),
        userRole.getDescription(),
        userRole.isActive(),
        userRole.getPermissions().stream()
            .map(
                merchantUserPermission -> {
                  PermissionDTO permissionDTO = new PermissionDTO();
                  BeanUtilWrapper.copyNonNullProperties(merchantUserPermission, permissionDTO);
                  return permissionDTO;
                })
            .collect(Collectors.toSet()));
  }
}
