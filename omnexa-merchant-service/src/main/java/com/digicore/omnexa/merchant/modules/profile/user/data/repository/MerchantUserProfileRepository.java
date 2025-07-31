/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.user.data.repository;

import com.digicore.omnexa.merchant.modules.profile.user.data.model.MerchantUserProfile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing merchant user profile data.
 *
 * <p>This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations and
 * custom query methods for the {@link MerchantUserProfile} entity.
 *
 * <p>Features: - Supports standard CRUD operations for merchant user profiles. - Provides a custom
 * method to check the existence of a merchant user profile by email.
 *
 * <p>Usage: - Use this repository in service classes to interact with the database. - Example:
 *
 * <pre>
 *   boolean exists = merchantUserProfileRepository.existsByEmail("email@example.com");
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public interface MerchantUserProfileRepository extends JpaRepository<MerchantUserProfile, Long> {

  /**
   * Checks if a merchant user profile exists by email.
   *
   * @param email the email to check.
   * @return {@code true} if a merchant user profile exists with the given email, {@code false}
   *     otherwise.
   */
  boolean existsByEmail(String email);

  @Transactional
  @Modifying
  @Query(
      value =
          """
        UPDATE merchant_user_profile
        SET
            profile_status = CASE WHEN :profileStatus IS NOT NULL THEN :profileStatus ELSE profile_status END,
            profile_verification_status = CASE WHEN :profileVerificationStatus IS NOT NULL THEN :profileVerificationStatus ELSE profile_verification_status END
        WHERE profile_id = :profileId
        """,
      nativeQuery = true)
  void conditionalUpdateProfileStatuses(
      @Param("profileStatus") String profileStatus,
      @Param("profileVerificationStatus") String profileVerificationStatus,
      @Param("profileId") String profileId);
}
