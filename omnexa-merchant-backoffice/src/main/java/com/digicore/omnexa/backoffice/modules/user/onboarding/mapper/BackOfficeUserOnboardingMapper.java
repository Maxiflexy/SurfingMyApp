/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.mapper;

import com.digicore.omnexa.backoffice.modules.user.profile.dto.request.BackOfficeProfileEditRequest;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting onboarding-related requests to back office user profile edit
 * requests.
 *
 * <p>This class provides utility methods to map different types of onboarding requests to a
 * standardized format used for editing back office user profiles.
 *
 * <p>Author: Oluwatobi Ogunwuyi Created On: Jul-18(Fri)-2025
 */
@Component
public class BackOfficeUserOnboardingMapper {

  /**
   * Maps an onboarding request to a back office user profile edit request.
   *
   * @param onboardingRequest the onboarding request to be mapped
   * @return a {@link BackOfficeProfileEditRequest} containing the mapped data
   * @throws OmnexaException if the provided onboarding request is invalid
   */
  public static BackOfficeProfileEditRequest
      mapBackOfficeUserOnboardingRequestToBackOfficeUserEditRequest(
          OnboardingRequest onboardingRequest) {
    if (onboardingRequest instanceof SignupRequest signupRequest) {
      return mapSignupRequestToBackOfficeProfileEditRequest(signupRequest);
    }
    throw new OmnexaException("Invalid onboarding request", HttpStatus.BAD_REQUEST);
  }

  /**
   * Maps a signup request to a back office user profile edit request.
   *
   * @param signupRequest the signup request to be mapped
   * @return a {@link BackOfficeProfileEditRequest} containing the mapped data
   */
  private static BackOfficeProfileEditRequest mapSignupRequestToBackOfficeProfileEditRequest(
      SignupRequest signupRequest) {
    BackOfficeProfileEditRequest backOfficeProfileEditRequest = new BackOfficeProfileEditRequest();
    BeanUtilWrapper.copyNonNullProperties(signupRequest, backOfficeProfileEditRequest);
    backOfficeProfileEditRequest.setPassword(signupRequest.getPassword());
    return backOfficeProfileEditRequest;
  }
}
