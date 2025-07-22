/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.controller;

import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserListResponse;
import com.digicore.omnexa.backoffice.modules.user.facade.BackOfficeUserFacade;
import com.digicore.omnexa.common.lib.api.ApiRequestWrapper;
import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BackOfficeUserController.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BackOfficeUserController Tests")
class BackOfficeUserControllerTest {

    @Mock
    private BackOfficeUserFacade backOfficeUserFacade;

    @InjectMocks
    private BackOfficeUserController backOfficeUserController;

    private ApiRequestWrapper<InviteUserRequest> inviteRequestWrapper;
    private ApiRequestWrapper<SignupRequest> signupRequestWrapper;
    private InviteUserResponse inviteUserResponse;
    private SignupResponse signupResponse;
    private UserListResponse userListResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        InviteUserRequest inviteUserRequest = new InviteUserRequest();
        inviteUserRequest.setEmail("test@example.com");
        inviteUserRequest.setFirstName("Test");

        inviteRequestWrapper = new ApiRequestWrapper<>();
        inviteRequestWrapper.setRequestId("req_12345");
        inviteRequestWrapper.setTimestamp(ZonedDateTime.now());
        inviteRequestWrapper.setData(inviteUserRequest);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("SecureP@ss123");
        signupRequest.setConfirmPassword("SecureP@ss123");

        signupRequestWrapper = new ApiRequestWrapper<>();
        signupRequestWrapper.setRequestId("req_67890");
        signupRequestWrapper.setTimestamp(ZonedDateTime.now());
        signupRequestWrapper.setData(signupRequest);

        inviteUserResponse = InviteUserResponse.builder()
                .email("test@example.com")
                .userId("AH12345678")
                .message("Invitation sent successfully")
                .build();

        signupResponse = SignupResponse.builder()
                .userId("AH12345678")
                .email("test@example.com")
                .fullName("John Doe")
                .status(BackOfficeUserStatus.INACTIVE)
                .message("Signup completed successfully")
                .build();

        userListResponse = UserListResponse.builder()
                .content(Collections.emptyList())
                .currentPage(1)
                .totalItems(0)
                .totalPages(0)
                .isFirstPage(true)
                .isLastPage(true)
                .build();
    }

    @Test
    @DisplayName("Should successfully invite user")
    void shouldSuccessfullyInviteUser() {
        // Given
        when(backOfficeUserFacade.inviteUser(any(InviteUserRequest.class))).thenReturn(inviteUserResponse);

        // When
        ResponseEntity<ApiResponseJson<InviteUserResponse>> response =
                backOfficeUserController.inviteUser(inviteRequestWrapper);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("User invited successfully");
        assertThat(response.getBody().getRequestId()).isEqualTo(inviteRequestWrapper.getRequestId());
        assertThat(response.getBody().getData()).isEqualTo(inviteUserResponse);

        verify(backOfficeUserFacade).inviteUser(inviteRequestWrapper.getData());
    }

    @Test
    @DisplayName("Should successfully complete signup")
    void shouldSuccessfullyCompleteSignup() {
        // Given
        when(backOfficeUserFacade.signup(any(SignupRequest.class))).thenReturn(signupResponse);

        // When
        ResponseEntity<ApiResponseJson<SignupResponse>> response =
                backOfficeUserController.signup(signupRequestWrapper);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Signup completed successfully");
        assertThat(response.getBody().getRequestId()).isEqualTo(signupRequestWrapper.getRequestId());
        assertThat(response.getBody().getData()).isEqualTo(signupResponse);

        verify(backOfficeUserFacade).signup(signupRequestWrapper.getData());
    }

    @Test
    @DisplayName("Should successfully get user list")
    void shouldSuccessfullyGetUserList() {
        // Given
        when(backOfficeUserFacade.getUserList(1, 16, "john", "ACTIVE")).thenReturn(userListResponse);

        // When
        ResponseEntity<ApiResponseJson<UserListResponse>> response =
                backOfficeUserController.getUserList(1, 16, "john", "ACTIVE");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Users retrieved successfully");
        assertThat(response.getBody().getData()).isEqualTo(userListResponse);

        verify(backOfficeUserFacade).getUserList(1, 16, "john", "ACTIVE");
    }

    @Test
    @DisplayName("Should handle null parameters in get user list")
    void shouldHandleNullParametersInGetUserList() {
        // Given
        when(backOfficeUserFacade.getUserList(null, null, null, null)).thenReturn(userListResponse);

        // When
        ResponseEntity<ApiResponseJson<UserListResponse>> response =
                backOfficeUserController.getUserList(null, null, null, null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();

        verify(backOfficeUserFacade).getUserList(null, null, null, null);
    }

    @Test
    @DisplayName("Should return health check response")
    void shouldReturnHealthCheckResponse() {
        // When
        ResponseEntity<ApiResponseJson<String>> response = backOfficeUserController.healthCheck();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Back Office User Service is running");
        assertThat(response.getBody().getData()).isEqualTo("OK");
        assertThat(response.getBody().getRequestId()).isEqualTo("health-check");
    }
}