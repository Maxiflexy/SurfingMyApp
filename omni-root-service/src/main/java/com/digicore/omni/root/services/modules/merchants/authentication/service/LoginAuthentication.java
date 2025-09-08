package com.digicore.omni.root.services.modules.merchants.authentication.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.common.util.AESUtil;
import com.digicore.common.util.ClientUtil;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.dtos.PasswordResetValidationDTO;
import com.digicore.omni.data.lib.modules.common.dtos.RecoverPasswordDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ResetDefaultPasswordDTO;

import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.root.lib.modules.common.services.LoginAttemptService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantUserAccountService;
import com.digicore.omni.root.lib.modules.merchant.service.PaymentRailSelectorService;
import com.digicore.omni.root.services.modules.common.authentication.AccountRecoveryService;
import com.digicore.omni.root.services.modules.common.authentication.LoginService;
import com.digicore.omni.root.services.modules.common.authentication.TokenRefresher;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginWith2faRequest;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.Resend2faOTPRequest;
import com.digicore.otp.enums.OtpType;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.otp.service.OtpService;
import com.digicore.request.processor.model.TokenVault;
import com.digicore.request.processor.repository.TokenVaultRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.digicore.omni.root.services.modules.common.utils.CommonUtils.buildFailureResponse;


@Service
@RequiredArgsConstructor
public class LoginAuthentication {

    private static final String EMAIL_INVALID_ERROR_MESSAGE = "Invalid email address provided";

    private static final String LOGINKEY_INVALID_ERROR_MESSAGE = "Invalid loginKey address provided";

   private final LoginService loginService;

   private final TokenVaultRepository tokenVaultRepository;
    private final MerchantService merchantService;

    private final MerchantUserAccountService merchantUserAccountService;

   private final AccountRecoveryService accountRecoveryService;

   private final LoginAttemptService loginAttemptService;

    private final TokenRefresher tokenRefresher;

   private final AESUtil aesUtil;


    private final NotificationDispatcher notificationDispatcher;

    private final OtpService otpService;

    @Value( "${omni.root.mail.login-notification:Login Notification}")
    private String loginNotification;

    @Value( "${omni.root.mail.compliance-update: Compliance Update}")
    private String  complianceUpdate;

    @Value( "${omni.root.mail.auth-otp: OTP For Authentication}")
    private String authOTP;

    @Value("${omni.authentication.request.is.encrypted:false}")
    private String requestIsEncryptedString;

    @SneakyThrows
    public ResponseEntity<Object> authenticateMerchant(SignIn signIn)  {

        boolean requestIsEncrypted = Boolean.parseBoolean(requestIsEncryptedString);

        if (requestIsEncrypted) {
            String decryptedDetails = aesUtil.decrypt(signIn.getEncryptedLoginDetails());
            signIn = ClientUtil.getObjectMapper().readValue(decryptedDetails, SignIn.class);
        }

        ResponseEntity<Object> profileStatusVerification = verifyProfileStatus(signIn.getUsername());
        if (!profileStatusVerification.getStatusCode().is2xxSuccessful())
            return profileStatusVerification;
        ApiResponseJson<?> apiResponseJson = (ApiResponseJson<?>) profileStatusVerification.getBody();
        MerchantProfileApiModel merchantProfileApiModel = (MerchantProfileApiModel) apiResponseJson.getData();
        LoginResponseApiModel responseApiModel;
        Map<String, Object> extraInfo = new HashMap<>();
        assert merchantProfileApiModel != null;
        if (merchantProfileApiModel.isEnable2faLogin()) {
            ResponseEntity<Object> response = loginService.authenticateWithoutGeneratingAccessToken(signIn);
            ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
            if (!authResult.isSuccess())
                return response;
            responseApiModel = (LoginResponseApiModel) authResult.getData();
            assert responseApiModel != null;
            extraInfo = responseApiModel.getAdditionalInformation();
            extraInfo.put("message", "You need to authenticate with 2fa");
            responseApiModel.setAdditionalInformation(extraInfo);
            sendOTPFor2faAuthentication(signIn.getUsername(), merchantProfileApiModel.getFirstName());
        } else {
            ResponseEntity<Object> response = loginService.authenticate(signIn,merchantProfileApiModel.getMerchantId());
            ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
            if (!authResult.isSuccess())
                return response;
            responseApiModel = (LoginResponseApiModel) authResult.getData();
            sendLoginSuccessEmail(merchantProfileApiModel);
            checkMerchantLiveStatusAndSendComplianceUpdateReminderEmail(merchantProfileApiModel);
        }



        extraInfo.put("merchantId", merchantProfileApiModel.getMerchantId());
        extraInfo.put("merchantEmail", merchantProfileApiModel.getEmail());
        extraInfo.put("complianceStatus", merchantProfileApiModel.getComplianceStatus());
        extraInfo.put("merchantName", merchantProfileApiModel.getLastName().concat(" ").concat(merchantProfileApiModel.getFirstName()));
        extraInfo.put("merchantBusinessName", merchantProfileApiModel.getMerchantBusinessDetailsDTO().getBusinessName());
        responseApiModel.setAdditionalInformation(extraInfo);

       // paymentRailSelectorService.checkIfMerchantExistInTableInsertIfNot(merchantProfileApiModel.getMerchantId());
        merchantService.generateTestKeys(merchantProfileApiModel.getMerchantId());

        return CommonUtils.buildSuccessResponse(responseApiModel,"Login Successful");
    }

