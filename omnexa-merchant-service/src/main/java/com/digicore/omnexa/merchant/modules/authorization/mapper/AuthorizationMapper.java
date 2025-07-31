/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.mapper;

import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserPermission;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Component
public class AuthorizationMapper {
  private AuthorizationMapper() {}

  public static PermissionDTO mapEntityToDTO(MerchantUserPermission permission) {
    PermissionDTO permissionDTO = new PermissionDTO();
    BeanUtilWrapper.copyNonNullProperties(permission, permissionDTO);
    return permissionDTO;
  }
}
