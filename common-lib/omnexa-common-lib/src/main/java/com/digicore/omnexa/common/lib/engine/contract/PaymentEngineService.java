/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.engine.contract;

import com.digicore.omnexa.common.lib.feign.TerminalFeignClient;
import com.digicore.omnexa.common.lib.transaction.dto.request.TransactionInitRequest;
import com.digicore.omnexa.common.lib.transaction.dto.response.TransactionInitResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-11(Mon)-2025
 */
@RequiredArgsConstructor
public abstract class PaymentEngineService {
  protected final HttpServletRequest httpServletRequest;
  protected final TerminalFeignClient terminalFeignClient;

  // protected PaymentEngineService(HttpServletRequest httpServletRequest) {
  //  this.httpServletRequest = httpServletRequest;
  // }

  public abstract TransactionInitResponse initializeTransaction(
      TransactionInitRequest transactionInitRequest);
}
