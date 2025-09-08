package com.digicore.omni.root.services.modules.merchants.onboarding.controller;


import com.digicore.omni.data.lib.modules.merchant.dto.MerchantBasicInformationDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantRegistrationDTO;
import com.digicore.omni.root.services.modules.merchants.onboarding.services.MerchantOnboardingService;
import com.digicore.omni.root.services.util.Constants;
import com.digicore.otp.request.OtpVerificationRequest;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.enums.LogActivityType;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(Constants.API_V1 + "merchant-signup/process/")
@RequiredArgsConstructor
@Tag(name = "Merchant-Onboarding-Controller",
        description = "Under this controller contains all the endpoints used to verify and create a merchant profile",
        externalDocs = @ExternalDocumentation(description = "New Update, Click on me !!!", url = "https://docs.google.com/document/d/143Pe1kOt-YsEFAZP6z8OIf0p46nONQ_fao2ixnK5mU0"))
public class MerchantOnboardingController {
    private final MerchantOnboardingService merchantOnboardingService;

    @PostMapping("send-mail-verification-code")
    public ResponseEntity<Object> sendVerificationCode(@RequestParam String email, @RequestParam String firstName)  {
        return merchantOnboardingService.sendVerificationCodeToEmail(email,firstName);
    }

    @PostMapping("mail-verification-code")
    public ResponseEntity<Object>  verifyCodeForEmail(@Valid @RequestBody  OtpVerificationRequest otpVerificationRequest)  {
        return merchantOnboardingService.validateEmailVerificationCode(otpVerificationRequest);
    }

    @PostMapping("resend-sms-verification-code")
    public ResponseEntity<Object>  resendVerificationCode(@RequestParam String email, @RequestParam String firstName, @RequestParam String phoneNumber) {
        return merchantOnboardingService.resendVerificationCodeToPhoneNumber(OtpVerificationRequest.builder()
                        .email(email)
                        .firstName(firstName)
                        .phoneNumber(phoneNumber)
                .build());
    }

    @PostMapping("sms-verification-code")
    public ResponseEntity<Object>  verifyCodeForPhoneNumber(@Valid @RequestBody  OtpVerificationRequest otpVerificationRequest) {
        return merchantOnboardingService.validatePhoneNumberVerificationCode(otpVerificationRequest);
    }

    @PostMapping("merchant-information-validation")
    public ResponseEntity<Object>  validateMerchantInformation(@Valid @RequestBody MerchantBasicInformationDTO merchantBasicInformationDTO) {
        return merchantOnboardingService.validateMerchantCreationRequest(merchantBasicInformationDTO);
    }

    @LogActivity(activity = LogActivityType.CREATE_ACTIVITY)
    @PostMapping("profile-activation")
    public ResponseEntity<Object>   addMerchant(@Valid @RequestBody MerchantRegistrationDTO merchantRegistrationDTO)  {
        return merchantOnboardingService.registerMerchant(merchantRegistrationDTO);

    }



}