    public ResponseEntity<Object> resend2faOTP(Resend2faOTPRequest resend2faOTPRequest){
        ResponseEntity<Object> profileStatusVerification = verifyProfileStatus(resend2faOTPRequest.getUsername());
        if (!profileStatusVerification.getStatusCode().is2xxSuccessful())
            return profileStatusVerification;
        ApiResponseJson<?> apiResponseJson = (ApiResponseJson<?>) profileStatusVerification.getBody();
        MerchantProfileApiModel merchantProfileApiModel = (MerchantProfileApiModel) apiResponseJson.getData();


       Optional<TokenVault> tokenVault = tokenVaultRepository.findByUserName(resend2faOTPRequest.getUsername());

        if(tokenVault.isPresent() && tokenVault.get().getToken().equalsIgnoreCase(resend2faOTPRequest.getLoginKey())){

            sendOTPFor2faAuthentication(resend2faOTPRequest.getUsername(), merchantProfileApiModel.getFirstName());

            return CommonUtils.buildSuccessResponse(null,"OTP sent Successfully");


        }


        return buildFailureResponse(List.of(new ApiError(String.format(LOGINKEY_INVALID_ERROR_MESSAGE, resend2faOTPRequest.getUsername()), "01")), HttpStatus.BAD_REQUEST);

    }


    public ResponseEntity<Object> authenticateMerchant(LoginWith2faRequest loginWith2faRequest) {
        ResponseEntity<Object> profileStatusVerification = verifyProfileStatus(loginWith2faRequest.getUsername());
        if (!profileStatusVerification.getStatusCode().is2xxSuccessful())
            return profileStatusVerification;
        ApiResponseJson<?> apiResponseJson = (ApiResponseJson<?>) profileStatusVerification.getBody();
        assert apiResponseJson != null;
        MerchantProfileApiModel merchantProfileApiModel = (MerchantProfileApiModel) apiResponseJson.getData();
        Map<String, Object> extraInfo;
        otpService.effect(loginWith2faRequest.getUsername(),OtpType.TWO_FA_VERIFICATION,loginWith2faRequest.getOtp());
        ResponseEntity<Object> response = loginService.authenticate(loginWith2faRequest,merchantProfileApiModel.getMerchantId());
        ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
        assert authResult != null;
        if (!authResult.isSuccess())
            return response;
        LoginResponseApiModel responseApiModel = (LoginResponseApiModel) authResult.getData();

        sendLoginSuccessEmail(merchantProfileApiModel);

        checkMerchantLiveStatusAndSendComplianceUpdateReminderEmail(merchantProfileApiModel);

        extraInfo = responseApiModel.getAdditionalInformation();


        extraInfo.put("merchantId",merchantProfileApiModel.getMerchantId());
        extraInfo.put("complianceStatus",merchantProfileApiModel.getComplianceStatus());
        extraInfo.put("merchantName",merchantProfileApiModel.getLastName().concat(" ").concat(merchantProfileApiModel.getFirstName()));
        extraInfo.put("merchantBusinessName",merchantProfileApiModel.getMerchantBusinessDetailsDTO().getBusinessName());
        responseApiModel.setAdditionalInformation(extraInfo);


        return CommonUtils.buildSuccessResponse(responseApiModel,"Login Successful");

    }

