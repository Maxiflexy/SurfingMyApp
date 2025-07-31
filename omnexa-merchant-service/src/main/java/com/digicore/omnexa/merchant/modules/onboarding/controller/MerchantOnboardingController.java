/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.onboarding.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.OnboardingSwaggerDoc.*;
import static com.digicore.omnexa.merchant.modules.onboarding.facade.MerchantOnboardingFacade.MERCHANT_ONBOARDING;
import static com.digicore.omnexa.merchant.modules.onboarding.facade.MerchantVerificationFacade.MERCHANT_EMAIL_VERIFICATION;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantOnboardingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling merchant onboarding operations.
 *
 * <p>This class provides an API endpoint for onboarding merchants. It uses Spring Boot's {@link
 * RestController} annotation to define a RESTful controller and integrates Swagger documentation
 * for API visibility.
 *
 * <p>Features: - Defines the base API path for merchant onboarding. - Provides a POST endpoint for
 * onboarding merchants. - Validates incoming requests using Jakarta Bean Validation. - Resolves and
 * processes the onboarding request using a facade pattern. - Returns a success response upon
 * successful processing.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-23(Mon)-2025
 */
@RestController
@RequestMapping(API_V1 + ONBOARDING_API)
@RequiredArgsConstructor
@Tag(name = ONBOARDING_CONTROLLER_TITLE, description = ONBOARDING_CONTROLLER_DESCRIPTION)
public class MerchantOnboardingController {
  private final FacadeResolver facadeResolver;

  /**
   * Endpoint for onboarding a merchant.
   *
   * <p>This method handles POST requests to onboard a merchant. It validates the incoming request,
   * resolves the appropriate facade, processes the request, and returns a success response.
   *
   * @param onboardingRequest the request payload containing merchant onboarding details.
   * @return a {@link ResponseEntity} containing the success message.
   */
  @PostMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
  public ResponseEntity<Object> onboardMerchant(
      @Valid @RequestBody MerchantOnboardingRequest onboardingRequest) {
    Facade<MerchantOnboardingRequest, Void> facade = facadeResolver.resolve(MERCHANT_ONBOARDING);
    facade.process(onboardingRequest);
    return ControllerResponse.buildCreateSuccessResponse("Merchant Onboarding Successful");
  }

  @PatchMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_VERIFY_TITLE,
      description = ONBOARDING_CONTROLLER_VERIFY_DESCRIPTION)
  public ResponseEntity<Object> verifyMerchant(@RequestParam String verificationToken) {
    Facade<String, Void> facade = facadeResolver.resolve(MERCHANT_EMAIL_VERIFICATION);
    facade.process(verificationToken);
    return ControllerResponse.buildSuccessResponse("Verification Successful");
  }
}
