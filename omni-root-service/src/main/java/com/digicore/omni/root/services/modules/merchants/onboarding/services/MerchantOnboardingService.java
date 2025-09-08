package com.digicore.omni.root.services.modules.merchants.onboarding.services;


import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.notification.lib.config.NotificationPropertyConfig;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;
import com.digicore.omnexa.notification.lib.contract.template.model.TemplateProvider;
import com.digicore.omnexa.notification.lib.service.PluggableEmailService;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.enums.Gender;
import com.digicore.omni.data.lib.modules.common.permission.model.Authority;
import com.digicore.omni.data.lib.modules.common.permission.model.Role;
import com.digicore.omni.data.lib.modules.common.permission.repository.RoleRepository;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantBasicInformationDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantBusinessDetailsDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantProfileDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantRegistrationDTO;

import com.digicore.omni.data.lib.modules.merchant.util.MerchantServiceUtils;
import com.digicore.omni.root.lib.modules.common.services.UserAccountService;

import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;

import com.digicore.omni.root.lib.modules.merchant.service.PaymentRailSelectorService;
import com.digicore.omni.root.services.modules.merchants.onboarding.apiModels.SignUpResponse;
import com.digicore.omni.root.services.modules.merchants.onboarding.utils.MerchantSignUpAssemblerModel;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.dto.SubMerchantRegistrationDTO;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantOnboardRequest;
import com.digicore.otp.enums.OtpType;
import com.digicore.otp.request.OtpVerificationRequest;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.otp.service.OtpService;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.processors.AuditLogProcessor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.digicore.common.util.ClientUtil.commaSeparatedName;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_FIRST_FOOTER;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_FOOTER_LINK;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_HELLO_SALUTATION;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_HI_SALUTATION;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_LOGO;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_NAME;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_PROVIDER_NAME;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_SECOND_FOOTER;
import static com.digicore.notification.lib.constants.NotificationConstants.CONTEXT_VAR_THIRD_FOOTER;
import static com.digicore.omni.root.services.modules.common.utils.CommonUtils.*;


@Service
@RequiredArgsConstructor
public class MerchantOnboardingService {


    private static final String EMAIL_ONBOARDED_ERROR_MESSAGE = "The email %s is already onboarded";
    private static final String EMAIL_INVALID_ERROR_MESSAGE = "Invalid email address provided";
    private static final String MERCHANT_CREATION_SUCCESS_MESSAGE = "Merchant created successfully";
    private final MerchantService merchantService;
    private final UserAccountService userAccountService;
    private final MerchantSignUpAssemblerModel merchantSignUpAssemblerModel;
    private final AuditLogProcessor auditLogProcessor;
    private final RoleRepository roleRepository;
    private final OtpService otpService;
    private final MerchantServiceUtils merchantServiceUtils;

    private final PaymentRailSelectorService paymentRailSelectorService;
    private final NotificationDispatcher notificationDispatcher;
    @Value("${omni.root.mail.welcome.subject:Welcome to Redpay}")
    private String welcomeSubject;
    @Value("${omni.root.mail.onboarding-otp:OTP For Onboarding}")
    private String onboardingOTP;

    private final PluggableEmailService pluggableEmailService;

    private final NotificationPropertyConfig notificationPropertyConfig;

    /**
     * This method sends verification code to the email supplied.
     *
     * @param email     The email that should receive the verification code.
     * @param firstName The first name of the user the mail would be sent to.
     * @return It would return the default ApiResponseJson object and http status code 200 with a description of what action was performed.
     */
    public ResponseEntity<Object> sendVerificationCodeToEmail(String email, String firstName) {

        if (!isValidEmail(email))
            return buildFailureResponse(List.of(new ApiError(EMAIL_INVALID_ERROR_MESSAGE, "01")), HttpStatus.BAD_REQUEST);

        if (merchantService.emailIsTaken(email))
            return buildFailureResponse(List.of(new ApiError(String.format(EMAIL_ONBOARDED_ERROR_MESSAGE, email), "01")), HttpStatus.BAD_REQUEST);


        otpService.send(
                NotificationServiceRequest.builder()
                        .firstName(firstName)
                        .recipients(List.of(email))
                        .channel("EMAIL")
                        .notificationSubject(onboardingOTP)
                        .notificationRequestType(NotificationRequestType.SEND_ON_BOARDING_OTP_EMAIL)
                        .build(), OtpType.EMAIL_VERIFICATION);
        return buildSuccessResponse(null, "Verification code has been sent to this mail ".concat(email));

    }

