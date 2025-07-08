/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.data.model;

import com.digicore.omnexa.common.lib.model.BaseModel;
import com.digicore.omnexa.merchant.service.modules.profile.data.converter.MerchantProfileTypeConverter;
import com.digicore.omnexa.merchant.service.modules.profile.data.enums.MerchantProfileType;
import com.digicore.omnexa.merchant.service.modules.profile.user.data.model.MerchantUserProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the profile of a merchant in the system. This entity is mapped to the
 * `merchant_profile` table in the database.
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Entity}: Specifies that this class is a JPA entity.
 *   <li>{@link Table}: Maps this entity to the `merchant_profile` table.
 *   <li>{@link Getter}, {@link Setter}, {@link ToString}: Lombok annotations for generating
 *       boilerplate code.
 *   <li>{@link OneToOne}: Defines a one-to-one relationship with {@link MerchantProfileStatus}.
 *   <li>{@link OneToMany}: Defines a one-to-many relationship with {@link MerchantUserProfile}.
 * </ul>
 *
 * <p>Fields:
 *
 * <ul>
 *   <li>businessName: The name of the merchant's business.
 *   <li>businessEmail: The email address of the merchant's business.
 *   <li>businessPhoneNumber: The phone number of the merchant's business.
 *   <li>termsAccepted: Indicates whether the merchant has accepted the terms and conditions.
 *   <li>merchantStatusProfile: The status profile associated with the merchant.
 *   <li>users: A list of user profiles associated with the merchant.
 * </ul>
 *
 * <p>Inheritance:
 *
 * <ul>
 *   <li>Extends {@link BaseModel} for common model properties.
 *   <li>Implements {@link Serializable} for object serialization.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Entity
@Table(
    name = "merchant_profile",
    indexes = {
      @Index(name = "idx_merchant_id", columnList = "merchantId"),
      @Index(name = "idx_business_email", columnList = "businessEmail")
    })
@Getter
@Setter
@ToString
public class MerchantProfile extends BaseModel implements Serializable {

  /** The name of the merchant's business. */
  private String businessName;

  /** The email address of the merchant's business. */
  private String businessEmail;

  /** The phone number of the merchant's business. */
  private String phoneNumber;

  /** Indicates whether the merchant has accepted the terms and conditions. */
  private boolean termsAccepted;

  private String merchantId;

  /**
   * The type of the merchant profile (e.g., Aggregator, Sub-merchant, Direct Merchant). This field
   * is converted to and from its string representation using {@link MerchantProfileTypeConverter}.
   */
  @Convert(converter = MerchantProfileTypeConverter.class)
  @Column(nullable = false)
  private MerchantProfileType profileType = MerchantProfileType.DIRECT_MERCHANT;

  /**
   * The status profile associated with the merchant. Defines a one-to-one relationship with {@link
   * MerchantProfileStatus}.
   */
  @OneToOne(mappedBy = "merchantProfile", cascade = CascadeType.ALL, optional = false)
  private MerchantProfileStatus merchantProfileStatus;

  /**
   * A list of user profiles associated with the merchant. Defines a one-to-many relationship with
   * {@link MerchantUserProfile}.
   */
  @OneToMany(mappedBy = "merchantProfile", cascade = CascadeType.ALL)
  private List<MerchantUserProfile> merchantUserProfiles = new ArrayList<>();
}
