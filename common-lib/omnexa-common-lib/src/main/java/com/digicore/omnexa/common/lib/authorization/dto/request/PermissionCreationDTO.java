/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.dto.request;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PermissionCreationDTO implements AuthorizationRequest, Serializable {
  public static final String PERMISSION_DTO =
      "com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO";

  public PermissionCreationDTO(String name) {
    this.name = name;
  }

  private String name;

  private String description;
  private String permissionType;
}
