/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.model;

import com.digicore.omnexa.common.lib.model.BaseModel;
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
@Table(name = "merchant_api_key_profile")
@Getter
@Setter
@ToString
public class MerchantApiKeyProfile extends BaseModel implements Serializable {

  /**
   * The `MerchantProfile` entity associated with this status profile. Defines a one-to-one
   * relationship with {@link MerchantProfile}.
   */
  @OneToOne
  @MapsId
  @JoinColumn(name = "merchant_profile_id")
  private MerchantProfile merchantProfile;

  private String livePublicKey;
  private String liveSecretKey;
  private String liveEncryptionKey;
  private String testPublicKey;
  private String testSecretKey;
  private String testEncryptionKey;

  private boolean liveMode = false;
}
