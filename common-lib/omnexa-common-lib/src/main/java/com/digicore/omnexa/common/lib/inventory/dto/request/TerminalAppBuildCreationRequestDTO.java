/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.inventory.dto.request;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-10(Sun)-2025
 */
@Getter
@Setter
public class TerminalAppBuildCreationRequestDTO
    implements InventoryRequest, AuthorizationRequest, Serializable {
  public static final String TERMINAL_APP_BUILD_CREATION_DTO =
      "com.digicore.omnexa.common.lib.inventory.dto.request.TerminalAppBuildCreationRequestDTO";
  private String appBuildId;
  private String versionName;
  private String versionNumber;
  private String releaseNote;
  private String buildFilePath;
}
