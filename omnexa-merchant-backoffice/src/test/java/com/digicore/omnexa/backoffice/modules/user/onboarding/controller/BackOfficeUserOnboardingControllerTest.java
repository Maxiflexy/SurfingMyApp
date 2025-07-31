/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO;
import com.digicore.omnexa.backoffice.modules.user.profile.service.BackOfficeUserProfileService;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for the BackOfficeUserOnboardingController class.
 *
 * <p>These tests validate the behavior of the controller's endpoints, ensuring that the correct
 * services and facades are invoked and the expected responses are returned.
 *
 * <p>Author: Onyekachi Ejemba Created On: Jul-23(Wed)-2025
 */
class BackOfficeUserOnboardingControllerTest {

  @Mock private FacadeResolver facadeResolver; // Mock for resolving facades

  @Mock
  private BackOfficeUserProfileService
      backOfficeUserProfileService; // Mock for user profile service

  @Mock
  @SuppressWarnings("rawtypes")
  private Facade userInviteFacade; // Mock for user invitation facade

  @Mock
  @SuppressWarnings("rawtypes")
  private Facade userOnboardingFacade; // Mock for user onboarding facade

  @InjectMocks private BackOfficeUserOnboardingController controller; // Controller under test

  /** Initializes mocks before each test. */
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * Tests the inviteBackOfficeUser method to ensure it returns a success response when a valid
   * request is provided.
   */
  @Test
  @SuppressWarnings("unchecked")
  void inviteBackOfficeUser_returnsSuccessResponse_whenRequestIsValid() {
    // Given
    UserInviteRequest request = new UserInviteRequest();
    request.setEmail("test@example.com");
    request.setFirstName("Test");
    request.setRole("Admin");

    when(facadeResolver.resolve("backOfficeUserInvitation")).thenReturn(userInviteFacade);

    // When
    ResponseEntity<Object> response = controller.inviteBackOfficeUser(request);

    // Then
    assertEquals(201, response.getStatusCode().value());
    assertNotNull(response.getBody());
    verify(facadeResolver).resolve("backOfficeUserInvitation");
    verify(userInviteFacade).process(request);
  }

  /**
   * Tests the retrieveBackOfficeUser method to ensure it returns a user profile when the email
   * exists.
   */
  @Test
  void retrieveBackOfficeUser_returnsUserProfile_whenEmailExists() {
    // Given
    String email = "user@example.com";
    BackOfficeUserProfileDTO mockProfile = new BackOfficeUserProfileDTO();

    when(backOfficeUserProfileService.getProfileByEmail(email)).thenReturn(mockProfile);

    // When
    ResponseEntity<Object> response = controller.retrieveBackOfficeUser(email);

    // Then
    assertEquals(201, response.getStatusCode().value());
    assertNotNull(response.getBody());
    verify(backOfficeUserProfileService).getProfileByEmail(email);
  }

  /**
   * Tests the completeSignUp method to ensure it returns a success response when a valid signup
   * request is provided.
   */
  @Test
  @SuppressWarnings("unchecked")
  void completeSignUp_returnsSuccessResponse_whenSignupRequestIsValid() {
    // Given
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("test@example.com");
    signupRequest.setFirstName("Test");
    signupRequest.setLastName("User");
    signupRequest.setPassword("password123");
    signupRequest.setRole("Admin");

    when(facadeResolver.resolve("backOfficeUserOnboarding")).thenReturn(userOnboardingFacade);

    // When
    ResponseEntity<Object> response = controller.completeSignUp(signupRequest);

    // Then
    assertEquals(201, response.getStatusCode().value());
    assertNotNull(response.getBody());
    verify(facadeResolver).resolve("backOfficeUserOnboarding");
    verify(userOnboardingFacade).process(signupRequest);
  }

