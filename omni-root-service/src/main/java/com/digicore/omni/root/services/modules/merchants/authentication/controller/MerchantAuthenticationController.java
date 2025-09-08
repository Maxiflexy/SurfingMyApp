package com.digicore.omni.root.services.modules.merchants.authentication.controller;


import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.dtos.PasswordResetValidationDTO;
import com.digicore.omni.data.lib.modules.common.dtos.RecoverPasswordDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ResetDefaultPasswordDTO;

import com.digicore.omni.root.services.modules.common.authentication.TokenRefresher;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginWith2faRequest;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.Resend2faOTPRequest;
import com.digicore.omni.root.services.modules.merchants.authentication.service.LoginAuthentication;
import com.digicore.omni.root.services.modules.merchants.authentication.utils.MerchantAuthenticationAssemblerModel;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/merchant-authentication/process/")
public class MerchantAuthenticationController {

    private final LoginAuthentication loginAuthentication;

    private final MerchantAuthenticationAssemblerModel assemblerModel;

    private final TokenRefresher tokenRefresher;


    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody SignIn signIn) {
        return  loginAuthentication.authenticateMerchant(signIn);
    }

    @PostMapping("authenticate-2fa-otp")
    public ResponseEntity<Object> authenticate2faOTP(@Valid  @RequestBody LoginWith2faRequest loginWith2faRequest) {
        return loginAuthentication.authenticateMerchant(loginWith2faRequest);
    }

    @PostMapping("2fa-resend-otp")
    public ResponseEntity<Object> resend2faOTP(@Valid @RequestBody Resend2faOTPRequest resend2faOTPRequest){

        return loginAuthentication.resend2faOTP(resend2faOTPRequest);

    }



    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("initiate-account-recovery")
    public ResponseEntity<Object> forgotPassword(@RequestParam String  username) {
        return loginAuthentication.sendOTPForForgotPassword(username);
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("password-reset")
    public ResponseEntity<Object> passwordRest(@RequestBody RecoverPasswordDTO recoverPasswordDTO) {
        return loginAuthentication.resetAccountPassword(recoverPasswordDTO);
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("password-reset-otp-validation")
    public ResponseEntity<Object> validateOTP(@Valid @RequestBody PasswordResetValidationDTO recoverPasswordDTO) {
      return  loginAuthentication.validateOTPForPasswordReset(recoverPasswordDTO);
    }


    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PostMapping("reset-default-password")
    public ResponseEntity<Object> resetDefaultPassword(Principal principal, @Valid @RequestBody ResetDefaultPasswordDTO recoverPasswordDTO) {
       return loginAuthentication.resetDefaultPassword(principal,recoverPasswordDTO);
    }

}
