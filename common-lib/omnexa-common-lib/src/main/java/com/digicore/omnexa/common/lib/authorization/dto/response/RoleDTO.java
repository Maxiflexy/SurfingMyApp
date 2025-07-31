/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.dto.response;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Getter
@Setter
@ToString
public class RoleDTO extends BaseRoleDTO implements AuthorizationResponse, Serializable {
  private Set<PermissionDTO> permissions = new HashSet<>();

  public RoleDTO(String name, String description, boolean active, Set<PermissionDTO> permissions) {
    super(name, description, active);
    this.permissions = permissions;
  }
}
