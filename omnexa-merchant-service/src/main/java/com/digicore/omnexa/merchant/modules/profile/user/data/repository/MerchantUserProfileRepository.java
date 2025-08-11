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

  //  /**
  //   * Retrieves paginated merchant user profile information for external service consumption.
  //   *
  //   * @param pageable the pagination information
  //   * @return a page of merchant user profile information responses
  //   */
  //  @Query("""
  //      SELECT new
  // com.digicore.omnexa.merchant.modules.profile.user.dto.response.MerchantUserProfileInfoResponse(
  //          mup.profileId,
  //          CONCAT(mup.firstName, ' ', mup.lastName),
  //          mup.email,
  //          mup.phoneNumber,
  //          mup.profileStatus
  //      )
  //      FROM MerchantUserProfile mup
  //      ORDER BY mup.createdDate DESC
  //      """)
  //  Page<MerchantProfileInfoResponse> findAllMerchantUserProfileInfo(Pageable pageable);
  //
  //  /**
  //   * Searches merchant user profiles by firstName, lastName, or email.
  //   *
  //   * @param searchTerm the search term to match against firstName, lastName, or email
  //   * @param pageable the pagination information
  //   * @return a page of merchant user profile information responses matching the search criteria
  //   */
  //  @Query("""
  //      SELECT new
  // com.digicore.omnexa.merchant.modules.profile.user.dto.response.MerchantUserProfileInfoResponse(
  //          mup.profileId,
  //          CONCAT(mup.firstName, ' ', mup.lastName),
  //          mup.email,
  //          mup.phoneNumber,
  //          mup.profileStatus
  //      )
  //      FROM MerchantUserProfile mup
  //      WHERE LOWER(mup.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
  //         OR LOWER(mup.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
  //         OR LOWER(mup.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
  //      ORDER BY mup.createdDate DESC
  //      """)
  //  Page<MerchantProfileInfoResponse> searchMerchantUserProfileInfo(
  //          @Param("searchTerm") String searchTerm,
  //          Pageable pageable);
  //
  //  /**
  //   * Filters merchant user profiles by profile status.
  //   *
  //   * @param profileStatus the profile status to filter by
  //   * @param pageable the pagination information
  //   * @return a page of merchant user profile information responses with the specified status
  //   */
  //  @Query("""
  //      SELECT new
  // com.digicore.omnexa.merchant.modules.profile.user.dto.response.MerchantUserProfileInfoResponse(
  //          mup.profileId,
  //          CONCAT(mup.firstName, ' ', mup.lastName),
  //          mup.email,
  //          mup.phoneNumber,
  //          mup.profileStatus
  //      )
  //      FROM MerchantUserProfile mup
  //      WHERE mup.profileStatus = :profileStatus
  //      ORDER BY mup.createdDate DESC
  //      """)
  //  Page<MerchantProfileInfoResponse> findMerchantUserProfileInfoByStatus(
  //          @Param("profileStatus") ProfileStatus profileStatus,
  //          Pageable pageable);
}
