/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.mapper;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserPermission;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Component
public class AuthorizationMapper {
  private AuthorizationMapper() {}

  public static PermissionDTO mapEntityToDTO(BackOfficeUserPermission permission) {
    PermissionDTO permissionDTO = new PermissionDTO();
    BeanUtilWrapper.copyNonNullProperties(permission, permissionDTO);
    permissionDTO.setId(null);
    return permissionDTO;
  }
}
