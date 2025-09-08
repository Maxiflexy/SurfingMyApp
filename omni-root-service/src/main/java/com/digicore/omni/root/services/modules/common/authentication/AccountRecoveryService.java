package com.digicore.omni.root.services.modules.common.authentication;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.PasswordResetValidationDTO;
import com.digicore.omni.data.lib.modules.common.dtos.RecoverPasswordDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ResetDefaultPasswordDTO;

import com.digicore.omni.root.lib.modules.common.services.UserAccountService;
import com.digicore.omni.root.services.modules.common.apiModels.PasswordResetApiModel;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.otp.enums.OtpType;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-02(Fri)-2022
 */

@Service
@RequiredArgsConstructor
public class AccountRecoveryService {

    private final UserAccountService userService;

    private final NotificationDispatcher notificationDispatcher;

    private final OtpService otpService;



    @Value("${omni.root.mail.account-recovery-otp: Account Recovery OTP}")
    private String accountRecoveryOtp;

    @Value("${omni.root.mail.account-recovery-subject: Password Reset}")
    private String accountRecoverySubject;



    public void recoverAccount(String username)  {
        UserAccountApiModel accountApiModel = userService.fetchUserByUsername(username);
        otpService.send(NotificationServiceRequest.builder()
                .recipients(List.of(accountApiModel.getEmail()))
                .firstName(accountApiModel.getFirstName())
                .notificationSubject(accountRecoveryOtp)
                .notificationRequestType(NotificationRequestType.SEND_ACCOUNT_RECOVERY_EMAIL)
                .build(), OtpType.PASSWORD_UPDATE);
    }

    @Transactional
    public void updateAccountPassword(RecoverPasswordDTO recoverPasswordDTO)  {
        otpService.effect(recoverPasswordDTO.getEmail(),OtpType.PASSWORD_UPDATE_RECOVERY_KEY,recoverPasswordDTO.getRecoverKey());
        userService.updateAccount(recoverPasswordDTO);
        UserAccountApiModel user = userService.fetchUserByUsername(recoverPasswordDTO.getEmail());
        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .recipients(List.of(user.getEmail()))
                .notificationSubject(accountRecoverySubject)
                .firstName(user.getFirstName())
                .notificationRequestType(NotificationRequestType.SEND_PASSWORD_UPDATE_EMAIL)
                .build());

    }

    public ResponseEntity<Object> updateDefaultPassword(ResetDefaultPasswordDTO recoverPasswordDTO)  {
        UserAccountApiModel user = userService.fetchUserByUsername(recoverPasswordDTO.getEmail());
        if (user.isDefaultPassword()) {
            RecoverPasswordDTO passwordDTO =  new RecoverPasswordDTO();
            passwordDTO.setEmail(recoverPasswordDTO.getEmail());
            passwordDTO.setNewPassword(recoverPasswordDTO.getNewPassword());
            userService.updateAccount(passwordDTO);
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                    .recipients(List.of(user.getEmail()))
                    .notificationSubject(accountRecoverySubject)
                    .firstName(user.getFirstName())
                    .notificationRequestType(NotificationRequestType.SEND_PASSWORD_UPDATE_EMAIL)
                    .build());
            return CommonUtils.buildSuccessResponse(null, null);
        }
        return CommonUtils.buildFailureResponse(List.of(new ApiError("operation not allowed","09")),HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<Object> validateOTPForPasswordReset(PasswordResetValidationDTO recoverPasswordDTO)  {
        try {
            otpService.effect(recoverPasswordDTO.getEmail(),OtpType.PASSWORD_UPDATE,recoverPasswordDTO.getOtp());
        } catch (ZeusRuntimeException e) {
            return CommonUtils.buildFailureResponse(e.getErrors(),e.getHttpStatus());
        }
        String recoveryKey = otpService.store(recoverPasswordDTO.getEmail(),OtpType.PASSWORD_UPDATE_RECOVERY_KEY);
        return CommonUtils.buildSuccessResponse(PasswordResetApiModel.builder()
                .recoveryKey(recoveryKey)
                .build(),null);


    }
}
