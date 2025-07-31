/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.model.kyc;

import com.digicore.omnexa.common.lib.model.Auditable;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Entity
@Table(name = "merchant_kyc_document")
@Getter
@Setter
@ToString
public class MerchantKycDocument extends Auditable<String> implements Serializable {
  @ManyToOne
  @JoinColumn(name = "merchant_profile_id", nullable = false)
  private MerchantProfile merchantProfile;

  @Column(columnDefinition = "text")
  private String kycDocument;
}
