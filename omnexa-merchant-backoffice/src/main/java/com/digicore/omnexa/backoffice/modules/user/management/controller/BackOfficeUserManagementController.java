/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.management.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.onboarding.OnboardingSwaggerDoc.ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION;
import static com.digicore.omnexa.common.lib.swagger.constant.onboarding.OnboardingSwaggerDoc.ONBOARDING_CONTROLLER_ONBOARD_TITLE;
import static com.digicore.omnexa.common.lib.swagger.constant.user.UserSwaggerDoc.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office user management operations.
 *
 * <p>Provides endpoints for retrieving users with different filtering options following SOLID
 * principles. Each endpoint has a specific responsibility: - Get all users with pagination only -
 * Get users filtered by search term - Get users filtered by profile status
 *
 * <p>Author: Onyekachi Ejemba Created On: Jul-29(Tue)-2025
 */
@RestController
@RequestMapping(API_V1 + USER_API)
@RequiredArgsConstructor
@Tag(name = USER_CONTROLLER_TITLE, description = USER_CONTROLLER_DESCRIPTION)
public class BackOfficeUserManagementController {

  private final ProfileService backOfficeUserProfileService;
  private final FacadeResolver facadeResolver;

  /**
   * Invites a new user to the back office system.
   *
   * @param userInviteRequest the request payload containing user invitation details
   * @return a response entity with a success message and HTTP status 201
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

  /**
   * Retrieves a paginated list of all back office users.
   *
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the number of users per page (optional, max 16, default: 16)
   * @return a response entity containing the paginated list of users
   */
  @GetMapping()
  @Operation(
      summary = USER_CONTROLLER_GET_ALL_TITLE,
      description = USER_CONTROLLER_GET_ALL_DESCRIPTION)
  @PreAuthorize("hasAuthority('view-backoffice-users')")
  public ResponseEntity<Object> getAllUsers(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        backOfficeUserProfileService.getAllProfiles(pageNumber, pageSize));
  }

  /// todo: add jpa specification
  /**
   * Retrieves a paginated list of back office users filtered by search term.
   *
   * @param search the search term to filter users by firstName, lastName, or email (required)
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the number of users per page (optional, max 16, default: 16)
   * @return a response entity containing the filtered and paginated list of users
   */
  @GetMapping(SEARCH_API)
  @Operation(summary = SEARCH_TITLE, description = SEARCH_DESCRIPTION)
  @PreAuthorize("hasAuthority('view-backoffice-users')")
  public ResponseEntity<Object> searchUsers(
      @Parameter(
              description = "Search term to filter users by firstName, lastName, or email",
              example = "john",
              required = true)
          @RequestParam
          String search,
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        backOfficeUserProfileService.searchProfilesPaginated(search, pageNumber, pageSize));
  }

  /// todo: add jpa specification
  /**
   * Retrieves a paginated list of back office users filtered by profile status.
   *
   * @param profileStatus the profile status to filter by (required)
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the number of users per page (optional, max 16, default: 16)
   * @return a response entity containing the filtered and paginated list of users
   */
  @GetMapping(FILTER_API)
  @Operation(summary = FILTER_TITLE, description = FILTER_DESCRIPTION)
  @PreAuthorize("hasAuthority('view-backoffice-users')")
  public ResponseEntity<Object> getUsersByStatus(
      @Parameter(
              description = "Filter by profile status (ACTIVE, INACTIVE, LOCKED, DEACTIVATED)",
              example = "ACTIVE",
              required = true)
          @RequestParam
          ProfileStatus profileStatus,
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        backOfficeUserProfileService.getProfilesByStatusPaginated(
            profileStatus, pageNumber, pageSize));
  }
}
