/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.transaction.dto.request;

import com.digicore.omnexa.common.lib.transaction.enums.Currency;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-11(Mon)-2025
 */
@Getter
@Setter
public class BaseTransactionInitRequest implements TransactionInitRequest {
  private String amountInMinor;
  private Currency currency;
  private Map<String, String> metaData;
}
