/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.approval.service;

import static com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO.ROLE_CREATION_DTO;
import static com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO.ROLE_DTO;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.module.ModuleConstant.ROLE;

import com.digicore.omnexa.backoffice.modules.user.authorization.approval.BackOfficeUserRoleProxyService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-05(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class BackOfficeUserRoleApprovalService implements BackOfficeUserRoleProxyService {
  private final RoleService backOfficeUserRoleService;

  @Override
  @MakerChecker(
      checkerPermission = "approve-create-backoffice-roles",
      makerPermission = "create-backoffice-roles",
      requestClassName = ROLE_CREATION_DTO,
      activity = CREATE,
      module = ROLE)
  public Object createRole(Object initialData, Object updateData, Object... files) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) updateData;
    backOfficeUserRoleService.createRole(roleCreationDTO);
    return Optional.empty();
  }

  @Override
  @MakerChecker(
      checkerPermission = "approve-delete-backoffice-role",
      makerPermission = "delete-backoffice-role",
      requestClassName = "java.lang.String",
      activity = DELETE,
      module = ROLE)
  public Object deleteRole(Object initialData, Object updateData, Object... files) {
    String roleName = (String) updateData;
    backOfficeUserRoleService.deleteRole(roleName);
    return Optional.empty();
  }

  @Override
  @MakerChecker(
      checkerPermission = "approve-edit-backoffice-role",
      makerPermission = "edit-backoffice-role",
      requestClassName = ROLE_CREATION_DTO,
      activity = EDIT,
      module = ROLE)
  public Object editRole(Object initialData, Object updateData, Object... files) {
    return null;
  }

  @Override
  @MakerChecker(
      checkerPermission = "approve-disable-backoffice-role",
      makerPermission = "disable-backoffice-role",
      requestClassName = ROLE_DTO,
      activity = DISABLE,
      module = ROLE)
  public Object disableRole(Object initialData, Object updateData, Object... files) {
    RoleDTO roleDTO = (RoleDTO) updateData;
    backOfficeUserRoleService.disableRole(roleDTO.getName());
    return Optional.empty();
  }

  @MakerChecker(
      checkerPermission = "approve-enable-backoffice-role",
      makerPermission = "enable-backoffice-role",
      requestClassName = ROLE_DTO,
      activity = ENABLE,
      module = ROLE)
  @Override
  public Object enableRole(Object initialData, Object updateData, Object... files) {
    RoleDTO roleDTO = (RoleDTO) updateData;
    backOfficeUserRoleService.enableRole(roleDTO.getName());
    return Optional.empty();
  }
}
