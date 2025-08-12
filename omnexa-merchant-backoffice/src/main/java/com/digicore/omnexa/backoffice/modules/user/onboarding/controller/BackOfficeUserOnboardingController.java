/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.onboarding.OnboardingSwaggerDoc.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office user onboarding operations.
 *
 * <p>Provides endpoints for user invitation, retrieval, and signup processes. User management
 * operations (listing, searching, filtering) have been moved to the
 * BackOfficeUserManagementController to follow SOLID principles.
 *
 * <p>Each endpoint is documented with its purpose, input parameters, and response details. This
 * controller uses Spring Boot annotations for request mapping and validation. It also integrates
 * with a facade resolver and user profile service for business logic.
 *
 * <p>Author: Onyekachi Ejemba Created On: Jul-08(Tue)-2025
 */
@RestController
@RequestMapping(API_V1 + ONBOARDING_API)
@RequiredArgsConstructor
@Tag(name = ONBOARDING_CONTROLLER_TITLE, description = ONBOARDING_CONTROLLER_DESCRIPTION)
public class BackOfficeUserOnboardingController {
  private final FacadeResolver facadeResolver;
  private final ProfileService backOfficeUserProfileService;

  /**
   * Retrieves the profile of a back office user by email.
   *
   * @param email the email address of the user to retrieve
   * @return a response entity containing the user profile or null if not found
   */
  @GetMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_RETRIEVE_TITLE,
      description = ONBOARDING_CONTROLLER_RETRIEVE_DESCRIPTION)
  public ResponseEntity<Object> retrieveBackOfficeUser(@RequestParam("email") String email) {
    return ControllerResponse.buildCreateSuccessResponse(
        backOfficeUserProfileService.getProfileByEmail(email));
  }

  /**
   * Completes the signup process for a back office user.
   *
   * @param signupRequest the request payload containing signup details
   * @return a response entity with a success message and HTTP status 201
   */
  @PatchMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_COMPLETE_SIGNUP_TITLE,
      description = ONBOARDING_CONTROLLER_COMPLETE_SIGNUP_DESCRIPTION)
  public ResponseEntity<Object> completeSignUp(@Valid @RequestBody SignupRequest signupRequest) {
    Facade<UserInviteRequest, Void> facade = facadeResolver.resolve("backOfficeUserOnboarding");
    facade.process(signupRequest);
    return ControllerResponse.buildCreateSuccessResponse("Onboarding Successful");
  }
}
