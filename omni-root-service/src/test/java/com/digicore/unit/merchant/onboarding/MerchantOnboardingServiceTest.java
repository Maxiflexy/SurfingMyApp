package com.digicore.unit.merchant.onboarding;

import com.digicore.omni.root.services.modules.merchants.onboarding.services.MerchantOnboardingService;
import com.digicore.otp.request.OtpVerificationRequest;
import com.digicore.otp.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Apr-24(Mon)-2023
 */




@ExtendWith(MockitoExtension.class)
@Slf4j
class MerchantOnboardingServiceTest {
    @Mock
    private OtpService otpService;


    @InjectMocks
    private MerchantOnboardingService merchantOnboardingService;

    private OtpVerificationRequest otpVerificationRequest;


    @BeforeEach
    public void setup(){

        otpVerificationRequest = OtpVerificationRequest.builder()
                .firstName("Oluwatobi")
                .phoneNumber("2347087982874")
                .email("tobiogunwuyi@gmail.com")
                .build();
    }


    @Test
     void testSendVerificationCodeToEmail() {

        ResponseEntity<Object> result = merchantOnboardingService.sendVerificationCodeToEmail(otpVerificationRequest.getEmail(),otpVerificationRequest.getFirstName());
        verifyTestResult(result);
    }

    @Test
    void testValidateEmailVerificationCode() {

        ResponseEntity<Object> result = merchantOnboardingService.validateEmailVerificationCode(otpVerificationRequest);
        verifyTestResult(result);
    }



    @Test
    void testResendVerificationCodeToPhoneNumber() {

        ResponseEntity<Object> result = merchantOnboardingService.resendVerificationCodeToPhoneNumber(otpVerificationRequest);
        verifyTestResult(result);
    }


//    @Test
//    void testSendVerificationCodeToEmail() {
//
//        ResponseEntity<Object> result = merchantOnboardingService.sendVerificationCodeToEmail("tobiogunwuyi@gmail.com","oluwatobi");
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        assertTrue(Objects.requireNonNull(result.getBody()).isSuccess());
//    }
//
//    @Test
//    void testSendVerificationCodeToEmail() {
//
//        ResponseEntity<Object> result = merchantOnboardingService.sendVerificationCodeToEmail("tobiogunwuyi@gmail.com","oluwatobi");
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        assertTrue(Objects.requireNonNull(result.getBody()).isSuccess());
//    }
//
//    @Test
//    void testSendVerificationCodeToEmail() {
//
//        ResponseEntity<Object> result = merchantOnboardingService.sendVerificationCodeToEmail("tobiogunwuyi@gmail.com","oluwatobi");
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        assertTrue(Objects.requireNonNull(result.getBody()).isSuccess());
//    }

    private static void verifyTestResult(ResponseEntity<Object> result) {
        //validate http status code
        assertTrue(result.getStatusCode().is2xxSuccessful());

    }
}