    private void checkMerchantLiveStatusAndSendComplianceUpdateReminderEmail(MerchantProfileApiModel merchantProfileApiModel) {
        if (!merchantProfileApiModel.isLiveMode()) {
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                    .firstName(merchantProfileApiModel.getFirstName())
                    .notificationSubject(complianceUpdate)
                    .recipients(List.of(merchantProfileApiModel.getEmail()))
                    .notificationRequestType(NotificationRequestType.SEND_COMPLIANCE_UPDATE_REMINDER_EMAIL)
                    .build());
        }
    }

    private void sendLoginSuccessEmail(MerchantProfileApiModel merchantProfileApiModel) {
        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                        .firstName(merchantProfileApiModel.getFirstName())
                        .recipients(List.of(merchantProfileApiModel.getEmail()))
                        .notificationSubject(loginNotification)
                        .notificationRequestType(NotificationRequestType.SEND_LOGIN_SUCCESS_EMAIL)
                        .build());
    }


    private ResponseEntity<Object> verifyProfileStatus(String username) {
        MerchantProfileApiModel merchantProfileApiModel = merchantUserAccountService.fetchMerchantUser(username);
        try {
            if (merchantProfileApiModel == null) {
                merchantProfileApiModel = merchantService.fetchUserByEmail(username);
            }
        } catch (ZeusRuntimeException e) {
            return CommonUtils.buildFailureResponse(e.getErrors(),e.getHttpStatus());
        }

        if (!merchantService.isMerchantProfileEnabled(merchantProfileApiModel.getMerchantId(), username)) {
            return CommonUtils.buildFailureResponse(List.of(new ApiError("Your account is disabled, kindly contact the admin!!!", "03")), HttpStatus.FORBIDDEN);

        }
        return CommonUtils.buildSuccessResponse(merchantProfileApiModel,null);
    }

    public ResponseEntity<Object> sendOTPForForgotPassword(String username)  {
        try {
            accountRecoveryService.recoverAccount(username);
        } catch (ZeusRuntimeException e) {
            return CommonUtils.buildFailureResponse(e.getErrors(),e.getHttpStatus());
        }
        return CommonUtils.buildSuccessResponse(null,null);


    }

    public ResponseEntity<Object> resetAccountPassword(RecoverPasswordDTO recoverPasswordDTO)  {
        try {
            accountRecoveryService.updateAccountPassword(recoverPasswordDTO);
            loginAttemptService.unlockUser(recoverPasswordDTO.getEmail());
        } catch (ZeusRuntimeException e) {
            return CommonUtils.buildFailureResponse(e.getErrors(),e.getHttpStatus());
        }
        return CommonUtils.buildSuccessResponse(null,null);

    }

    public ResponseEntity<Object> validateOTPForPasswordReset(PasswordResetValidationDTO recoverPasswordDTO)  {
        return accountRecoveryService.validateOTPForPasswordReset(recoverPasswordDTO);
    }

    public void sendOTPFor2faAuthentication(String email, String firstName) {
        otpService.send(NotificationServiceRequest.builder()
                .recipients(List.of(email))
                .notificationSubject(authOTP)
                .firstName(firstName)
                .notificationRequestType(NotificationRequestType.SEND_OTP_FOR_2FA_EMAIL)
                .build(), OtpType.TWO_FA_VERIFICATION);
    }



    public ResponseEntity<Object> resetDefaultPassword(Principal principal, ResetDefaultPasswordDTO recoverPasswordDTO)  {
        ResponseEntity<Object> response = accountRecoveryService.updateDefaultPassword(recoverPasswordDTO);
        if (response.getStatusCode().is2xxSuccessful()){
            merchantUserAccountService.updateInviteStatus(principal.getName(),true);
        }

        return tokenRefresher.getRefreshTokenAfterDefaultPasswordIsUpdated(principal);
    }

}
