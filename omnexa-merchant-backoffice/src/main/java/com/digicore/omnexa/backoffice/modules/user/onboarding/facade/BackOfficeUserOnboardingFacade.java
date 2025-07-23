/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.facade;

import com.digicore.omnexa.backoffice.modules.user.authentication.dto.BackOfficeUserAuthProfileOnboardingRequest;
import com.digicore.omnexa.backoffice.modules.user.onboarding.mapper.BackOfficeUserOnboardingMapper;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.request.BackOfficeProfileEditRequest;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeProfileEditResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade for handling back office user onboarding operations.
 *
 * <p>This class implements the {@link Facade} interface to process onboarding requests
 * and manage user profiles. It integrates with profile services to validate, update,
 * and create user profiles, as well as to manage profile statuses.
 *
 * <p>Author: Onyekachi Ejemba
 * Created On: Jul-08(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeUserOnboardingFacade implements Facade<OnboardingRequest, Void> {

  private final ProfileService backOfficeUserProfileService; // Service for managing user profiles
  private final ProfileService backOfficeUserAuthProfileService; // Service for managing authentication profiles

  /**
   * Processes an onboarding request and updates the user profile accordingly.
   *
   * <p>This method validates the onboarding request, updates the user profile,
   * creates an authentication profile, and sets the profile status to active and verified.
   *
   * @param request the onboarding request to process
   * @return an empty {@link Optional} as the method does not return a value
   */
  @Override
  public Optional<Void> process(OnboardingRequest request) {
    ProfileEditResponse profileEditResponse =
        backOfficeUserProfileService.validateOnboardingRequest(request);

    if (profileEditResponse
        instanceof BackOfficeProfileEditResponse backOfficeProfileEditResponse) {
      // Map the onboarding request to a profile edit request
      BackOfficeProfileEditRequest backOfficeProfileEditRequest =
          BackOfficeUserOnboardingMapper
              .mapBackOfficeUserOnboardingRequestToBackOfficeUserEditRequest(request);
      backOfficeProfileEditRequest.setProfileId(backOfficeProfileEditResponse.getProfileId());
      backOfficeUserProfileService.updateProfile(backOfficeProfileEditRequest);

      // Create an authentication profile for the user
      BackOfficeUserAuthProfileOnboardingRequest authProfileOnboardingRequest =
          new BackOfficeUserAuthProfileOnboardingRequest();
      BeanUtilWrapper.copyNonNullProperties(
          backOfficeProfileEditResponse, authProfileOnboardingRequest);
      authProfileOnboardingRequest.setBackOfficeUserProfileId(
          backOfficeProfileEditResponse.getBackOfficeUserProfileId());

      // Set additional properties for signup requests
      if (request instanceof SignupRequest signupRequest) {
        authProfileOnboardingRequest.setPassword(signupRequest.getPassword());
        authProfileOnboardingRequest.setUsername(signupRequest.getEmail());
      }

      backOfficeUserAuthProfileService.createProfile(authProfileOnboardingRequest);

      // Update the profile status to active and verified
      backOfficeUserProfileService.updateProfileStatus(
          backOfficeProfileEditResponse.getProfileId(),
          ProfileStatus.ACTIVE,
          ProfileVerificationStatus.VERIFIED);
    }

    return Optional.empty();
  }

  /**
   * Returns the type of the facade.
   *
   * @return a string representing the type of the facade
   */
  @Override
  public String getType() {
    return "backOfficeUserOnboarding";
  }
}