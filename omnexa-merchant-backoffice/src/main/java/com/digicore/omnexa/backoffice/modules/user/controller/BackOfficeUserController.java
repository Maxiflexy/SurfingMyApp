/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.controller;

import com.digicore.common.lib.api.ApiRequestWrapper;
import com.digicore.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserListResponse;
import com.digicore.omnexa.backoffice.modules.user.facade.BackOfficeUserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

import static com.digicore.common.lib.api.ApiVersion.API_V1;

/**
 * Controller for back office user management operations.
 *
 * <p>Provides endpoints for user invitation and signup processes.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@RestController
@RequestMapping(API_V1 + "backoffice/users")
@RequiredArgsConstructor
@Tag(name = "BackOffice-User-Module", description = "APIs for back office user management")
public class BackOfficeUserController {

    private final BackOfficeUserFacade backOfficeUserFacade;

    /**
     * Invites a new user to the back office system.
     *
     * @param requestWrapper invitation request containing email and first name
     * @return response with invitation details
     */
    @PostMapping("/invite")
    @Operation(
            summary = "Invite a new back office user",
            description = "Sends an invitation email to a new user to join the back office system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User invited successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseJson.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or user already exists"
            )
    })
    public ResponseEntity<ApiResponseJson<InviteUserResponse>> inviteUser(
            @Valid @RequestBody ApiRequestWrapper<InviteUserRequest> requestWrapper) {

        InviteUserResponse response = backOfficeUserFacade.inviteUser(requestWrapper.getData());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseJson.<InviteUserResponse>builder()
                        .success(true)
                        .message("User invited successfully")
                        .requestId(requestWrapper.getRequestId())
                        .timestamp(ZonedDateTime.now())
                        .data(response)
                        .build());
    }

    /**
     * Completes user signup after invitation.
     *
     * @param requestWrapper signup request with user details
     * @return response with created user information
     */
    @PostMapping("/signup")
    @Operation(
            summary = "Complete back office user signup",
            description = "Allows an invited user to complete their registration by providing additional details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User signup completed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseJson.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User invitation not found"
            )
    })
    public ResponseEntity<ApiResponseJson<SignupResponse>> signup(
            @Valid @RequestBody ApiRequestWrapper<SignupRequest> requestWrapper) {

        SignupResponse response = backOfficeUserFacade.signup(requestWrapper.getData());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseJson.<SignupResponse>builder()
                        .success(true)
                        .message("Signup completed successfully")
                        .requestId(requestWrapper.getRequestId())
                        .timestamp(ZonedDateTime.now())
                        .data(response)
                        .build());
    }


    /**
     * Retrieves paginated list of back office users with search and filter capabilities.
     *
     * @param pageNumber page number (1-based, optional, default: 1)
     * @param pageSize page size (max 16, optional, default: 16)
     * @param search search term to filter users by name or email (optional)
     * @param status filter by user status (optional)
     * @return paginated list of users
     */
    @GetMapping("/list")
    @Operation(
            summary = "Get paginated list of back office users with search and filter",
            description = "Retrieves a paginated list of back office users with optional search by name/email and filter by status, sorted by creation date (newest first)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseJson.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters"
            )
    })
    public ResponseEntity<ApiResponseJson<UserListResponse>> getUserList(
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(required = false) Integer pageNumber,
            @Parameter(description = "Page size (max 16)", example = "16")
            @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "Search term to filter users by name or email", example = "john.doe")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by user status (ACTIVE, INACTIVE, PENDING, etc.)", example = "ACTIVE")
            @RequestParam(required = false) String status) {

        UserListResponse response = backOfficeUserFacade.getUserList(pageNumber, pageSize, search, status);

        return ResponseEntity.ok(ApiResponseJson.<UserListResponse>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(response)
                .build());
    }

    @GetMapping()
    public ResponseEntity<ApiResponseJson<String>> healthCheck() {
        ApiResponseJson<String> response = ApiResponseJson.<String>builder()
                .success(true)
                .message("Back Office User Service is running")
                .requestId("health-check")
                .timestamp(ZonedDateTime.now())
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}