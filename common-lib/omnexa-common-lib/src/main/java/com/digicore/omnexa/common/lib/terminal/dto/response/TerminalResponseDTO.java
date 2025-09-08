/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.terminal.dto.response;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.inventory.dto.response.InventoryResponse;
import com.digicore.omnexa.common.lib.terminal.enums.TerminalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-06(Wed)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Terminal ResponseDTO")
public class TerminalResponseDTO implements InventoryResponse {
  public static final String TERMINAL_RESPONSE_DTO_CLASS_NAME =
      "com.digicore.omnexa.common.lib.terminal.dto.response.TerminalResponseDTO";
  private String serialId;
  private String terminalId;
  private String merchantId;
  private TerminalStatus terminalStatus;
  private TerminalStatus assignmentStatus;
  private TerminalStatus deploymentStatus;
  private boolean appUpdateRequired;
  private MerchantOutletProfileDTO merchantOutletProfileDTO;

  public TerminalResponseDTO(
      String serialId,
      String terminalId,
      boolean appUpdateRequired,
      TerminalStatus assignmentStatus,
      TerminalStatus deploymentStatus,
      TerminalStatus terminalStatus) {
    this.serialId = serialId;
    this.terminalId = terminalId;
    this.appUpdateRequired = appUpdateRequired;
    this.assignmentStatus = assignmentStatus;
    this.deploymentStatus = deploymentStatus;
    this.terminalStatus = terminalStatus;
  }

  public TerminalResponseDTO(
      String serialId,
      String terminalId,
      boolean appUpdateRequired,
      TerminalStatus assignmentStatus,
      TerminalStatus deploymentStatus,
      TerminalStatus terminalStatus,
      String merchantId,
      String outletTitle,
      String address,
      ProfileStatus status) {
    this.serialId = serialId;
    this.terminalId = terminalId;
    this.appUpdateRequired = appUpdateRequired;
    this.assignmentStatus = assignmentStatus;
    this.deploymentStatus = deploymentStatus;
    this.terminalStatus = terminalStatus;
    this.merchantOutletProfileDTO =
        new MerchantOutletProfileDTO(outletTitle, address, merchantId, status);
  }
}
