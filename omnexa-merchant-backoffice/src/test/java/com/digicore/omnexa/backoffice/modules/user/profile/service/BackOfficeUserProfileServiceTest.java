/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.service;

import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.backoffice.modules.user.profile.data.repository.BackOfficeUserRepository;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.request.BackOfficeProfileEditRequest;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeProfileEditResponse;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO;
import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.response.UserInviteResponse;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BackOfficeUserProfileService class.
 *
 * <p>These tests validate the behavior of the service methods, ensuring that
 * the correct repository methods are invoked and the expected responses or exceptions
 * are returned.
 *
 * <p>Author: Onyekachi Ejemba
 * Created On: Jul-22(Tue)-2025
 */
@ExtendWith(MockitoExtension.class)
class BackOfficeUserProfileServiceTest {

    @Mock
    private BackOfficeUserRepository backOfficeUserRepository; // Mock for the user repository

    @Mock
    private MessagePropertyConfig messagePropertyConfig; // Mock for message property configuration

    @InjectMocks
    private BackOfficeUserProfileService backOfficeUserProfileService; // Service under test

    /**
     * Tests the createProfile method to ensure it creates a user profile successfully
     * when a valid UserInviteRequest is provided.
     */
    @Test
    void createProfile_createsUserProfileSuccessfully_whenValidUserInviteRequest() {
        UserInviteRequest request = new UserInviteRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setRole("Admin");

        Mockito.when(backOfficeUserRepository.existsByEmail(request.getEmail())).thenReturn(false);

        OnboardingResponse response = backOfficeUserProfileService.createProfile(request);

        assertInstanceOf(UserInviteResponse.class, response);
        Mockito.verify(backOfficeUserRepository).save(Mockito.any(BackOfficeUserProfile.class));
    }

    /**
     * Tests the createProfile method to ensure it throws an OmnexaException
     * when the email already exists in the repository.
     */
    @Test
    void createProfile_throwsOmnexaException_whenEmailAlreadyExists() {
        UserInviteRequest request = new UserInviteRequest();
        request.setEmail("test@example.com");

        Mockito.when(backOfficeUserRepository.existsByEmail(request.getEmail())).thenReturn(true);
        Mockito.when(messagePropertyConfig.getOnboardMessage(DUPLICATE)).thenReturn("Duplicate email");

        OmnexaException exception = assertThrows(
            OmnexaException.class,
            () -> backOfficeUserProfileService.createProfile(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Duplicate email"));
    }

    /**
     * Tests the updateProfile method to ensure it updates a user profile successfully
     * when a valid BackOfficeProfileEditRequest is provided.
     */
    @Test
    void updateProfile_updatesUserProfileSuccessfully_whenValidRequest() {
        BackOfficeProfileEditRequest request = new BackOfficeProfileEditRequest();
        request.setProfileId("123");
        request.setEmail("updated@example.com");
        request.setFirstName("UpdatedName");

        Mockito.when(backOfficeUserRepository.existsByProfileId(request.getProfileId())).thenReturn(true);

        ProfileEditResponse response = backOfficeUserProfileService.updateProfile(request);

        assertTrue(response instanceof BackOfficeProfileEditResponse);
        Mockito.verify(backOfficeUserRepository).updateProfile(
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getRole(),
            request.getProfileId()
        );
    }

    /**
     * Tests the updateProfile method to ensure it throws an OmnexaException
     * when the profile ID does not exist in the repository.
     */
    @Test
    void updateProfile_throwsOmnexaException_whenProfileIdDoesNotExist() {
        BackOfficeProfileEditRequest request = new BackOfficeProfileEditRequest();
        request.setProfileId("123");

        Mockito.when(backOfficeUserRepository.existsByProfileId(request.getProfileId())).thenReturn(false);
        Mockito.when(messagePropertyConfig.getOnboardMessage(NOT_FOUND)).thenReturn("Profile not found");

        OmnexaException exception = assertThrows(
            OmnexaException.class,
            () -> backOfficeUserProfileService.updateProfile(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Profile not found"));
    }

    /**
     * Tests the getProfileByEmail method to ensure it returns a ProfileInfoResponse
     * when a profile with the given email exists.
     */
    @Test
    void getProfileByEmail_returnsProfileInfoResponse_whenProfileExists() {
        String email = "test@example.com";
        BackOfficeUserProfileDTO userProfileDTO = new BackOfficeUserProfileDTO();
        userProfileDTO.setProfileVerificationStatus(ProfileVerificationStatus.PENDING_INVITE_ACCEPTANCE);

        Mockito.when(backOfficeUserRepository.findProfileStatusesByEmail(email)).thenReturn(Optional.of(userProfileDTO));

        ProfileInfoResponse response = backOfficeUserProfileService.getProfileByEmail(email);

        assertEquals(userProfileDTO, response);
    }

    /**
     * Tests the getProfileByEmail method to ensure it throws an OmnexaException
     * when no profile with the given email exists in the repository.
     */
    @Test
    void getProfileByEmail_throwsOmnexaException_whenProfileDoesNotExist() {
        String email = "nonexistent@example.com";

        Mockito.when(backOfficeUserRepository.findProfileStatusesByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(messagePropertyConfig.getOnboardMessage(NOT_FOUND)).thenReturn("Profile not found");

        OmnexaException exception = assertThrows(
            OmnexaException.class,
            () -> backOfficeUserProfileService.getProfileByEmail(email)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertTrue(exception.getMessage().contains("Profile not found"));
    }
}