    /**
     * This method validate verification code sent to the email supplied, it also automatically send another verification code to the phone number supplied.
     *
     * @param emailVerificationRequest This object contains fields that holds data to be verified.
     * @return It would return the default ApiResponseJson object and http status code 200 with a description of what action was performed.
     */

    public ResponseEntity<Object> validateEmailVerificationCode(OtpVerificationRequest emailVerificationRequest) {
        ResponseEntity<Object> result = verifyCodeSupplied(emailVerificationRequest.getEmail(), emailVerificationRequest.getOtp());
        if (result != null) return result;
        return sendVerificationCodeToPhoneNumber(NotificationServiceRequest.builder()
                .firstName(emailVerificationRequest.getFirstName())
                .recipients(List.of(emailVerificationRequest.getEmail()))
                .phoneNumbers(List.of(emailVerificationRequest.getPhoneNumber()))
                .requesterPhoneNumber(emailVerificationRequest.getPhoneNumber())
                .notificationRequestType(NotificationRequestType.SEND_SMS_OTP_FOR_ONBOARDING)
                .channel("SMS").build());

    }

    /**
     * This method validate verification code sent to the email supplied.
     *
     * @param email         This is the email of prospective merchant.
     * @param onboardingOTP This is the otp code sent to prospective merchant.
     * @return It would return the default ApiResponseJson object and http status code 200 with a description of what action was performed and a registration code.
     */

    public ResponseEntity<Object> validateEmailVerificationCode(String email, String onboardingOTP) {
        ResponseEntity<Object> result = verifyCodeSupplied(email, onboardingOTP);
        if (result != null) return result;
        Map<String, Object> response = new HashMap<>();
        response.put("registrationCode", otpService.store(email, OtpType.CUSTOMER_REGISTRATION));
        return buildSuccessResponse(response, "Verification code has been verified successfully");

    }

    @Nullable
    private ResponseEntity<Object> verifyCodeSupplied(String email, String onboardingOTP) {
        if (!isValidEmail(email))
            return buildFailureResponse(List.of(new ApiError(EMAIL_INVALID_ERROR_MESSAGE, "01")), HttpStatus.BAD_REQUEST);

        if (merchantService.emailIsTaken(email))
            return buildFailureResponse(List.of(new ApiError(String.format(EMAIL_ONBOARDED_ERROR_MESSAGE, email), "01")), HttpStatus.BAD_REQUEST);

        try {
            otpService.effect(email, OtpType.EMAIL_VERIFICATION, onboardingOTP);
        } catch (ZeusRuntimeException e) {
            return buildFailureResponse(List.of(new ApiError(e.getMessage(), "11")), HttpStatus.BAD_REQUEST);

        }
        return null;
    }

    /**
     * This method resend verification code to the phone number supplied.
     *
     * @param emailVerificationRequest This object contains fields that holds data to be verified.
     * @return It would return the default ApiResponseJson object and http status code 200 with a description of what action was performed.
     */
    public ResponseEntity<Object> resendVerificationCodeToPhoneNumber(OtpVerificationRequest emailVerificationRequest) {
        if (!isValidEmail(emailVerificationRequest.getEmail()))
            return buildFailureResponse(List.of(new ApiError(EMAIL_INVALID_ERROR_MESSAGE, "01")), HttpStatus.BAD_REQUEST);

        if (merchantService.emailIsTaken(emailVerificationRequest.getEmail()))
            return buildFailureResponse(List.of(new ApiError(String.format(EMAIL_ONBOARDED_ERROR_MESSAGE, emailVerificationRequest.getEmail()), "01")), HttpStatus.BAD_REQUEST);
        return sendVerificationCodeToPhoneNumber(NotificationServiceRequest.builder()
                .firstName(emailVerificationRequest.getFirstName())
                .recipients(List.of(emailVerificationRequest.getEmail()))
                .phoneNumbers(List.of(emailVerificationRequest.getPhoneNumber()))
                .requesterPhoneNumber(emailVerificationRequest.getPhoneNumber())
                .notificationRequestType(NotificationRequestType.SEND_SMS_OTP_FOR_ONBOARDING)
                .channel("SMS").build());

    }


