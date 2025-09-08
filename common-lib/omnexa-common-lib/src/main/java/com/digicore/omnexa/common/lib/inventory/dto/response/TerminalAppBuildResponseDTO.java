/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-10(Sun)-2025
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Terminal APP Build ResponseDTO")
public class TerminalAppBuildResponseDTO implements InventoryResponse {
  public static final String TERMINAL_APP_BUILD_RESPONSE_DTO_CLASS_NAME =
      "com.digicore.omnexa.common.lib.inventory.dto.response.TerminalAppBuildResponseDTO";
  private String appBuildId;
  private String versionName;
  private String versionNumber;
  private String releaseNote;
}
