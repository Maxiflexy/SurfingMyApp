package com.digicore.backoffice.service;

//import com.digicoreltd.api.helper.services.ZeusService;


import com.digicore.omni.data.lib.modules.backoffice.repository.BackOfficeProfileRepository;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;
import com.digicore.omni.root.services.modules.backoffice.onboarding.service.OnBoardingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BackOfficeUserServiceTest {

    @Mock
    private BackOfficeProfileRepository backOfficeProfileRepository;

    @Mock
    private BackOfficeService backOfficeService;

    // underTest
    private OnBoardingService underTest;

    @BeforeEach
    void setUp(){
        underTest = new OnBoardingService(backOfficeService);
    }

    // @Test
//    void signUp_with_invalid_input_throws_ZestException(){
//        // given
//        BackOfficeUserDTO userRegistrationDTO = null;
//
//        // when
//
//        // then
//        assertThrows(UserException.class, ()->underTest.processBackOfficeUserSignUp(userRegistrationDTO));
//        verify(backOfficeProfileRepository, never()).save(any());
//    }

    // @Test
//    void signUp_with_required_details_is_successful() throws UserException, EmailTakenException {
//        // given
//        BackOfficeUserDTO userRegistrationDTO = new BackOfficeUserDTO("m@m.com", "password", "mon", "ok");
//
//        // when
//        when(backOfficeProfileRepository.save(any())).thenReturn(new BackOfficeProfile());
//
//        // then
//        assertInstanceOf(BackOfficeUserApiModel.class, underTest.processBackOfficeUserSignUp(userRegistrationDTO));
//    }

}