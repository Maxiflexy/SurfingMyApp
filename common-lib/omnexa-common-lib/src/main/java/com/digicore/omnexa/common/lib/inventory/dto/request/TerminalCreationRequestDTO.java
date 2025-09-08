/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.inventory.dto.request;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
@Getter
@Setter
public class TerminalCreationRequestDTO
    implements InventoryRequest, AuthorizationRequest, Serializable {
  public static final String TERMINAL_CREATION_DTO =
      "com.digicore.omnexa.common.lib.inventory.dto.request.TerminalCreationRequestDTO";
  private List<String> serialIds;
}
