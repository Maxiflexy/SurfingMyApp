package com.digicore.omni.root.services.modules.merchants.onboarding.controller;

import com.digicore.omni.data.lib.modules.merchant.dto.MerchantRegistrationDTO;
import com.digicore.omni.root.services.modules.merchants.onboarding.services.MerchantOnboardingService;
import com.digicore.omni.root.services.util.Constants;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.enums.LogActivityType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(Constants.API_V2 + "merchant-signup/process/")
@RequiredArgsConstructor
@Tag(name = "Merchant-Onboarding-Controller-V2",
        description = "Under this controller contains all the endpoints used to verify and create a merchant profile")
public class MerchantOnboardingControllerV2 {
    private final MerchantOnboardingService merchantOnboardingService;

    @PostMapping("mail-verification-code")
    public ResponseEntity<Object>  verifyCodeForEmail(@RequestParam String email, @RequestParam String verificationCode)  {
        return merchantOnboardingService.validateEmailVerificationCode(email,verificationCode);
    }

    @LogActivity(activity = LogActivityType.CREATE_ACTIVITY)
    @PostMapping("profile-activation")
    public ResponseEntity<Object>   addMerchant(@Valid @RequestBody MerchantRegistrationDTO merchantRegistrationDTO)  {
        return merchantOnboardingService.registerMerchantV2(merchantRegistrationDTO);
    }

}
