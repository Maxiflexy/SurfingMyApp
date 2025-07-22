/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.outlets.contract.service;

import com.digicore.omnexa.common.lib.outlets.contract.request.CreateOutletRequest;
import com.digicore.omnexa.common.lib.outlets.contract.response.OutletResponse;

/**
 * @author Hossana Chukwunyere
 * @createdOn Jul-11(Fri)-2025
 */
public interface OutletService {
  OutletResponse createMerchantOutlet(CreateOutletRequest request);
}
