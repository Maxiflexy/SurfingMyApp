/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.repository;

import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-04(Mon)-2025
 */
@Repository
public interface MerchantKycProfileRepository extends JpaRepository<MerchantKycProfile, Long> {
  Optional<MerchantKycProfile> findFirstByMerchantProfileMerchantId(String merchantId);
}
