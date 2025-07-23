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
import io.swagger.v3.oas.annotations.Parameter;

/**
 * Controller for back office user management operations.
 *
 * <p>Provides endpoints for user invitation, retrieval, signup processes,
 * and fetching paginated lists of users.
 *
 * <p>Each endpoint is documented with its purpose, input parameters, and response details.
 * This controller uses Spring Boot annotations for request mapping and validation.
 * It also integrates with a facade resolver and user profile service for business logic.
 *
 * <p>Author: Onyekachi Ejemba
 * Created On: Jul-08(Tue)-2025
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
   * @param userInviteRequest the request payload containing user invitation details
   * @return a response entity with a success message and HTTP status 201
   */
  @PostMapping()
  //@PreAuthorize("hasAuthority('invite-backoffice-user')")
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

  /**
   * Retrieves the profile of a back office user by email.
   *
   * @param email the email address of the user to retrieve
   * @return a response entity containing the user profile or null if not found
   */
  @GetMapping()
  @Operation(
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
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
      summary = ONBOARDING_CONTROLLER_ONBOARD_TITLE,
      description = ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION)
  public ResponseEntity<Object> completeSignUp(@Valid @RequestBody SignupRequest signupRequest) {
    Facade<UserInviteRequest, Void> facade = facadeResolver.resolve("backOfficeUserOnboarding");
    facade.process(signupRequest);
    return ControllerResponse.buildCreateSuccessResponse("Onboarding Successful");
  }


  /**
   * Retrieves a paginated list of back office users with optional search and filter parameters.
   *
   * @param pageNumber the page number (1-based) for pagination
   * @param pageSize the number of users per page (maximum 16)
   * @param search an optional search term to filter users by name or email
   * @param profileStatus an optional filter for user profile status (e.g., ACTIVE, INACTIVE)
   * @return a response entity containing the paginated list of users
   */
  @GetMapping("/users")
  @Operation(
          summary = "Get paginated list of backoffice users with search and filter",
          description = "Retrieves a paginated list of all backoffice users with optional search by name/email and filter by profile status")
  public ResponseEntity<Object> getAllUsers(
          @Parameter(description = "Page number (1-based)", example = "1")
          @RequestParam(required = false) Integer pageNumber,
          @Parameter(description = "Page size (max 16)", example = "16")
          @RequestParam(required = false) Integer pageSize,
          @Parameter(description = "Search term to filter users by firstName, lastName, or email", example = "john")
          @RequestParam(required = false) String search,
          @Parameter(description = "Filter by profile status (ACTIVE, INACTIVE, LOCKED, DEACTIVATED)", example = "ACTIVE")
          @RequestParam(required = false) String profileStatus) {

    return ControllerResponse.buildCreateSuccessResponse(
            backOfficeUserProfileService.getAllUsersPaginated(pageNumber, pageSize, search, profileStatus));
  }
}
