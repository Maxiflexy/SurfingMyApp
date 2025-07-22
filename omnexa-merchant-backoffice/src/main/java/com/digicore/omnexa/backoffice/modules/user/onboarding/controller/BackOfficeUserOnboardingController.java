/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.OnboardingSwaggerDoc.*;

import com.digicore.omnexa.backoffice.modules.user.profile.service.BackOfficeUserProfileService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office user management operations.
 *
 * <p>Provides endpoints for user invitation and signup processes.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@RestController
@RequestMapping(API_V1 + ONBOARDING_API)
@RequiredArgsConstructor
@Tag(name = ONBOARDING_CONTROLLER_TITLE, description = ONBOARDING_CONTROLLER_DESCRIPTION)
public class BackOfficeUserOnboardingController {
  private final FacadeResolver facadeResolver;
  private final BackOfficeUserProfileService backOfficeUserProfileService;

  /**
   * Invites a new user to the back office system.
   *
   * @return response with invitation details
   */
  @PostMapping()
  @PreAuthorize("hasAuthority('invite-backoffice-user')")
  @Operation(
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
  public ResponseEntity<Object> inviteBackOfficeUser(
      @Valid @RequestBody UserInviteRequest userInviteRequest) {
    Facade<UserInviteRequest, Void> facade = facadeResolver.resolve("backOfficeUserInvitation");
    facade.process(userInviteRequest);
    return ControllerResponse.buildCreateSuccessResponse(
        "BackOffice User Invitation sent Successful");
  }

  @GetMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
  public ResponseEntity<Object> retrieveBackOfficeUser(@RequestParam("email") String email) {
    return ControllerResponse.buildCreateSuccessResponse(
        backOfficeUserProfileService.getProfileByEmail(email));
  }

  @PatchMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
  public ResponseEntity<Object> completeSignUp(@Valid @RequestBody SignupRequest signupRequest) {
    Facade<UserInviteRequest, Void> facade = facadeResolver.resolve("backOfficeUserOnboarding");
    facade.process(signupRequest);
    return ControllerResponse.buildCreateSuccessResponse("Onboarding Successful");
  }
}