  //    /**
  //     * Tests the getAllUsers method to ensure it returns paginated users
  //     * when valid parameters are provided.
  //     */
  //    @Test
  //    void getAllUsers_returnsPaginatedUsers_whenParametersAreProvided() {
  //        // Given
  //        PaginatedUserResponse mockResponse = PaginatedUserResponse.builder()
  //                .content(Collections.emptyList())
  //                .currentPage(1)
  //                .totalItems(0)
  //                .totalPages(0)
  //                .isFirstPage(true)
  //                .isLastPage(true)
  //                .build();
  //
  //        when(backOfficeUserProfileService.getAllUsersPaginated(1, 10, "john", "ACTIVE"))
  //                .thenReturn(mockResponse);
  //
  //        // When
  //        ResponseEntity<Object> response = controller.getAllUsers(1, 10, "john", "ACTIVE");
  //
  //        // Then
  //        assertEquals(201, response.getStatusCode().value());
  //        assertNotNull(response.getBody());
  //        verify(backOfficeUserProfileService).getAllUsersPaginated(1, 10, "john", "ACTIVE");
  //    }

  //    /**
  //     * Tests the getAllUsers method to ensure it returns all users
  //     * when no parameters are provided.
  //     */
  //    @Test
  //    void getAllUsers_returnsAllUsers_whenParametersAreNull() {
  //        // Given
  //        PaginatedUserResponse mockResponse = PaginatedUserResponse.builder()
  //                .content(Collections.emptyList())
  //                .currentPage(1)
  //                .totalItems(0)
  //                .totalPages(0)
  //                .isFirstPage(true)
  //                .isLastPage(true)
  //                .build();
  //
  //        when(backOfficeUserProfileService.getAllUsersPaginated(null, null, null, null))
  //                .thenReturn(mockResponse);
  //
  //        // When
  //        ResponseEntity<Object> response = controller.getAllUsers(null, null, null, null);
  //
  //        // Then
  //        assertEquals(201, response.getStatusCode().value());
  //        assertNotNull(response.getBody());
  //        verify(backOfficeUserProfileService).getAllUsersPaginated(null, null, null, null);
  //    }

  //    /**
  //     * Tests the getAllUsers method to ensure it returns filtered users
  //     * when only a search term is provided.
  //     */
  //    @Test
  //    void getAllUsers_returnsFilteredUsers_whenOnlySearchProvided() {
  //        // Given
  //        PaginatedUserResponse mockResponse = PaginatedUserResponse.builder()
  //                .content(Collections.emptyList())
  //                .currentPage(1)
  //                .totalItems(2)
  //                .totalPages(1)
  //                .isFirstPage(true)
  //                .isLastPage(true)
  //                .build();
  //
  //        when(backOfficeUserProfileService.getAllUsersPaginated(null, null, "test", null))
  //                .thenReturn(mockResponse);
  //
  //        // When
  //        ResponseEntity<Object> response = controller.getAllUsers(null, null, "test", null);
  //
  //        // Then
  //        assertEquals(201, response.getStatusCode().value());
  //        assertNotNull(response.getBody());
  //        verify(backOfficeUserProfileService).getAllUsersPaginated(null, null, "test", null);
  //    }

  //    /**
  //     * Tests the getAllUsers method to ensure it returns filtered users
  //     * when only a profile status is provided.
  //     */
  //    @Test
  //    void getAllUsers_returnsFilteredUsers_whenOnlyStatusProvided() {
  //        // Given
  //        PaginatedUserResponse mockResponse = PaginatedUserResponse.builder()
  //                .content(Collections.emptyList())
  //                .currentPage(1)
  //                .totalItems(3)
  //                .totalPages(1)
  //                .isFirstPage(true)
  //                .isLastPage(true)
  //                .build();
  //
  //        when(backOfficeUserProfileService.getAllUsersPaginated(null, null, null, "ACTIVE"))
  //                .thenReturn(mockResponse);
  //
  //        // When
  //        ResponseEntity<Object> response = controller.getAllUsers(null, null, null, "ACTIVE");
  //
  //        // Then
  //        assertEquals(201, response.getStatusCode().value());
  //        assertNotNull(response.getBody());
  //        verify(backOfficeUserProfileService).getAllUsersPaginated(null, null, null, "ACTIVE");
  //    }
}
