/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.data.model;

import com.digicore.omnexa.common.lib.model.BaseAuthModel;
import com.digicore.omnexa.common.lib.model.BaseUserModel;
import com.digicore.omnexa.merchant.modules.profile.user.data.model.MerchantUserProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing the authentication profile of a merchant user.
 *
 * <p>This class extends {@link BaseUserModel} to inherit common user properties and implements
 * {@link Serializable} for object serialization. It is mapped to the `merchant_user_auth_profile`
 * table in the database.
 *
 * <p>Features: - Establishes a one-to-one relationship with {@link MerchantUserProfile}. - Utilizes
 * JPA annotations for ORM mapping. - Includes Lombok annotations for getter, setter, and toString
 * methods.
 *
 * <p>Usage: - This class is used to store and manage authentication-related data for merchant
 * users. - Example:
 *
 * <pre>
 *   MerchantUserAuthProfile authProfile = new MerchantUserAuthProfile();
 *   authProfile.setMerchantUserProfile(userProfile);
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Entity
@Table(
    name = "merchant_user_auth_profile",
    indexes = {@Index(name = "idx_username", columnList = "username")})
@Getter
@Setter
@ToString
public class MerchantUserAuthProfile extends BaseAuthModel implements Serializable {

  /**
   * The associated merchant user profile.
   *
   * <p>This field establishes a one-to-one relationship with {@link MerchantUserProfile}. It is
   * mapped by the `merchantUserProfile` field in the {@link MerchantUserProfile} class. Cascade
   * operations are applied to ensure changes propagate to the related entity.
   */
  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "merchant_user_profile_id", nullable = false)
  private MerchantUserProfile merchantUserProfile;
}
