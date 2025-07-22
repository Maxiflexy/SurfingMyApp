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
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
@Component
public class BackOfficeUserOnboardingMapper {

  public static BackOfficeProfileEditRequest
      mapBackOfficeUserOnboardingRequestToBackOfficeUserEditRequest(
          OnboardingRequest onboardingRequest) {
    if (onboardingRequest instanceof SignupRequest signupRequest) {
      return mapSignupRequestToBackOfficeProfileEditRequest(signupRequest);
    }
    throw new OmnexaException("Invalid onboarding request", HttpStatus.BAD_REQUEST);
  }

  private static BackOfficeProfileEditRequest mapSignupRequestToBackOfficeProfileEditRequest(
      SignupRequest signupRequest) {
    BackOfficeProfileEditRequest backOfficeProfileEditRequest = new BackOfficeProfileEditRequest();
    BeanUtilWrapper.copyNonNullProperties(signupRequest, backOfficeProfileEditRequest);
    return backOfficeProfileEditRequest;
  }
}
