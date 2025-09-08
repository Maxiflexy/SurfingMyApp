/*
 * Created by Monsuru (7/8/2022)
 */

package com.digicore.omni.root.services.modules.backoffice.authentication.controller;

import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.dtos.PasswordResetValidationDTO;
import com.digicore.omni.data.lib.modules.common.dtos.RecoverPasswordDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ResetDefaultPasswordDTO;

import com.digicore.omni.root.services.modules.backoffice.authentication.service.BackofficeLoginService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
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
@RequestMapping("/api/v1/backoffice-authentication/process/")
public class BackOfficeAuthenticationController {

    private final BackofficeLoginService backofficeLoginService;


    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody SignIn signIn){
        return backofficeLoginService.authenticateBackOfficeUser(signIn);
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("initiate-account-recovery")
    public ResponseEntity<Object> initiateAccountRecovery(@RequestParam String email) {
             backofficeLoginService.sendOTPForForgotPassword(email);
             return CommonUtils.buildSuccessResponse(null,null);
    }
    @PostMapping("password-reset-otp-validation")
    public ResponseEntity<Object> validateOTP(@Valid @RequestBody PasswordResetValidationDTO recoverPasswordDTO) {
        return  backofficeLoginService.validateOTPForPasswordReset(recoverPasswordDTO);
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("password-reset")
    public ResponseEntity<Object> passwordReset(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO){
        backofficeLoginService.resetAccountPassword(recoverPasswordDTO);
        return CommonUtils.buildSuccessResponse(null,null);
    }


    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PostMapping("reset-default-password")
    public ResponseEntity<Object> resetDefaultPassword(Principal principal, @Valid @RequestBody ResetDefaultPasswordDTO recoverPasswordDTO) {
       return backofficeLoginService.resetDefaultPassword(principal,recoverPasswordDTO);

    }

}
