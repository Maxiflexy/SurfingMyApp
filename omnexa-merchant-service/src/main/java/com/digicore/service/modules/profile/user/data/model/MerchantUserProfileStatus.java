/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.profile.user.data.model;

import com.digicore.common.lib.converter.ProfileStatusConverter;
import com.digicore.common.lib.converter.ProfileVerificationStatusConverter;
import com.digicore.common.lib.enums.ProfileStatus;
import com.digicore.common.lib.enums.ProfileVerificationStatus;
import com.digicore.common.lib.model.BaseModel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents the status profile of a user associated with a merchant in the system. This entity is
 * mapped to the `merchant_user_status_profile` table in the database.
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Entity}: Specifies that this class is a JPA entity.
 *   <li>{@link Table}: Maps this entity to the `merchant_user_status_profile` table.
 *   <li>{@link Getter}, {@link Setter}, {@link ToString}: Lombok annotations for generating
 *       boilerplate code.
 *   <li>{@link OneToOne}: Defines a one-to-one relationship with {@link MerchantUserProfile}.
 *   <li>{@link MapsId}: Indicates that this entity shares the same primary key as the
 *       `MerchantUserProfile` entity.
 *   <li>{@link JoinColumn}: Specifies the foreign key column name in the database.
 *   <li>{@link Convert}: Specifies the use of a custom converter for the `status` field.
 * </ul>
 *
 * <p>Fields:
 *
 * <ul>
 *   <li>merchantUserProfile: The `MerchantUserProfile` entity associated with this status profile.
 *   <li>status: The current status of the user, converted using {@link StatusConverter}.
 *   <li>locked: Indicates whether the user account is locked.
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
    name = "merchant_user_profile_status",
    indexes = {
      @Index(name = "idx_user_status", columnList = "profileStatus"),
      @Index(name = "idx_user_verification_status", columnList = "profileVerificationStatus")
    })
@Getter
@Setter
@ToString
public class MerchantUserProfileStatus extends BaseModel implements Serializable {

  /**
   * The `MerchantUserProfile` entity associated with this status profile. Defines a one-to-one
   * relationship with {@link MerchantUserProfile}.
   */
  @OneToOne
  @MapsId
  @JoinColumn(name = "merchant_user_profile_id", nullable = false)
  private MerchantUserProfile merchantUserProfile;

  /**
   * The current status of the user. This field is converted to and from its string representation
   * using {@link ProfileStatusConverter}.
   */
  @Convert(converter = ProfileStatusConverter.class)
  private ProfileStatus profileStatus = ProfileStatus.ACTIVE;

  @Convert(converter = ProfileVerificationStatusConverter.class)
  private ProfileVerificationStatus profileVerificationStatus =
      ProfileVerificationStatus.PENDING_EMAIL_VERIFICATION;
}
