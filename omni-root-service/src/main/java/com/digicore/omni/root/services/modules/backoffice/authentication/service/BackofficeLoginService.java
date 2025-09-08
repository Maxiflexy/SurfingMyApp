package com.digicore.omni.root.services.modules.backoffice.authentication.service;


import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.common.util.AESUtil;
import com.digicore.common.util.ClientUtil;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.backoffice.apimodel.BackOfficeUserApiModel;
import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.dtos.PasswordResetValidationDTO;
import com.digicore.omni.data.lib.modules.common.dtos.RecoverPasswordDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ResetDefaultPasswordDTO;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;

import com.digicore.omni.root.lib.modules.common.services.LoginAttemptService;
import com.digicore.omni.root.services.modules.common.authentication.AccountRecoveryService;
import com.digicore.omni.root.services.modules.common.authentication.LoginService;
import com.digicore.omni.root.services.modules.common.authentication.TokenRefresher;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-02(Fri)-2022
 */

@Service
@RequiredArgsConstructor
public class BackofficeLoginService {

    private final LoginService loginService;

    private final LoginAttemptService loginAttemptService;

    private final AccountRecoveryService accountRecoveryService;

    private final BackOfficeService backOfficeService;

    private final NotificationDispatcher notificationDispatcher;

    private final TokenRefresher tokenRefresher;
    
    private final AESUtil aesUtil;


    @Value( "${omni.root.mail.login-notification:Login Notification}")
    private String loginNotification;

    @Value("${omni.authentication.request.is.encrypted:false}")
    private String requestIsEncryptedString;
    private boolean requestIsEncrypted;

    @SneakyThrows
    public ResponseEntity<Object> authenticateBackOfficeUser(SignIn signIn)  {

        requestIsEncrypted = Boolean.parseBoolean(requestIsEncryptedString);

        // Check if the request is encrypted and if encrypted, decrypt it
        if (requestIsEncrypted) {
            String decryptedDetails = aesUtil.decrypt(signIn.getEncryptedLoginDetails());
            signIn = ClientUtil.getObjectMapper().readValue(decryptedDetails, SignIn.class);
        }

        if (!backOfficeService.isBackOfficeProfileEnabled(signIn.getUsername())) {
            throw new ZeusRuntimeException("Your account is disabled, kindly contact the admin", HttpStatus.UNAUTHORIZED);
        } else {
        //you can always add extra info to the additional information
            ResponseEntity<Object> response = loginService.authenticate(signIn,null);
            ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
            assert authResult != null;
            if (!authResult.isSuccess())
                return response;
            LoginResponseApiModel responseApiModel = (LoginResponseApiModel) authResult.getData();

            Map<String, Object> extraInfo;
        extraInfo = responseApiModel.getAdditionalInformation();
        BackOfficeUserApiModel backOfficeUserApiModel = backOfficeService.fetchUserByEmail((String) extraInfo.get("email"));
        extraInfo.put("userId", backOfficeUserApiModel.getBackOfficeUserId());
        extraInfo.put("userName", signIn.getUsername());
        extraInfo.put("name", backOfficeUserApiModel.getLastName().concat(" ").concat(backOfficeUserApiModel.getFirstName()));
        responseApiModel.setAdditionalInformation(extraInfo);
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .recipients(List.of(backOfficeUserApiModel.getEmail()))
                .notificationSubject(loginNotification)
                .firstName(backOfficeUserApiModel.getFirstName())
                .notificationRequestType(NotificationRequestType.SEND_LOGIN_SUCCESS_EMAIL)
                .build());


        return CommonUtils.buildSuccessResponse(responseApiModel,null);

        }
    }

    public ResponseEntity<Object> resetDefaultPassword(Principal principal, ResetDefaultPasswordDTO recoverPasswordDTO)  {
        String email = principal.getName();
        recoverPasswordDTO.setEmail(email);
        ResponseEntity<Object> response = accountRecoveryService.updateDefaultPassword(recoverPasswordDTO);
        if (response.getStatusCode().is2xxSuccessful()){
            backOfficeService.updateProfileStatus(principal.getName(),true);
        }

        return tokenRefresher.getRefreshTokenAfterDefaultPasswordIsUpdated(principal);

    }

    public void sendOTPForForgotPassword(String username)  {
        accountRecoveryService.recoverAccount(username);
    }

    public ResponseEntity<Object> validateOTPForPasswordReset(PasswordResetValidationDTO recoverPasswordDTO)   {
        return accountRecoveryService.validateOTPForPasswordReset(recoverPasswordDTO);
    }

    public void resetAccountPassword(RecoverPasswordDTO recoverPasswordDTO) {
        accountRecoveryService.updateAccountPassword(recoverPasswordDTO);
        loginAttemptService.unlockUser(recoverPasswordDTO.getEmail());

    }
}
