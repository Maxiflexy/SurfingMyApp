/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.inventory.contract;

import com.digicore.omnexa.common.lib.inventory.dto.request.InventoryRequest;
import com.digicore.omnexa.common.lib.inventory.dto.response.InventoryResponse;
import com.digicore.omnexa.common.lib.terminal.enums.TerminalStatus;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
public interface InventoryService {
  void addToInventory(InventoryRequest request);

  default PaginatedResponse<InventoryResponse> listInventories(
      Integer pageNumber, Integer pageSize) {
    return null;
  }

  default PaginatedResponse<InventoryResponse> listTerminalAppBuilds(
      Integer pageNumber, Integer pageSize) {
    return null;
  }

  default PaginatedResponse<InventoryResponse> searchProfile(
      String searchTerm, TerminalStatus status, Integer pageNumber, Integer pageSize) {
    return null;
  }

  default InventoryResponse getInventoryBySerialId(String serialId) {
    return null;
  }

  default InventoryResponse getTerminalAppBuildByAppBuildId(String appBuildId) {
    return null;
  }
}
