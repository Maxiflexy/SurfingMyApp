/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.repository;

import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycDocument;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-05(Tue)-2025
 */
@Repository
public interface MerchantKycDocumentRepository extends JpaRepository<MerchantKycDocument, Long> {
  Optional<MerchantKycDocument> findFirstByMerchantProfileMerchantId(
      String merchantProfileMerchantId);
}