    /**
     * This method resend verification code to the phone number supplied.
     *
     * @param notificationServiceRequest This object contains fields that holds data about the notification to be sent.
     * @return It would return the default ApiResponseJson object and http status code 200 with a description of what action was performed.
     */
    private ResponseEntity<Object> sendVerificationCodeToPhoneNumber(NotificationServiceRequest notificationServiceRequest) {
        otpService.send(notificationServiceRequest, OtpType.PHONE_NUMBER_VERIFICATION);
        return buildSuccessResponse(null, "Verification code has been sent to this phone number  ".concat(notificationServiceRequest.getRequesterPhoneNumber()));
    }

    /**
     * This method validate verification code sent to the phone number supplied.
     *
     * @param phoneNumberVerificationRequest This object contains fields that holds data to be verified.
     * @return It would return the default ApiResponseJson object, http status code 200 with a description of what action was performed and a registration code.
     */
    public ResponseEntity<Object> validatePhoneNumberVerificationCode(OtpVerificationRequest phoneNumberVerificationRequest) {
        if (!isValidEmail(phoneNumberVerificationRequest.getEmail()))
            return buildFailureResponse(List.of(new ApiError(EMAIL_INVALID_ERROR_MESSAGE, "01")), HttpStatus.BAD_REQUEST);

        if (merchantService.emailIsTaken(phoneNumberVerificationRequest.getEmail()))
            return buildFailureResponse(List.of(new ApiError(String.format(EMAIL_ONBOARDED_ERROR_MESSAGE, phoneNumberVerificationRequest.getEmail()), "01")), HttpStatus.BAD_REQUEST);
        otpService.effect(phoneNumberVerificationRequest.getEmail(), OtpType.PHONE_NUMBER_VERIFICATION, phoneNumberVerificationRequest.getOtp());
        Map<String, Object> response = new HashMap<>();
        response.put("registrationCode", otpService.store(phoneNumberVerificationRequest.getEmail().concat(phoneNumberVerificationRequest.getPhoneNumber()), OtpType.CUSTOMER_REGISTRATION));
        return buildSuccessResponse(response, "Verification codes has been verified successfully");

    }

