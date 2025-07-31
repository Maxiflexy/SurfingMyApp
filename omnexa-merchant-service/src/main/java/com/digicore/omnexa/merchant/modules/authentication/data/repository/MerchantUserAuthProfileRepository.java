/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.data.repository;

import static com.digicore.omnexa.merchant.modules.authentication.helper.LoginHelper.LOGIN_DETAIL_DTO_CLASS_NAME;

import com.digicore.omnexa.merchant.modules.authentication.data.model.MerchantUserAuthProfile;
import com.digicore.omnexa.merchant.modules.authentication.dto.response.MerchantLoginProfileDTO;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
public interface MerchantUserAuthProfileRepository
    extends JpaRepository<MerchantUserAuthProfile, Long> {
  boolean existsByUsername(String username);

  @Query(
      "SELECT new "
          + LOGIN_DETAIL_DTO_CLASS_NAME
          + "( mp.merchantId,"
          + "        mp.businessName,"
          + "        CONCAT(mup.firstName, ' ', mup.lastName),"
          + "        mup.role,"
          + "        mp.merchantKycProfile.kycStatus,"
          + "        auth.username,"
          + "        auth.password) FROM MerchantUserAuthProfile auth"
          + "    JOIN auth.merchantUserProfile mup"
          + "    JOIN mup.merchantProfile mp"
          + "    WHERE auth.username = :username"
          + "      AND mup.deleted = false")
  Optional<MerchantLoginProfileDTO> findByUsername(String username);
}
