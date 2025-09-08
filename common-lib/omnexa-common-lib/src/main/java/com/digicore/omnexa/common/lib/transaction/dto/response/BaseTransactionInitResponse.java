/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.transaction.dto.response;

import com.digicore.omnexa.common.lib.transaction.enums.Currency;
import com.digicore.omnexa.common.lib.transaction.enums.PaymentToken;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-11(Mon)-2025
 */
@Getter
@Setter
public class BaseTransactionInitResponse implements TransactionInitResponse {
  private String amountInMinor;
  private String transactionReference;
  private Currency currency;
  private List<PaymentToken> availablePaymentTokens;
  private Map<String, String> metadata;
}
