/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.helper;

import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.security.SecurityPropertyConfig;
import com.digicore.omnexa.common.lib.util.AESUtil;
import com.digicore.omnexa.merchant.modules.authentication.data.repository.MerchantUserAuthProfileRepository;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantProfileRepository;
import com.digicore.omnexa.merchant.modules.profile.user.data.model.MerchantUserProfile;
import com.digicore.omnexa.merchant.modules.profile.user.data.repository.MerchantUserProfileRepository;
import jakarta.persistence.EntityManager;
import java.security.SecureRandom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Component
@RequiredArgsConstructor
public class ProfileHelper {

  public static final String MERCHANT_ID_PREFIX = "S-";

  public static final int RANDOM_ID_BOUND = 900000;

  public static final int RANDOM_ID_OFFSET = 100000;
  @Getter private final MerchantProfileRepository merchantProfileRepository;
  @Getter private final MerchantUserProfileRepository merchantUserProfileRepository;
  @Getter private final MessagePropertyConfig messagePropertyConfig;
  @Getter private final SecureRandom secureRandom = new SecureRandom();
  private final EntityManager entityManager;
  @Getter private final SecurityPropertyConfig securityPropertyConfig;
  @Getter private final MerchantUserAuthProfileRepository merchantUserAuthProfileRepository;
  @Getter private final PasswordEncoder passwordEncoder;
  public static final String MERCHANT_PROFILE_CREATION_FAILED = "Merchant profile creation failed";

  // Create lazy reference to merchant profile
  public MerchantProfile getMerchantProfileByReference(Long id) {
    return entityManager.getReference(MerchantProfile.class, id);
  }

  // Create lazy reference to merchant user profile
  public MerchantUserProfile getMerchantUserProfileByReference(Long id) {
    return entityManager.getReference(MerchantUserProfile.class, id);
  }

  public String encrypt(String data) {
    return AESUtil.encrypt(data, securityPropertyConfig.getSystemKey());
  }

  public boolean merchantIsVerified(String merchantId) {
    return merchantProfileRepository.existsByMerchantIdAndProfileVerificationStatus(
        merchantId, ProfileVerificationStatus.VERIFIED);
  }
}
