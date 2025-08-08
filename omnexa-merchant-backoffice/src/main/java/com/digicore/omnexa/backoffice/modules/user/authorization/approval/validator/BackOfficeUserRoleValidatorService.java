/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.approval.validator;

import com.digicore.omnexa.backoffice.modules.user.authorization.approval.BackOfficeUserRoleProxyService;
import com.digicore.omnexa.backoffice.modules.user.authorization.helper.AuthorizationHelper;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * @author Daudu John
 * @createdOn Mar-12(Wed)-2025
 */

@Service
@RequiredArgsConstructor
public class BackOfficeUserRoleValidatorService {
  private final BackOfficeUserRoleProxyService customerUserProxyService;
  private final AuthorizationHelper authorizationHelper;

  public void createRole(RoleCreationDTO roleDTO) {
    authorizationHelper.validateRoleName(roleDTO.getName());
    authorizationHelper.validateRolePermissions(roleDTO.getPermissions());
    roleDTO.setSystemInitiated(false);
    customerUserProxyService.createRole(null, roleDTO);
  }

  public void deleteRole(String name) {
    authorizationHelper.validateRoleName(name);
    customerUserProxyService.deleteRole(null, name);
  }

  public void editRole(RoleCreationDTO roleDTO) {
    authorizationHelper.validateRoleName(roleDTO.getName());
    RoleDTO roleToEdit = authorizationHelper.retrieveRole(roleDTO.getName());
    authorizationHelper.validateRolePermissions(roleDTO.getPermissions());
    customerUserProxyService.editRole(roleToEdit, roleDTO);
  }

  public void disableRole(String roleName) {
    authorizationHelper.validateRoleName(roleName);
    RoleDTO roleToDisable = authorizationHelper.retrieveRole(roleName);
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setActive(false);
    customerUserProxyService.disableRole(roleToDisable, roleDTO);
  }

  public void enableRole(String roleName) {
    authorizationHelper.validateRoleName(roleName);
    RoleDTO roleToEnable = authorizationHelper.retrieveRole(roleName);
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setName(roleName);
    roleDTO.setActive(true);
    customerUserProxyService.enableRole(roleToEnable, roleDTO);
  }
}
