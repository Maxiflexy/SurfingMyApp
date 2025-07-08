/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.data.model;

import com.digicore.omnexa.common.lib.converter.KycStatusConverter;
import com.digicore.omnexa.common.lib.converter.ProfileStatusConverter;
import com.digicore.omnexa.common.lib.enums.KycStatus;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.model.BaseModel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the status profile of a merchant in the system. This entity is mapped to the
 * `merchant_status_profile` table in the database.
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Entity}: Specifies that this class is a JPA entity.
 *   <li>{@link Table}: Maps this entity to the `merchant_status_profile` table.
 *   <li>{@link Getter}, {@link Setter}, {@link ToString}: Lombok annotations for generating
 *       boilerplate code.
 *   <li>{@link OneToOne}: Defines a one-to-one relationship with {@link MerchantProfile}.
 *   <li>{@link MapsId}: Indicates that this entity shares the same primary key as the
 *       `MerchantProfile` entity.
 *   <li>{@link JoinColumn}: Specifies the foreign key column name in the database.
 *   <li>{@link Convert}: Specifies the use of a custom converter for the `status` field.
 * </ul>
 *
 * <p>Fields:
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
    name = "merchant_profile_status",
    indexes = {
      @Index(name = "idx_profile_status", columnList = "profileStatus"),
      @Index(name = "idx_kyc_status", columnList = "kycStatus")
    })
@Getter
@Setter
@ToString
public class MerchantProfileStatus extends BaseModel implements Serializable {

  /**
   * The `MerchantProfile` entity associated with this status profile. Defines a one-to-one
   * relationship with {@link MerchantProfile}.
   */
  @OneToOne
  @MapsId
  @JoinColumn(name = "merchant_profile_id")
  private MerchantProfile merchantProfile;

  /**
   * The current status of the merchant. This field is converted to and from its string
   * representation using {@link ProfileStatusConverter}.
   */
  @Convert(converter = ProfileStatusConverter.class)
  private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

  /** Indicates whether the merchant's KYC (Know Your Customer) verification is complete. */
  @Convert(converter = KycStatusConverter.class)
  private KycStatus kycStatus = KycStatus.PENDING;
}
