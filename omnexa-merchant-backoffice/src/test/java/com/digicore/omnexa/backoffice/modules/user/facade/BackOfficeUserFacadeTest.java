/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.facade;

import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserListResponse;
import com.digicore.omnexa.backoffice.modules.user.service.BackOfficeUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BackOfficeUserFacade.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BackOfficeUserFacade Tests")
class BackOfficeUserFacadeTest {

    @Mock
    private BackOfficeUserService backOfficeUserService;

    @InjectMocks
    private BackOfficeUserFacade backOfficeUserFacade;

    private InviteUserRequest inviteUserRequest;
    private SignupRequest signupRequest;
    private InviteUserResponse inviteUserResponse;
    private SignupResponse signupResponse;
    private UserListResponse userListResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        inviteUserRequest = new InviteUserRequest();
        inviteUserRequest.setEmail("test@example.com");
        inviteUserRequest.setFirstName("Test");

        signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("SecureP@ss123");
        signupRequest.setConfirmPassword("SecureP@ss123");

        inviteUserResponse = InviteUserResponse.builder()
                .email("test@example.com")
                .userId("AH12345678")
                .message("Invitation sent successfully")
                .build();

        signupResponse = SignupResponse.builder()
                .userId("AH12345678")
                .email("test@example.com")
                .fullName("John Doe")
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
    @DisplayName("Should delegate invite user to service")
    void shouldDelegateInviteUserToService() {
        // Given
        when(backOfficeUserService.inviteUser(inviteUserRequest)).thenReturn(inviteUserResponse);

        // When
        InviteUserResponse result = backOfficeUserFacade.inviteUser(inviteUserRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(inviteUserResponse.getEmail());
        assertThat(result.getUserId()).isEqualTo(inviteUserResponse.getUserId());
        assertThat(result.getMessage()).isEqualTo(inviteUserResponse.getMessage());

        verify(backOfficeUserService).inviteUser(inviteUserRequest);
    }

    @Test
    @DisplayName("Should delegate signup to service")
    void shouldDelegateSignupToService() {
        // Given
        when(backOfficeUserService.signup(signupRequest)).thenReturn(signupResponse);

        // When
        SignupResponse result = backOfficeUserFacade.signup(signupRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(signupResponse.getUserId());
        assertThat(result.getEmail()).isEqualTo(signupResponse.getEmail());
        assertThat(result.getFullName()).isEqualTo(signupResponse.getFullName());
        assertThat(result.getMessage()).isEqualTo(signupResponse.getMessage());

        verify(backOfficeUserService).signup(signupRequest);
    }

    @Test
    @DisplayName("Should delegate get user list to service")
    void shouldDelegateGetUserListToService() {
        // Given
        Integer pageNumber = 1;
        Integer pageSize = 16;
        String search = "john";
        String status = "ACTIVE";

        when(backOfficeUserService.getUserList(pageNumber, pageSize, search, status))
                .thenReturn(userListResponse);

        // When
        UserListResponse result = backOfficeUserFacade.getUserList(pageNumber, pageSize, search, status);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCurrentPage()).isEqualTo(userListResponse.getCurrentPage());
        assertThat(result.getTotalItems()).isEqualTo(userListResponse.getTotalItems());
        assertThat(result.getContent()).isEqualTo(userListResponse.getContent());

        verify(backOfficeUserService).getUserList(pageNumber, pageSize, search, status);
    }

    @Test
    @DisplayName("Should handle null parameters in get user list")
    void shouldHandleNullParametersInGetUserList() {
        // Given
        when(backOfficeUserService.getUserList(null, null, null, null))
                .thenReturn(userListResponse);

        // When
        UserListResponse result = backOfficeUserFacade.getUserList(null, null, null, null);

        // Then
        assertThat(result).isNotNull();
        verify(backOfficeUserService).getUserList(null, null, null, null);
    }
}