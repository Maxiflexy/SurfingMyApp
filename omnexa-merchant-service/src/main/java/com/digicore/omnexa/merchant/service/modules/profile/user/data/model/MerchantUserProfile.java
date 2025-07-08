/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.user.data.model;

import com.digicore.omnexa.common.lib.model.BaseUserModel;
import com.digicore.omnexa.merchant.service.modules.authentication.data.model.MerchantUserAuthProfile;
import com.digicore.omnexa.merchant.service.modules.profile.data.model.MerchantProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a user profile associated with a merchant in the system. This entity is mapped to the
 * `merchant_user_profile` table in the database.
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Entity}: Specifies that this class is a JPA entity.
 *   <li>{@link Table}: Maps this entity to the `merchant_user_profile` table.
 *   <li>{@link Getter}, {@link Setter}, {@link ToString}: Lombok annotations for generating
 *       boilerplate code.
 *   <li>{@link ManyToOne}: Defines a many-to-one relationship with {@link MerchantProfile}.
 *   <li>{@link JoinColumn}: Specifies the foreign key column for the `merchantProfile` field.
 *   <li>{@link OneToOne}: Defines a one-to-one relationship with {@link MerchantUserProfileStatus}.
 * </ul>
 *
 * <p>Fields:
 *
 * <ul>
 *   <li>merchantProfile: The merchant profile associated with this user.
 *   <li>merchantUserStatusProfile: The status profile associated with this user.
 *   <li>role: The role of the user within the merchant's organization.
 * </ul>
 *
 * <p>Inheritance:
 *
 * <ul>
 *   <li>Extends {@link BaseUserModel} for common user model properties.
 *   <li>Implements {@link Serializable} for object serialization.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Entity
@Table(
    name = "merchant_user_profile",
    indexes = {
      @Index(name = "idx_merchant_profile_id", columnList = "merchant_profile_id"),
      @Index(name = "idx_user_role", columnList = "role")
    })
@Getter
@Setter
@ToString
public class MerchantUserProfile extends BaseUserModel implements Serializable {

  /**
   * The merchant profile associated with this user. Defines a many-to-one relationship with {@link
   * MerchantProfile}.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "merchant_profile_id", nullable = false)
  private MerchantProfile merchantProfile;

  /**
   * The status profile associated with this user. Defines a one-to-one relationship with {@link
   * MerchantUserProfileStatus}.
   */
  @OneToOne(mappedBy = "merchantUserProfile", cascade = CascadeType.ALL, optional = false)
  private MerchantUserProfileStatus merchantUserProfileStatus;

  /**
   * The auth profile associated with this user. Defines a one-to-one relationship with {@link
   * MerchantUserProfileStatus}.
   */
  @OneToOne(mappedBy = "merchantUserProfile", cascade = CascadeType.ALL, optional = false)
  private MerchantUserAuthProfile merchantUserAuthProfile;

  /** The role of the user within the merchant's organization. */
  private String role;
}
