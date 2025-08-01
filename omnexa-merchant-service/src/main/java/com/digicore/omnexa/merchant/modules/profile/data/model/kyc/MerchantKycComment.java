/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.model.kyc;

import com.digicore.omnexa.common.lib.model.Auditable;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
@Entity
@Table(name = "merchant_kyc_comment")
@Getter
@Setter
@ToString
public class MerchantKycComment extends Auditable<String> implements Serializable {
  private String comment;
  private String commenter;

  @ManyToOne
  @JoinColumn(name = "merchant_kyc_profile_id", nullable = false)
  private MerchantKycProfile merchantKycProfile;
}
