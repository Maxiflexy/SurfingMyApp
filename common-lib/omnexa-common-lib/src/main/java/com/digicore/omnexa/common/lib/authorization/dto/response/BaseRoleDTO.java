/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.dto.response;

import java.io.Serializable;
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
public class BaseRoleDTO implements Serializable {

  private String name;

  private String description;

  private boolean active;

  private boolean systemInitiated;
  private boolean max = false;
}
