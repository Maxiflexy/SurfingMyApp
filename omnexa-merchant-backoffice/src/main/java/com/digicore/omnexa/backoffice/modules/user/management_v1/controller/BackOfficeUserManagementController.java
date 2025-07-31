/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.management.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.UserSwaggerDoc.*;

import com.digicore.omnexa.backoffice.modules.user.profile.service.BackOfficeUserProfileService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office user management operations.
 *
 * <p>Provides endpoints for retrieving users with different filtering options following SOLID principles.
 * Each endpoint has a specific responsibility:
 * - Get all users with pagination only
 * - Get users filtered by search term
 * - Get users filtered by profile status
 *
 * <p>Author: Onyekachi Ejemba
 * Created On: Jul-29(Tue)-2025
 */
@RestController
@RequestMapping(API_V1 + USER_API)
@RequiredArgsConstructor
@Tag(name = USER_CONTROLLER_TITLE, description = USER_CONTROLLER_DESCRIPTION)
public class BackOfficeUserManagementController {

    //private final BackOfficeUserManagementFacade userManagementFacade;
    private final BackOfficeUserProfileService backOfficeUserProfileService;

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
    public ResponseEntity<Object> getAllUsers(
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(required = false) Integer pageNumber,
            @Parameter(description = "Page size (max 16)", example = "16")
            @RequestParam(required = false) Integer pageSize) {

        return ControllerResponse.buildCreateSuccessResponse(
                backOfficeUserProfileService.getAllUsersPaginated(pageNumber, pageSize));
    }

    /**
     * Retrieves a paginated list of back office users filtered by search term.
     *
     * @param search the search term to filter users by firstName, lastName, or email (required)
     * @param pageNumber the page number (1-based, optional, default: 1)
     * @param pageSize the number of users per page (optional, max 16, default: 16)
     * @return a response entity containing the filtered and paginated list of users
     */
    @GetMapping("/users/search")
    @Operation(
            summary = USER_CONTROLLER_GET_ALL_TITLE,
            description = USER_CONTROLLER_GET_ALL_BY_SEARCH_FILTER_DESCRIPTION)
    public ResponseEntity<Object> searchUsers(
            @Parameter(description = "Search term to filter users by firstName, lastName, or email", example = "john", required = true)
            @RequestParam String search,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(required = false) Integer pageNumber,
            @Parameter(description = "Page size (max 16)", example = "16")
            @RequestParam(required = false) Integer pageSize) {

        return ControllerResponse.buildCreateSuccessResponse(
                backOfficeUserProfileService.searchUsersPaginated(search, pageNumber, pageSize));
    }

    /**
     * Retrieves a paginated list of back office users filtered by profile status.
     *
     * @param profileStatus the profile status to filter by (required)
     * @param pageNumber the page number (1-based, optional, default: 1)
     * @param pageSize the number of users per page (optional, max 16, default: 16)
     * @return a response entity containing the filtered and paginated list of users
     */
    @GetMapping("/users/status")
    @Operation(
            summary = USER_CONTROLLER_GET_ALL_TITLE,
            description = USER_CONTROLLER_GET_ALL_BY_PROFILE_STATUS_FILTER_DESCRIPTION)
    public ResponseEntity<Object> getUsersByStatus(
            @Parameter(description = "Filter by profile status (ACTIVE, INACTIVE, LOCKED, DEACTIVATED)", example = "ACTIVE", required = true)
            @RequestParam String profileStatus,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(required = false) Integer pageNumber,
            @Parameter(description = "Page size (max 16)", example = "16")
            @RequestParam(required = false) Integer pageSize) {

        return ControllerResponse.buildCreateSuccessResponse(
                backOfficeUserProfileService.getUsersByStatusPaginated(profileStatus, pageNumber, pageSize));
    }
}