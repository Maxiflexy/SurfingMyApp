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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for performing database operations on BackOfficeUserProfile entities.
 *
 * <p>Provides methods for CRUD operations, custom queries, and updates related to back office user
 * profiles.
 *
 * <p>Author: Onyekachi Ejemba Created On: Jul-08(Tue)-2025
 */
public interface BackOfficeUserRepository extends JpaRepository<BackOfficeUserProfile, Long> {

  /**
   * Finds a back office user by their email address.
   *
   * @param email the email address to search for
   * @return an {@link Optional} containing the user profile if found, or empty if not found
   */
  Optional<BackOfficeUserProfile> findByEmail(String email);

  /**
   * Checks if a back office user exists with the given email address.
   *
   * @param email the email address to check
   * @return true if a user exists with the given email, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Checks if a back office user exists with the given profile ID.
   *
   * @param profileId the profile ID to check
   * @return true if a user exists with the given profile ID, false otherwise
   */
  boolean existsByProfileId(String profileId);

  /**
   * Retrieves profile statuses of a back office user by their email address.
   *
   * @param email the email address to search for
   * @return an {@link Optional} containing the {@link BackOfficeUserProfileDTO} if found, or empty
   *     if not found
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id, b.profileId, b.profileStatus, b.profileVerificationStatus, b.email, b.firstName, b.lastName, b.createdDate) "
          + "FROM BackOfficeUserProfile b WHERE b.email = :email")
  Optional<BackOfficeUserProfileDTO> findProfileStatusesByEmail(@Param("email") String email);

  /**
   * Updates the profile status and verification status of a back office user.
   *
   * @param profileStatus the new profile status to set
   * @param profileVerificationStatus the new profile verification status to set
   * @param profileId the profile ID of the user to update
   */
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

  /**
   * Finds a back office user by their profile ID.
   *
   * @param profileId the profile ID to search for
   * @return an {@link Optional} containing the {@link BackOfficeUserProfileDTO} if found, or empty
   *     if not found
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.email, b.firstName, b.lastName, b.profileId,  b.role) FROM BackOfficeUserProfile b WHERE b.profileId = :profileId")
  Optional<BackOfficeUserProfileDTO> findByProfileId(@Param("profileId") String profileId);

  /**
   * Updates the profile details of a back office user.
   *
   * @param email the new email address to set (optional)
   * @param firstName the new first name to set (optional)
   * @param lastName the new last name to set (optional)
   * @param role the new role to set (optional)
   * @param profileId the profile ID of the user to update
   */
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

  /**
   * Retrieves a paginated list of back office users based on search and filter criteria.
   *
   * @param search the search term to filter users by name or email (optional)
   * @param profileStatus the profile status to filter users by (optional)
   * @param pageable the pagination information
   * @return a {@link Page} containing the list of {@link BackOfficeUserProfileDTO} matching the
   *     criteria
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id, b.profileId, b.profileStatus, b.profileVerificationStatus, b.email, b.firstName, b.lastName, b.createdDate) "
          + "FROM BackOfficeUserProfile b WHERE "
          + "(:search IS NULL OR :search = '' OR "
          + "LOWER(CONCAT(COALESCE(b.firstName, ''), ' ', COALESCE(b.lastName, ''))) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(b.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND "
          + "(:profileStatus IS NULL OR b.profileStatus = :profileStatus) "
          + "ORDER BY b.createdDate DESC")
  Page<BackOfficeUserProfileDTO> findAllUsersPaginated(
      @Param("search") String search,
      @Param("profileStatus") ProfileStatus profileStatus,
      Pageable pageable);

  /**
   * Retrieves a paginated list of all back office users without any filters.
   *
   * @param pageable the pagination information
   * @return a {@link Page} containing the list of {@link BackOfficeUserProfileDTO}
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id, b.profileId, b.profileStatus, b.profileVerificationStatus, b.email, b.firstName, b.lastName, b.createdDate) "
          + "FROM BackOfficeUserProfile b "
          + "ORDER BY b.createdDate DESC")
  Page<BackOfficeUserProfileDTO> findAllUsersPaginated(Pageable pageable);

  /**
   * Retrieves a paginated list of back office users filtered by search term.
   *
   * @param search the search term to filter users by name or email (required)
   * @param pageable the pagination information
   * @return a {@link Page} containing the list of {@link BackOfficeUserProfileDTO} matching the
   *     search criteria
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id, b.profileId, b.profileStatus, b.profileVerificationStatus, b.email, b.firstName, b.lastName, b.createdDate) "
          + "FROM BackOfficeUserProfile b WHERE "
          + "LOWER(CONCAT(COALESCE(b.firstName, ''), ' ', COALESCE(b.lastName, ''))) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(b.email) LIKE LOWER(CONCAT('%', :search, '%')) "
          + "ORDER BY b.createdDate DESC")
  Page<BackOfficeUserProfileDTO> findUsersBySearchPaginated(
      @Param("search") String search, Pageable pageable);

  /**
   * Retrieves a paginated list of back office users filtered by profile status.
   *
   * @param profileStatus the profile status to filter users by (required)
   * @param pageable the pagination information
   * @return a {@link Page} containing the list of {@link BackOfficeUserProfileDTO} matching the
   *     status criteria
   */
  @Query(
      "SELECT new "
          + BACKOFFICE_USER_PROFILE_DTO
          + "(b.id, b.profileId, b.profileStatus, b.profileVerificationStatus, b.email, b.firstName, b.lastName, b.createdDate) "
          + "FROM BackOfficeUserProfile b WHERE "
          + "b.profileStatus = :profileStatus "
          + "ORDER BY b.createdDate DESC")
  Page<BackOfficeUserProfileDTO> findUsersByStatusPaginated(
      @Param("profileStatus") ProfileStatus profileStatus, Pageable pageable);
}
