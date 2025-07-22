/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.integration.controller;

import com.digicore.omnexa.backoffice.modules.user.config.TestConfig;
import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import com.digicore.omnexa.backoffice.modules.user.data.repository.BackOfficeUserRepository;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.api.ApiRequestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for BackOfficeUserController with full application context.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
@DisplayName("BackOfficeUserController Integration Tests")
class BackOfficeUserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BackOfficeUserRepository backOfficeUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clean up database before each test
        backOfficeUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully invite a new user")
    void shouldSuccessfullyInviteNewUser() throws Exception {
        // Given
        InviteUserRequest inviteRequest = new InviteUserRequest();
        inviteRequest.setEmail("newuser@example.com");
        inviteRequest.setFirstName("New");

        ApiRequestWrapper<InviteUserRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_12345");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(inviteRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("User invited successfully")))
                .andExpect(jsonPath("$.requestId", is("req_12345")))
                .andExpect(jsonPath("$.data.email", is("newuser@example.com")))
                .andExpected(jsonPath("$.data.userId").exists());
    }

    @Test
    @DisplayName("Should return error when inviting existing user")
    void shouldReturnErrorWhenInvitingExistingUser() throws Exception {
        // Given - create existing user
        BackOfficeUser existingUser = new BackOfficeUser();
        existingUser.setEmail("existing@example.com");
        existingUser.setStatus(BackOfficeUserStatus.INACTIVE);
        existingUser.setCreatedBy("test");
        backOfficeUserRepository.save(existingUser);

        InviteUserRequest inviteRequest = new InviteUserRequest();
        inviteRequest.setEmail("existing@example.com");
        inviteRequest.setFirstName("Existing");

        ApiRequestWrapper<InviteUserRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_12345");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(inviteRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpected(jsonPath("$.success", is(false)))
                .andExpected(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    @DisplayName("Should return validation error for invalid email")
    void shouldReturnValidationErrorForInvalidEmail() throws Exception {
        // Given
        InviteUserRequest inviteRequest = new InviteUserRequest();
        inviteRequest.setEmail("invalid-email");
        inviteRequest.setFirstName("Test");

        ApiRequestWrapper<InviteUserRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_12345");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(inviteRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully complete signup")
    void shouldSuccessfullyCompleteSignup() throws Exception {
        // Given - create invited user
        BackOfficeUser invitedUser = new BackOfficeUser();
        invitedUser.setEmail("invited@example.com");
        invitedUser.setStatus(BackOfficeUserStatus.INACTIVE);
        invitedUser.setCreatedBy("system");
        backOfficeUserRepository.save(invitedUser);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("invited@example.com");
        signupRequest.setPassword("SecureP@ss123");
        signupRequest.setConfirmPassword("SecureP@ss123");

        ApiRequestWrapper<SignupRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_67890");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(signupRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Signup completed successfully")))
                .andExpect(jsonPath("$.requestId", is("req_67890")))
                .andExpect(jsonPath("$.data.email", is("invited@example.com")))
                .andExpect(jsonPath("$.data.fullName", is("John Doe")))
                .andExpected(jsonPath("$.data.userId").exists());
    }

    @Test
    @DisplayName("Should return error when signup user not found")
    void shouldReturnErrorWhenSignupUserNotFound() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("notinvited@example.com");
        signupRequest.setPassword("SecureP@ss123");
        signupRequest.setConfirmPassword("SecureP@ss123");

        ApiRequestWrapper<SignupRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_67890");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(signupRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpected(jsonPath("$.success", is(false)))
                .andExpected(jsonPath("$.message", containsString("not found")));
    }

    @Test
    @DisplayName("Should return validation error for password mismatch")
    void shouldReturnValidationErrorForPasswordMismatch() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("SecureP@ss123");
        signupRequest.setConfirmPassword("DifferentPassword");

        ApiRequestWrapper<SignupRequest> requestWrapper = new ApiRequestWrapper<>();
        requestWrapper.setRequestId("req_67890");
        requestWrapper.setTimestamp(ZonedDateTime.now());
        requestWrapper.setData(signupRequest);

        // When & Then
        mockMvc.perform(post("/v1/backoffice/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWrapper)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpected(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("Should successfully get user list")
    void shouldSuccessfullyGetUserList() throws Exception {
        // Given - create test users
        createTestUser("user1@example.com", "John", "Doe", BackOfficeUserStatus.ACTIVE);
        createTestUser("user2@example.com", "Jane", "Smith", BackOfficeUserStatus.INACTIVE);

        // When & Then
        mockMvc.perform(get("/v1/backoffice/users/list")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Users retrieved successfully")))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpected(jsonPath("$.data.totalItems", is(2)))
                .andExpected(jsonPath("$.data.currentPage", is(1)));
    }

    @Test
    @DisplayName("Should filter users by status")
    void shouldFilterUsersByStatus() throws Exception {
        // Given - create test users
        createTestUser("active@example.com", "Active", "User", BackOfficeUserStatus.ACTIVE);
        createTestUser("inactive@example.com", "Inactive", "User", BackOfficeUserStatus.INACTIVE);

        // When & Then
        mockMvc.perform(get("/v1/backoffice/users/list")
                        .param("status", "ACTIVE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpected(jsonPath("$.data.totalItems", is(1)))
                .andExpect(jsonPath("$.data.content[0].email", is("active@example.com")));
    }

    @Test
    @DisplayName("Should search users by search term")
    void shouldSearchUsersBySearchTerm() throws Exception {
        // Given - create test users
        createTestUser("john.doe@example.com", "John", "Doe", BackOfficeUserStatus.ACTIVE);
        createTestUser("jane.smith@example.com", "Jane", "Smith", BackOfficeUserStatus.ACTIVE);

        // When & Then
        mockMvc.perform(get("/v1/backoffice/users/list")
                        .param("search", "john"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpected(jsonPath("$.data.totalItems", is(1)))
                .andExpect(jsonPath("$.data.content[0].email", is("john.doe@example.com")));
    }

    @Test
    @DisplayName("Should return health check response")
    void shouldReturnHealthCheckResponse() throws Exception {
        // When & Then
        mockMvc.perform(get("/v1/backoffice/users"))
                .andDo(print())
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success", is(true)))
                .andExpected(jsonPath("$.message", is("Back Office User Service is running")))
                .andExpected(jsonPath("$.data", is("OK")))
                .andExpected(jsonPath("$.requestId", is("health-check")));
    }

    private void createTestUser(String email, String firstName, String lastName, BackOfficeUserStatus status) {
        BackOfficeUser user = new BackOfficeUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setStatus(status);
        user.setCreatedBy("test");
        backOfficeUserRepository.save(user);
    }
}