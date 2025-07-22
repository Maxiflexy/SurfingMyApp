/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PermissionDTO implements Serializable {
  public static final String PERMISSION_DTO =
      "com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO";

  public PermissionDTO(String name) {
    this.name = name;
  }

  private String name;

  @JsonInclude(JsonInclude.Include.NON_DEFAULT)
  private long id;

  private String description;
  private String permissionType;
}
