//package com.digicore.omnexa.backoffice.modules.user.profile.service;
//
//
///**
// * @author Onyekachi Ejemba
// * @createdOn Jul-22(Tue)-2025
// */
//@ExtendWith(MockitoExtension.class)
//class BackOfficeUserProfileServiceTest {
//
//    @Mock
//    private BackOfficeUserRepository backOfficeUserRepository;
//
//    @Mock
//    private MessagePropertyConfig messagePropertyConfig;
//
//    @InjectMocks
//    private BackOfficeUserProfileService backOfficeUserProfileService;
//
//    @Test
//    void createProfile_createsUserProfileSuccessfully_whenValidUserInviteRequest() {
//        UserInviteRequest request = new UserInviteRequest();
//        request.setEmail("test@example.com");
//        request.setFirstName("John");
//        request.setRole("Admin");
//
//        Mockito.when(backOfficeUserRepository.existsByEmail(request.getEmail())).thenReturn(false);
//
//        OnboardingResponse response = backOfficeUserProfileService.createProfile(request);
//
//        assertTrue(response instanceof UserInviteResponse);
//        Mockito.verify(backOfficeUserRepository).save(Mockito.any(BackOfficeUserProfile.class));
//    }
//
//    @Test
//    void createProfile_throwsOmnexaException_whenEmailAlreadyExists() {
//        UserInviteRequest request = new UserInviteRequest();
//        request.setEmail("test@example.com");
//
//        Mockito.when(backOfficeUserRepository.existsByEmail(request.getEmail())).thenReturn(true);
//        Mockito.when(messagePropertyConfig.getOnboardMessage(DUPLICATE)).thenReturn("Duplicate email");
//
//        OmnexaException exception = assertThrows(
//            OmnexaException.class,
//            () -> backOfficeUserProfileService.createProfile(request)
//        );
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
//        assertTrue(exception.getMessage().contains("Duplicate email"));
//    }
//
//    @Test
//    void updateProfile_updatesUserProfileSuccessfully_whenValidRequest() {
//        BackOfficeProfileEditRequest request = new BackOfficeProfileEditRequest();
//        request.setProfileId("123");
//        request.setEmail("updated@example.com");
//        request.setFirstName("UpdatedName");
//
//        Mockito.when(backOfficeUserRepository.existsByProfileId(request.getProfileId())).thenReturn(true);
//
//        ProfileEditResponse response = backOfficeUserProfileService.updateProfile(request);
//
//        assertTrue(response instanceof BackOfficeProfileEditResponse);
//        Mockito.verify(backOfficeUserRepository).updateProfile(
//            request.getEmail(),
//            request.getFirstName(),
//            request.getLastName(),
//            request.getRole(),
//            request.getProfileId()
//        );
//    }
//
//    @Test
//    void updateProfile_throwsOmnexaException_whenProfileIdDoesNotExist() {
//        BackOfficeProfileEditRequest request = new BackOfficeProfileEditRequest();
//        request.setProfileId("123");
//
//        Mockito.when(backOfficeUserRepository.existsByProfileId(request.getProfileId())).thenReturn(false);
//        Mockito.when(messagePropertyConfig.getOnboardMessage(NOT_FOUND)).thenReturn("Profile not found");
//
//        OmnexaException exception = assertThrows(
//            OmnexaException.class,
//            () -> backOfficeUserProfileService.updateProfile(request)
//        );
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//        assertTrue(exception.getMessage().contains("Profile not found"));
//    }
//
//    @Test
//    void getProfileByEmail_returnsProfileInfoResponse_whenProfileExists() {
//        String email = "test@example.com";
//        BackOfficeUserProfileDTO userProfileDTO = new BackOfficeUserProfileDTO();
//        userProfileDTO.setProfileVerificationStatus(ProfileVerificationStatus.PENDING_INVITE_ACCEPTANCE);
//
//        Mockito.when(backOfficeUserRepository.findProfileStatusesByEmail(email)).thenReturn(Optional.of(userProfileDTO));
//
//        ProfileInfoResponse response = backOfficeUserProfileService.getProfileByEmail(email);
//
//        assertEquals(userProfileDTO, response);
//    }
//
//    @Test
//    void getProfileByEmail_throwsOmnexaException_whenProfileDoesNotExist() {
//        String email = "nonexistent@example.com";
//
//        Mockito.when(backOfficeUserRepository.findProfileStatusesByEmail(email)).thenReturn(Optional.empty());
//        Mockito.when(messagePropertyConfig.getOnboardMessage(NOT_FOUND)).thenReturn("Profile not found");
//
//        OmnexaException exception = assertThrows(
//            OmnexaException.class,
//            () -> backOfficeUserProfileService.getProfileByEmail(email)
//        );
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
//        assertTrue(exception.getMessage().contains("Profile not found"));
//    }
//}