    @Transactional(rollbackFor = {Exception.class, ZeusRuntimeException.class, RuntimeException.class})
    public ResponseEntity<Object> registerMerchant(MerchantRegistrationDTO merchantRegistrationDTO) {
        try {
            otpService.effect(merchantRegistrationDTO.getEmail().concat(merchantRegistrationDTO.getPhoneNumber()), OtpType.CUSTOMER_REGISTRATION, merchantRegistrationDTO.getRegistrationCode());//
            return createMerchantAccountAndProfile(merchantRegistrationDTO);

        } catch (ZeusRuntimeException e) {
            return buildFailureResponse(List.of(new ApiError(e.getMessage(), e.getCode())), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(rollbackFor = {Exception.class, ZeusRuntimeException.class, RuntimeException.class})
    public ResponseEntity<Object> registerMerchantV2(MerchantRegistrationDTO merchantRegistrationDTO) {
        try {
            otpService.effect(merchantRegistrationDTO.getEmail(), OtpType.CUSTOMER_REGISTRATION, merchantRegistrationDTO.getRegistrationCode());
            return createMerchantAccountAndProfile(merchantRegistrationDTO);

        } catch (ZeusRuntimeException e) {
            return buildFailureResponse(List.of(new ApiError(e.getMessage(), e.getCode())), HttpStatus.BAD_REQUEST);
        }
    }

    @NotNull
    private ResponseEntity<Object> createMerchantAccountAndProfile(MerchantRegistrationDTO merchantRegistrationDTO) {
        validateMerchantCreationRequest(new MerchantBasicInformationDTO(merchantRegistrationDTO.getEmail(), merchantRegistrationDTO.getPhoneNumber(), merchantRegistrationDTO.getBusinessName()));
        Role role = roleRepository.findByName("BUSINESS_OWNER");
        MerchantProfileApiModel merchantProfileApiModel;
        if (isUserAccountCreated(merchantRegistrationDTO, role)) {
            merchantProfileApiModel = merchantService.saveUser(MerchantProfileDTO.builder()
                    .merchantId(merchantServiceUtils.generateMerchantId())
                    .firstName(merchantRegistrationDTO.getFirstName().toUpperCase())
                    .lastName(merchantRegistrationDTO.getLastName().toUpperCase())
                    .email(merchantRegistrationDTO.getEmail().toUpperCase())
                    .gender(Gender.valueOf(merchantRegistrationDTO.getGender()))
                    .phoneNumber(merchantRegistrationDTO.getPhoneNumber())
                    .merchantBusinessDetails(MerchantBusinessDetailsDTO.builder()
                            .businessName(merchantRegistrationDTO.getBusinessName().toUpperCase()).build())
                    .build());
            sendWelcomeMailToMerchant(merchantRegistrationDTO, merchantProfileApiModel);

            paymentRailSelectorService.savePaymentRails(merchantServiceUtils.generateMerchantId());

            return buildSuccessResponse(merchantSignUpAssemblerModel.toModel(SignUpResponse.builder()
                    .merchantId(merchantProfileApiModel != null ? merchantProfileApiModel.getMerchantId() : null)
                    .build()), MERCHANT_CREATION_SUCCESS_MESSAGE);
        }
        return buildFailureResponse(List.of(new ApiError("Merchant account creation failed", "09")), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @NotNull
    public ResponseEntity<Object> createSubMerchantAccountAndProfile(SubMerchantRegistrationDTO subMerchantOnboardingDTO) {
        validateMerchantCreationRequest(new MerchantBasicInformationDTO(subMerchantOnboardingDTO.getEmail(), subMerchantOnboardingDTO.getPhoneNumber(), subMerchantOnboardingDTO.getBusinessName()));
        Role role = roleRepository.findByName("BUSINESS_OWNER");
        MerchantProfileApiModel merchantProfileApiModel;
        if (isUserAccountCreated(subMerchantOnboardingDTO, role)) {
            merchantProfileApiModel = merchantService.saveUser(MerchantProfileDTO.builder()
                    .merchantId(merchantServiceUtils.generateMerchantId())
                    .firstName(subMerchantOnboardingDTO.getFirstName().toUpperCase())
                    .lastName(subMerchantOnboardingDTO.getLastName().toUpperCase())
                    .email(subMerchantOnboardingDTO.getEmail().toUpperCase())
                    .gender(Gender.valueOf(subMerchantOnboardingDTO.getGender()))
                    .phoneNumber(subMerchantOnboardingDTO.getPhoneNumber())
                            .parentMerchantId(subMerchantOnboardingDTO.getParentMerchantId())
                    .merchantBusinessDetails(MerchantBusinessDetailsDTO.builder()
                            .businessName(subMerchantOnboardingDTO.getBusinessName().toUpperCase()).build())
                    .build());
            sendWelcomeMailToMerchant(subMerchantOnboardingDTO, merchantProfileApiModel);

            paymentRailSelectorService.savePaymentRails(merchantServiceUtils.generateMerchantId());

            return buildSuccessResponse(merchantSignUpAssemblerModel.toModel(SignUpResponse.builder()
                    .merchantId(merchantProfileApiModel != null ? merchantProfileApiModel.getMerchantId() : null)
                    .build()), MERCHANT_CREATION_SUCCESS_MESSAGE);
        }
        return buildFailureResponse(List.of(new ApiError("Merchant account creation failed", "09")), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendWelcomeMailToMerchant(MerchantRegistrationDTO merchantRegistrationDTO, MerchantProfileApiModel merchantProfileApiModel) {
        if (merchantProfileApiModel != null) {
            auditLogProcessor.saveAuditWithDescription("MERCHANT", merchantRegistrationDTO.getFirstName().concat(" ").concat(merchantRegistrationDTO.getLastName()), merchantRegistrationDTO.getEmail(), LogActivityType.CREATE_ACTIVITY, MERCHANT_CREATION_SUCCESS_MESSAGE);

            NotificationServiceRequest notificationServiceRequest = NotificationServiceRequest.builder()
                    .recipients(List.of(merchantRegistrationDTO.getEmail()))
                    .notificationSubject(welcomeSubject)
                    .firstName(merchantRegistrationDTO.getFirstName())
                    .notificationRequestType(NotificationRequestType.SEND_WELCOME_EMAIL)
                    .build();

            Context context = buildHtmlContext(notificationServiceRequest);
            context.setVariable(CONTEXT_VAR_NAME, commaSeparatedName(CONTEXT_VAR_HELLO_SALUTATION.concat(notificationServiceRequest.getFirstName())));

            String htmlStr = pluggableEmailService.getPluggableTemplateService().getEngine(TemplateProvider.THYMELEAF).parseTemplate("welcomeOnboard", context);
            EmailRequest emailRequest = buildSendMail(notificationServiceRequest);
            emailRequest.setCopy(htmlStr);

            pluggableEmailService.getEngine(EmailChannelType.SENDGRID).sendEmailAsync(emailRequest);

//            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
//                    .recipients(List.of(merchantRegistrationDTO.getEmail()))
//                    .notificationSubject(welcomeSubject)
//                    .firstName(merchantRegistrationDTO.getFirstName())
//                    .notificationRequestType(NotificationRequestType.SEND_WELCOME_EMAIL)
//                    .build());
        }
    }

    private void sendWelcomeMailToMerchant(SubMerchantRegistrationDTO merchantRegistrationDTO, MerchantProfileApiModel merchantProfileApiModel) {
        if (merchantProfileApiModel != null) {
            auditLogProcessor.saveAuditWithDescription("MERCHANT", merchantRegistrationDTO.getFirstName().concat(" ").concat(merchantRegistrationDTO.getLastName()), merchantRegistrationDTO.getEmail(), LogActivityType.CREATE_ACTIVITY, MERCHANT_CREATION_SUCCESS_MESSAGE);

            NotificationServiceRequest notificationServiceRequest = NotificationServiceRequest.builder()
                    .recipients(List.of(merchantRegistrationDTO.getEmail()))
                    .notificationSubject(welcomeSubject)
                    .firstName(merchantRegistrationDTO.getFirstName())
                    .notificationRequestType(NotificationRequestType.SEND_WELCOME_EMAIL)
                    .build();

            Context context = buildHtmlContext(notificationServiceRequest);
            context.setVariable(CONTEXT_VAR_NAME, commaSeparatedName(CONTEXT_VAR_HELLO_SALUTATION.concat(notificationServiceRequest.getFirstName())));

            String htmlStr = pluggableEmailService.getPluggableTemplateService().getEngine(TemplateProvider.THYMELEAF).parseTemplate("welcomeOnboard", context);
            EmailRequest emailRequest = buildSendMail(notificationServiceRequest);
            emailRequest.setCopy(htmlStr);

            pluggableEmailService.getEngine(EmailChannelType.SENDGRID).sendEmailAsync(emailRequest);
        }
    }

    private Context buildHtmlContext(NotificationServiceRequest notificationServiceRequest) {
        Context context = new Context();
        context.setVariable(CONTEXT_VAR_LOGO, notificationPropertyConfig.getLogoUrlGreen());
        context.setVariable(CONTEXT_VAR_FIRST_FOOTER, notificationPropertyConfig.getFirstFooter());
        context.setVariable(CONTEXT_VAR_SECOND_FOOTER, notificationPropertyConfig.getSecondFooter());
        context.setVariable(CONTEXT_VAR_THIRD_FOOTER, notificationPropertyConfig.getThirdFooter());
        context.setVariable(CONTEXT_VAR_FOOTER_LINK, notificationPropertyConfig.getFooterLink());
        context.setVariable(CONTEXT_VAR_PROVIDER_NAME, notificationPropertyConfig.getProviderName());
        context.setVariable(getFieldValue(CONTEXT_VAR_NAME), commaSeparatedName(CONTEXT_VAR_HI_SALUTATION.concat(getFieldValue(notificationServiceRequest.getFirstName()))));
        return context;
    }

    private EmailRequest buildSendMail(NotificationServiceRequest notificationServiceRequest) {
        Map<String, Object> placeHolders = new HashMap<>();

        return EmailRequest.builder()
                .useTemplate(false)
                .sender("donotreply@sofriwebservices.com")
                .subject("OTP Verification")
                .placeHolders(placeHolders)
                .isHtml(true)
                .recipients(Set.of(notificationServiceRequest.getRecipients().get(0)))
                .templateName("d-ee6494c2086648f1b59c18e5f1b4886e")
                .build();
    }

    private String getFieldValue(String fieldValue) {
        return fieldValue != null ? fieldValue : "";
    }

    private boolean isUserAccountCreated(MerchantRegistrationDTO merchantRegistrationDTO, Role role) {
        return userAccountService.saveUser(UserAccountApiModel.builder()
                .lastName(merchantRegistrationDTO.getLastName().toUpperCase())
                .firstName(merchantRegistrationDTO.getFirstName().toUpperCase())
                .username(merchantRegistrationDTO.getEmail().toUpperCase())
                .email(merchantRegistrationDTO.getEmail().toUpperCase())
                .role(role.getName())
                .permissions(new ArrayList<>(role.getPermissions().stream().map(Authority::getPermission).toList()))
                .password(merchantRegistrationDTO.getPassword())
                .build(), false);
    }


    private boolean isUserAccountCreated(SubMerchantOnboardRequest merchantRegistrationDTO, Role role) {
        return userAccountService.saveUser(UserAccountApiModel.builder()
                .lastName(merchantRegistrationDTO.getLastName().toUpperCase())
                .firstName(merchantRegistrationDTO.getFirstName().toUpperCase())
                .username(merchantRegistrationDTO.getEmail().toUpperCase())
                .email(merchantRegistrationDTO.getEmail().toUpperCase())
                .role(role.getName())
                .permissions(new ArrayList<>(role.getPermissions().stream().map(Authority::getPermission).toList()))
                .password(merchantRegistrationDTO.getPassword())
                .build(), false);
    }


    public ResponseEntity<Object> validateMerchantCreationRequest(MerchantBasicInformationDTO merchantBasicInformationDTO) {
        ArrayList<Object> validatedResponse = new ArrayList<>();
        if (merchantService.emailIsTaken(merchantBasicInformationDTO.getEmail()))
            validatedResponse.add(String.format(EMAIL_ONBOARDED_ERROR_MESSAGE, merchantBasicInformationDTO.getEmail()));
        if (merchantService.businessNameIsTaken(merchantBasicInformationDTO.getBusinessName()))
            validatedResponse.add(String.format("The business name %s is already onboarded", merchantBasicInformationDTO.getBusinessName()));
        if (!isValidEmail(merchantBasicInformationDTO.getEmail()))
            validatedResponse.add(String.format("%s is not a valid email address", merchantBasicInformationDTO.getEmail()));
        if (validatedResponse.isEmpty())
            return buildSuccessResponse(null, "Merchant information validated successfully");

        return buildFailureResponse(validatedResponse.stream().map(x -> new ApiError((String) x, "05")).toList(), HttpStatus.BAD_REQUEST);
    }
}
