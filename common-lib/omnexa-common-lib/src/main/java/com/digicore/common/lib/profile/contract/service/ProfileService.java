/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.profile.contract.service;

import com.digicore.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.common.lib.profile.contract.request.ProfileEditRequest;
import com.digicore.common.lib.profile.contract.response.ProfileEditResponse;
import com.digicore.common.lib.profile.contract.response.ProfileInfoResponse;
import java.util.Optional;

/**
 * Service interface for profile management.
 *
 * <p>This interface defines the contract for managing profiles within the application. It provides
 * methods for creating, updating, retrieving, and deleting profiles.
 *
 * <p>Features: - Supports onboarding requests for profile creation. - Allows profile updates
 * through edit requests. - Provides retrieval of profile information by ID. - Enables profile
 * deletion by ID.
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
   * Retrieves a profile by its ID.
   *
   * @param profileId the ID of the profile to retrieve.
   * @return an Optional containing the profile information response if found, or an empty Optional
   *     if not found.
   */
  default Optional<ProfileInfoResponse> getProfileById(String profileId) {
    return null;
  }

  /**
   * Deletes a profile by its ID.
   *
   * @param profileId the ID of the profile to delete.
   */
  default void deleteProfile(String profileId) {}
  ;
}
