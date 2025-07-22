/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.data.repository;

import static com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO.BACKOFFICE_USER_PROFILE_DTO;

import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for BackOfficeUser entity operations.
 *
 * <p>Provides database operations for back office users including finding users by email and
 * checking existence.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
public interface BackOfficeUserRepository extends JpaRepository<BackOfficeUserProfile, Long> {

  /**
   * Finds a back office user by email.
   *
   * @param email the email to search for
   * @return an Optional containing the user if found
   */
  Optional<BackOfficeUserProfile> findByEmail(String email);

  /**
   * Checks if a user exists with the given email.
   *
   * @param email the email to check
   * @return true if user exists, false otherwise
   */
  boolean existsByEmail(String email);

  boolean existsByProfileId(String profileId);

  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id,b.profileId, b.profileStatus, b.profileVerificationStatus) FROM BackOfficeUserProfile b WHERE b.email = :email")
  Optional<BackOfficeUserProfileDTO> findProfileStatusesByEmail(@Param("email") String email);

  @Transactional
  @Modifying
  @Query(
      """
    UPDATE BackOfficeUserProfile b
    SET
        b.profileStatus = COALESCE(:profileStatus, b.profileStatus),
        b.profileVerificationStatus = COALESCE(:profileVerificationStatus, b.profileVerificationStatus)
    WHERE b.profileId = :profileId
""")
  void updateProfileStatuses(
      @Param("profileStatus") ProfileStatus profileStatus,
      @Param("profileVerificationStatus") ProfileVerificationStatus profileVerificationStatus,
      @Param("profileId") String profileId);

  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.email, b.firstName, b.lastName, b.profileId,  b.role) FROM BackOfficeUserProfile b WHERE b.profileId = :profileId")
  Optional<BackOfficeUserProfileDTO> findByProfileId(@Param("profileId") String profileId);

  @Transactional
  @Modifying
  @Query(
      """
    UPDATE BackOfficeUserProfile b
    SET
        b.email = COALESCE(:email, b.email),
        b.firstName = COALESCE(:firstName, b.firstName),
        b.lastName = COALESCE(:lastName, b.lastName),
        b.role = COALESCE(:role, b.role)
    WHERE b.profileId = :profileId
    """)
  void updateProfile(
      @Param("email") String email,
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("role") String role,
      @Param("profileId") String profileId);
}
