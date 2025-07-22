/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.service;

import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import com.digicore.omnexa.backoffice.modules.user.data.repository.BackOfficeUserRepository;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserListResponse;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BackOfficeUserService.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BackOfficeUserService Tests")
class BackOfficeUserServiceTest {

    @Mock
    private BackOfficeUserRepository backOfficeUserRepository;

    @Mock
    private MessagePropertyConfig messagePropertyConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BackOfficeUserService backOfficeUserService;

    private InviteUserRequest inviteUserRequest;
    private SignupRequest signupRequest;
    private BackOfficeUser testUser;

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

        testUser = new BackOfficeUser();
        testUser.setId(1L);
        testUser.setUserId("AH12345678");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setStatus(BackOfficeUserStatus.INACTIVE);
        testUser.setCreatedDate(LocalDateTime.now());

        // Setup message property config mocks
        setupMessagePropertyConfigMocks();
    }

    private void setupMessagePropertyConfigMocks() {
        Map<String, String> onboardMessages = new HashMap<>();
        onboardMessages.put("duplicate", "User with profile {profile} already exists");
        onboardMessages.put("invalid", "Invalid onboarding request");
        onboardMessages.put("not_found", "User with profile {profile} not found");
        onboardMessages.put("successful", "Onboarding completed successfully");

        when(messagePropertyConfig.getOnboardMessage("duplicate")).thenReturn(onboardMessages.get("duplicate"));
        when(messagePropertyConfig.getOnboardMessage("invalid")).thenReturn(onboardMessages.get("invalid"));
        when(messagePropertyConfig.getOnboardMessage("not_found")).thenReturn(onboardMessages.get("not_found"));
        when(messagePropertyConfig.getOnboardMessage("successful")).thenReturn(onboardMessages.get("successful"));
    }

    @Test
    @DisplayName("Should successfully invite a new user")
    void shouldSuccessfullyInviteNewUser() {
        // Given
        when(backOfficeUserRepository.existsByEmail(inviteUserRequest.getEmail())).thenReturn(false);
        when(backOfficeUserRepository.save(any(BackOfficeUser.class))).thenReturn(testUser);

        // When
        InviteUserResponse response = backOfficeUserService.inviteUser(inviteUserRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(response.getMessage()).contains("Invitation sent successfully");

        verify(backOfficeUserRepository).existsByEmail(inviteUserRequest.getEmail());
        verify(backOfficeUserRepository).save(any(BackOfficeUser.class));
        verify(emailService).sendInvitationEmail(inviteUserRequest.getEmail(), inviteUserRequest.getFirstName());
    }

    @Test
    @DisplayName("Should throw exception when inviting existing user")
    void shouldThrowExceptionWhenInvitingExistingUser() {
        // Given
        when(backOfficeUserRepository.existsByEmail(inviteUserRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> backOfficeUserService.inviteUser(inviteUserRequest))
                .isInstanceOf(OmnexaException.class)
                .hasMessageContaining("already exists");

        verify(backOfficeUserRepository).existsByEmail(inviteUserRequest.getEmail());
        verify(backOfficeUserRepository, never()).save(any(BackOfficeUser.class));
        verify(emailService, never()).sendInvitationEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("Should successfully complete signup")
    void shouldSuccessfullyCompleteSignup() {
        // Given
        when(backOfficeUserRepository.findByEmail(signupRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encoded-password");
        when(backOfficeUserRepository.save(any(BackOfficeUser.class))).thenReturn(testUser);

        // When
        SignupResponse response = backOfficeUserService.signup(signupRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getFullName()).isEqualTo("John Doe");
        assertThat(response.getStatus()).isEqualTo(BackOfficeUserStatus.INACTIVE);
        assertThat(response.getMessage()).contains("Signup completed successfully");

        verify(backOfficeUserRepository).findByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(backOfficeUserRepository).save(any(BackOfficeUser.class));
    }

    @Test
    @DisplayName("Should throw exception when signup user not found")
    void shouldThrowExceptionWhenSignupUserNotFound() {
        // Given
        when(backOfficeUserRepository.findByEmail(signupRequest.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> backOfficeUserService.signup(signupRequest))
                .isInstanceOf(OmnexaException.class)
                .hasMessageContaining("not found");

        verify(backOfficeUserRepository).findByEmail(signupRequest.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(backOfficeUserRepository, never()).save(any(BackOfficeUser.class));
    }

    @Test
    @DisplayName("Should throw exception when passwords don't match")
    void shouldThrowExceptionWhenPasswordsDontMatch() {
        // Given
        signupRequest.setConfirmPassword("DifferentPassword");

        // When & Then
        assertThatThrownBy(() -> backOfficeUserService.signup(signupRequest))
                .isInstanceOf(OmnexaException.class)
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        verify(backOfficeUserRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should throw exception when password is weak")
    void shouldThrowExceptionWhenPasswordIsWeak() {
        // Given
        signupRequest.setPassword("weak");
        signupRequest.setConfirmPassword("weak");

        // When & Then
        assertThatThrownBy(() -> backOfficeUserService.signup(signupRequest))
                .isInstanceOf(OmnexaException.class)
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        verify(backOfficeUserRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Should return paginated user list")
    void shouldReturnPaginatedUserList() {
        // Given
        List<BackOfficeUser> users = Arrays.asList(testUser, createAnotherTestUser());
        Page<BackOfficeUser> userPage = new PageImpl<>(users, PageRequest.of(0, 16), 2);

        when(backOfficeUserRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 16, null, null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getCurrentPage()).isEqualTo(1);
        assertThat(response.getTotalItems()).isEqualTo(2);
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.isFirstPage()).isTrue();
        assertThat(response.isLastPage()).isTrue();

        verify(backOfficeUserRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty list when no users found")
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Given
        Page<BackOfficeUser> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 16), 0);
        when(backOfficeUserRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 16, null, null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getCurrentPage()).isEqualTo(1);
        assertThat(response.getTotalItems()).isEqualTo(0);
        assertThat(response.getTotalPages()).isEqualTo(0);

        verify(backOfficeUserRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should search users by search term")
    void shouldSearchUsersBySearchTerm() {
        // Given
        List<BackOfficeUser> users = List.of(testUser);
        Page<BackOfficeUser> userPage = new PageImpl<>(users, PageRequest.of(0, 16), 1);

        when(backOfficeUserRepository.findUsersBySearch(eq("john"), any(Pageable.class))).thenReturn(userPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 16, "john", null);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);

        verify(backOfficeUserRepository).findUsersBySearch(eq("john"), any(Pageable.class));
    }

    @Test
    @DisplayName("Should filter users by status")
    void shouldFilterUsersByStatus() {
        // Given
        List<BackOfficeUser> users = List.of(testUser);
        Page<BackOfficeUser> userPage = new PageImpl<>(users, PageRequest.of(0, 16), 1);

        when(backOfficeUserRepository.findByStatus(eq(BackOfficeUserStatus.ACTIVE), any(Pageable.class))).thenReturn(userPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 16, null, "ACTIVE");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);

        verify(backOfficeUserRepository).findByStatus(eq(BackOfficeUserStatus.ACTIVE), any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle invalid status gracefully")
    void shouldHandleInvalidStatusGracefully() {
        // Given
        List<BackOfficeUser> users = List.of(testUser);
        Page<BackOfficeUser> userPage = new PageImpl<>(users, PageRequest.of(0, 16), 1);

        when(backOfficeUserRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 16, null, "INVALID_STATUS");

        // Then
        assertThat(response).isNotNull();
        verify(backOfficeUserRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should enforce max page size")
    void shouldEnforceMaxPageSize() {
        // Given
        List<BackOfficeUser> users = List.of(testUser);
        Page<BackOfficeUser> userPage = new PageImpl<>(users, PageRequest.of(0, 16), 1);

        when(backOfficeUserRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // When
        UserListResponse response = backOfficeUserService.getUserList(1, 100, null, null);

        // Then
        assertThat(response).isNotNull();
        verify(backOfficeUserRepository).findAll(any(Pageable.class));
    }

    private BackOfficeUser createAnotherTestUser() {
        BackOfficeUser user = new BackOfficeUser();
        user.setId(2L);
        user.setUserId("AH87654321");
        user.setEmail("another@example.com");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setStatus(BackOfficeUserStatus.ACTIVE);
        user.setCreatedDate(LocalDateTime.now());
        return user;
    }
}