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
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade for back office user operations.
 *
 * <p>Provides a simplified interface for user-related operations following the facade pattern.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeUserOnboardingFacade implements Facade<OnboardingRequest, Void> {
  private final ProfileService backOfficeUserProfileService;
  private final ProfileService backOfficeUserAuthProfileService;

  @Override
  public Optional<Void> process(OnboardingRequest request) {
    ProfileEditResponse profileEditResponse =
        backOfficeUserProfileService.validateOnboardingRequest(request);
    if (profileEditResponse
        instanceof BackOfficeProfileEditResponse backOfficeProfileEditResponse) {
      BackOfficeProfileEditRequest backOfficeProfileEditRequest =
          BackOfficeUserOnboardingMapper
              .mapBackOfficeUserOnboardingRequestToBackOfficeUserEditRequest(request);
      backOfficeProfileEditRequest.setProfileId(backOfficeProfileEditResponse.getProfileId());
      backOfficeUserProfileService.updateProfile(backOfficeProfileEditRequest);

      BackOfficeUserAuthProfileOnboardingRequest authProfileOnboardingRequest =
          new BackOfficeUserAuthProfileOnboardingRequest();
      BeanUtilWrapper.copyNonNullProperties(
          backOfficeProfileEditResponse, authProfileOnboardingRequest);
      authProfileOnboardingRequest.setBackOfficeUserProfileId(
          backOfficeProfileEditResponse.getBackOfficeUserProfileId());
      backOfficeUserAuthProfileService.createProfile(authProfileOnboardingRequest);

      backOfficeUserProfileService.updateProfileStatus(
          backOfficeProfileEditResponse.getProfileId(),
          ProfileStatus.ACTIVE,
          ProfileVerificationStatus.VERIFIED);
    }

    return Optional.empty();
  }

  @Override
  public String getType() {
    return "backOfficeUserOnboarding";
  }
}
