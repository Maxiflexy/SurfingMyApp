/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.model;

import com.digicore.omnexa.common.lib.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Entity
@Table(
    name = "merchant_bank_profile",
    indexes = {@Index(name = "idx_merchant_id", columnList = "merchantId")})
@Getter
@Setter
@ToString
public class MerchantBankProfile extends BaseModel implements Serializable {
  private String bankName;
  private String bankAccountNumber;
  private String merchantId;
}
