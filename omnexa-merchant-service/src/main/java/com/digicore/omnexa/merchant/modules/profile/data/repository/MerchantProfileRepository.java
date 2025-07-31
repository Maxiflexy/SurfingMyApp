/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.repository;

import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing merchant profile data.
 *
 * <p>This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations and
 * custom query methods for the {@link MerchantProfile} entity.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {

  /**
   * Checks if a merchant profile exists by business name or business email.
   *
   * @param businessName the business name to check.
   * @param businessEmail the business email to check.
   * @return {@code true} if a merchant profile exists with the given business name or email, {@code
   *     false} otherwise.
   */
  boolean existsByBusinessNameOrBusinessEmail(String businessName, String businessEmail);

  boolean existsByMerchantIdAndProfileVerificationStatus(
      String merchantId, ProfileVerificationStatus status);

  @Query("SELECT m.id FROM MerchantProfile m WHERE m.merchantId = :merchantId")
  Optional<Long> findIdByMerchantId(@Param("merchantId") String merchantId);

  @Transactional
  @Modifying
  @Query(
      value =
          """
        UPDATE merchant_profile
        SET
            profile_status = CASE WHEN :profileStatus IS NOT NULL THEN :profileStatus ELSE profile_status END,
            profile_verification_status = CASE WHEN :profileVerificationStatus IS NOT NULL THEN :profileVerificationStatus ELSE profile_verification_status END
        WHERE merchant_id = :merchantId
        """,
      nativeQuery = true)
  void conditionalUpdateProfileStatuses(
      @Param("profileStatus") String profileStatus,
      @Param("profileVerificationStatus") String profileVerificationStatus,
      @Param("merchantId") String merchantId);
}
