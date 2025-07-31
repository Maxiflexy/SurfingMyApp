/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.onboarding.facade;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantApiKeyOnboardingRequest;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade implementation for handling merchant onboarding operations.
 *
 * <p>This class orchestrates the merchant onboarding process by creating four profiles: merchant
 * profile, merchant user profile, merchant user auth profile and merchant api key profile.
 *
 * <p>The facade ensures atomic operations through transactional boundaries and provides clear error
 * handling for failed onboarding scenarios.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-23(Mon)-2025
 */
@Component
@RequiredArgsConstructor
public class MerchantOnboardingFacade implements Facade<OnboardingRequest, Void> {
  private static final Logger log = LoggerFactory.getLogger(MerchantOnboardingFacade.class);

  public static final String MERCHANT_ONBOARDING = "merchantOnboarding";

  private final ProfileService merchantProfileService;
  private final ProfileService merchantUserProfileService;
  private final ProfileService merchantUserAuthProfileService;
  private final ProfileService merchantApiKeyProfileService;

  /**
   * Processes the merchant onboarding request atomically.
   *
   * @param request the onboarding request containing merchant details
   * @throws OmnexaException if any step in the onboarding process fails
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public Optional<Void> process(OnboardingRequest request) {
    try {
      // Create merchant profile
      OnboardingResponse merchantResponse = merchantProfileService.createProfile(request);
      MerchantOnboardingResponse merchantOnboardingResponse =
          castToMerchantResponse(merchantResponse);

      // Create merchant user profile
      MerchantUserOnboardingRequest userRequest =
          buildUserOnboardingRequest(request, merchantOnboardingResponse);
      OnboardingResponse userResponse = merchantUserProfileService.createProfile(userRequest);
      MerchantOnboardingResponse userOnboardingResponse = castToMerchantResponse(userResponse);

      // Create merchant user auth profile
      MerchantUserOnboardingRequest authRequest =
          buildAuthOnboardingRequest(request, merchantOnboardingResponse, userOnboardingResponse);
      merchantUserAuthProfileService.createProfile(authRequest);

      // Create merchant api key profile
      MerchantApiKeyOnboardingRequest apiKeyRequest =
          buildApiKeyOnboardingRequest(merchantOnboardingResponse);
      merchantApiKeyProfileService.createProfile(apiKeyRequest);

      // TODO: send verification email

      log.info(
          "Successfully completed merchant onboarding for merchant: {}",
          merchantOnboardingResponse.getMerchantProfileId());
      return Optional.empty();

    } catch (Exception e) {
      log.error(
          "Merchant onboarding failed for request: {} because of: {}", request, e.getMessage());
      throw new OmnexaException("Merchant onboarding process failed", e);
    }
  }

  /**
   * Safely casts OnboardingResponse to MerchantOnboardingResponse.
   *
   * @param response the response to cast
   * @return MerchantOnboardingResponse
   * @throws ClassCastException if casting fails
   */
  private MerchantOnboardingResponse castToMerchantResponse(OnboardingResponse response) {
    return (MerchantOnboardingResponse) response;
  }

  /**
   * Builds merchant user onboarding request from original request and merchant response.
   *
   * @param originalRequest the original onboarding request
   * @param merchantResponse the merchant profile creation response
   * @return configured MerchantUserOnboardingRequest
   */
  private MerchantUserOnboardingRequest buildUserOnboardingRequest(
      OnboardingRequest originalRequest, MerchantOnboardingResponse merchantResponse) {

    MerchantUserOnboardingRequest userRequest = new MerchantUserOnboardingRequest();
    BeanUtilWrapper.copyNonNullProperties(originalRequest, userRequest);
    userRequest.setMerchantProfileId(merchantResponse.getMerchantProfileId());
    return userRequest;
  }

  /**
   * Builds merchant user auth onboarding request from original request and previous responses.
   *
   * @param originalRequest the original onboarding request
   * @param merchantResponse the merchant profile creation response
   * @param userResponse the user profile creation response
   * @return configured MerchantUserOnboardingRequest for auth
   */
  private MerchantUserOnboardingRequest buildAuthOnboardingRequest(
      OnboardingRequest originalRequest,
      MerchantOnboardingResponse merchantResponse,
      MerchantOnboardingResponse userResponse) {

    MerchantUserOnboardingRequest authRequest = new MerchantUserOnboardingRequest();
    BeanUtilWrapper.copyNonNullProperties(originalRequest, authRequest);
    authRequest.setUsername(merchantResponse.getUsername());
    authRequest.setMerchantProfileId(userResponse.getMerchantProfileId());
    return authRequest;
  }

  private MerchantApiKeyOnboardingRequest buildApiKeyOnboardingRequest(
      MerchantOnboardingResponse merchantResponse) {

    MerchantApiKeyOnboardingRequest userRequest = new MerchantApiKeyOnboardingRequest();
    userRequest.setMerchantProfileId(merchantResponse.getMerchantProfileId());
    return userRequest;
  }

  @Override
  public String getType() {
    return MERCHANT_ONBOARDING;
  }
}
