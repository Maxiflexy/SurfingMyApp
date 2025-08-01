/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.profile.contract;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.dto.request.ProfileEditRequest;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;

/**
 * Service interface for profile management.
 *
 * <p>This interface defines the contract for managing profiles within the application. It provides
 * methods for creating, updating, retrieving, and deleting profiles.
 *
 * <p>Features: - Supports onboarding requests for profile creation. - Allows profile updates
 * through edit requests. - Provides retrieval of profile information by ID. - Enables profile
 * deletion by ID. - Supports search and filtering capabilities with pagination.
 *
 * <p>Usage: - Implement this interface in a class to provide the actual business logic for profile
 * management. - Example:
 *
 * <pre>
 *   public class ProfileServiceImpl implements ProfileService {
 *       // Implement methods here
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
public interface ProfileService {

  /**
   * Creates a new profile based on the provided onboarding request.
   *
   * @param request the onboarding request containing profile details.
   * @return the response containing the result of the profile creation.
   */
  OnboardingResponse createProfile(OnboardingRequest request);

  /**
   * Updates an existing profile based on the provided edit request.
   *
   * @param request the profile edit request containing updated profile details.
   * @return the response containing the result of the profile update.
   */
  default ProfileEditResponse updateProfile(ProfileEditRequest request) {
    return null;
  }

  /**
   * Updates the status of a profile, e.g., after invitation acceptance.
   *
   * @param profileId the ID of the profile.
   * @param profileStatus the status to set.
   * @param profileVerificationStatus the verification status to set.
   */
  default void updateProfileStatus(
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {}

  /**
   * Retrieves a profile by its ID.
   *
   * @param profileId the ID of the profile to retrieve.
   * @return an Optional containing the profile information response if found, or an empty Optional
   *     if not found.
   */
  default ProfileInfoResponse getProfileById(String profileId) {
    return null;
  }

  /**
   * Retrieves a paginated list of all profiles.
   *
   * @param pageNumber the page number (1-based, optional)
   * @param pageSize the page size (optional)
   * @return a paginated response containing profile information
   */
  default PaginatedResponse<ProfileInfoResponse> getAllProfiles(
      Integer pageNumber, Integer pageSize) {
    return null;
  }

  /**
   * Retrieves a paginated list of profiles filtered by search term.
   *
   * @param search the search term to filter profiles by name or email (required)
   * @param pageNumber the page number (1-based, optional)
   * @param pageSize the page size (optional)
   * @return a paginated response containing filtered profile information
   */
  default PaginatedResponse<ProfileInfoResponse> searchProfilesPaginated(
          String search, Integer pageNumber, Integer pageSize) {
    return null;
  }

  /**
   * Retrieves a paginated list of profiles filtered by profile status.
   *
   * @param profileStatus the profile status to filter by (required)
   * @param pageNumber the page number (1-based, optional)
   * @param pageSize the page size (optional)
   * @return a paginated response containing filtered profile information
   */
  default PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
          String profileStatus, Integer pageNumber, Integer pageSize) {
    return null;
  }

  default ProfileInfoResponse getProfileByEmail(String email) {
    return null;
  }

  default ProfileEditResponse validateOnboardingRequest(OnboardingRequest request) {
    return null;
  }

  /**
   * Deletes a profile by its ID.
   *
   * @param profileId the ID of the profile to delete.
   */
  default void deleteProfile(String profileId) {}
}
