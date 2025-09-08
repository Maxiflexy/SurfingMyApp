/*
 * Created by Monsuru (19/9/2022)
 */

package com.digicore.omni.root.services.modules.merchants.settings.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;

import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.common.util.ClientUtil;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.common.dtos.UpdatePasswordDTO;
import com.digicore.omni.data.lib.modules.common.enums.TransactionFeeCharge;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentRailsDTO;

import com.digicore.omni.data.lib.modules.merchant.dto.compliance.ComplianceFileUploadRequest;
import com.digicore.omni.data.lib.modules.merchant.dto.settings.MerchantBusinessProfileUpdateDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.settings.MerchantUserProfileUpdateDTO;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;

import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.lib.modules.merchant.service.PaymentRailSelectorService;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import lombok.RequiredArgsConstructor;


import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;


import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;



import javax.net.ssl.SSLContext;
import java.io.FileInputStream;

import java.security.KeyStore;
import java.security.Principal;

import java.util.ArrayList;
import java.util.List;


import static com.digicore.omni.root.services.modules.common.utils.CommonUtils.buildFailureResponse;
import static com.digicore.omni.root.services.modules.common.utils.CommonUtils.buildSuccessResponse;


import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.web.client.RestTemplate;


/**
 * @author Monsuru <br/>
 * @since Sep-19(Mon)-2022
 */
@Service
@RequiredArgsConstructor
public class SettingsService {

    private final MerchantService merchantService;



    private final PaymentRailSelectorService paymentRailSelectorService;

    private final NotificationDispatcher notificationDispatcher;

    private final AuditLogProcessor auditLogProcessor;

    private final AuthInformationUtils authInformationUtils;

    @Value( "${omni.root.mail.auth-otp:Password Change Notification}")
    private String changePassword;

    @Value( "${omni.root.mail.auth-otp:2FA Enabled}")
    private String enableTwoFactorAuth;

    @Value( "${omni.root.mail.auth-otp:2FA Disabled}")
    private String disableTwoFactorAuth;

    @Value("${apple.url}")
    private String apple_url;

    @Value("${pfx.file.path}")
    private String pfxFilePath;

    @Value("${pfx.password}")
    private String pfxPassword;

    @Value("${apple.host}")
    private String apple_host;

    private final RestTemplateBuilder restTemplateBuilder;



    public MerchantProfileApiModel getMerchantProfile(Principal principal)  {
        return merchantService.fetchUserByEmail(principal.getName());
    }

    public MerchantProfileApiModel updateMerchantUserProfile(MerchantUserProfileUpdateDTO merchantUserProfileUpdateDTO,
                                                             Principal principal)  {
        return merchantService.updateMerchantUserProfile(merchantUserProfileUpdateDTO, principal.getName());
    }

    public MerchantProfileApiModel updateMerchantBusinessProfile(MerchantBusinessProfileUpdateDTO merchantBusinessProfileUpdateDTO,
                                                                 Principal principal)  {
        return merchantService.updateMerchantBusinessProfile(merchantBusinessProfileUpdateDTO, principal.getName());
    }

    public void updateAccountPassword(UpdatePasswordDTO recoverPasswordDTO)  {
        merchantService.updateAccountPassword(recoverPasswordDTO);
            MerchantProfileApiModel merchant = merchantService.fetchUserByEmail(ClientUtil.getLoggedInUsername());
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                            .recipients(List.of(ClientUtil.getLoggedInUsername()))
                            .notificationSubject(changePassword)
                            .firstName(merchant.getFirstName())
                            .notificationRequestType(NotificationRequestType.SEND_PASSWORD_UPDATE_EMAIL)
                            .build());

    }

    public ApiResponseJson<Object> toggle2faStatus()  {
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        if (merchantService.update2FaLoginStatus()){
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(),auditLog.getUser(),auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY,"2FA enabled");
            MerchantProfileApiModel merchant = merchantService.fetchUserByEmail(ClientUtil.getLoggedInUsername());
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                            .recipients(List.of(ClientUtil.getLoggedInUsername()))
                            .notificationSubject(enableTwoFactorAuth)
                            .firstName(merchant.getFirstName())
                            .twoFaIsEnabled(true)
                            .notificationRequestType(NotificationRequestType.SEND_2FA_STATUS_EMAIL)
                            .build());
        }else {
            MerchantProfileApiModel merchant = merchantService.fetchUserByEmail(ClientUtil.getLoggedInUsername());
            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                    .recipients(List.of(ClientUtil.getLoggedInUsername()))
                    .notificationSubject(disableTwoFactorAuth)
                    .firstName(merchant.getFirstName())
                    .twoFaIsEnabled(false)
                    .notificationRequestType(NotificationRequestType.SEND_2FA_STATUS_EMAIL)
                    .build());
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(),auditLog.getUser(),auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY,"2FA disabled");
        }
        return ApiResponseJson.<Object>builder()
                .message("Request successfully treated")
                .success(true)
                .data(merchantService.fetch2faLoginStatus())
                .errors(new ArrayList<>())
                .build();
    }


    public ResponseEntity<Object> updateMerchantPaymentRails(PaymentRailsDTO paymentRailsDTO){

        if(paymentRailsDTO.getPaymentRails() == null || paymentRailsDTO.getPaymentRails().isEmpty()){
            return buildFailureResponse(List.of(new ApiError("You must select at least a payment rail ", "08")), HttpStatus.BAD_REQUEST);
        }

        boolean response =   paymentRailSelectorService.updateMerhantPaymentRails(paymentRailsDTO);

        if(response == false){
            return  buildFailureResponse(List.of(new ApiError("Invalid payment rail selected ", "08")), HttpStatus.BAD_REQUEST);
        }

       return  buildSuccessResponse("Payment rail updated!", "Request successfully treated");


    }

    public ResponseEntity<Object> getMerchantPaymentRails(){

        PaymentRailsDTO   paymentRailsDTO =     paymentRailSelectorService.getMerchantPaymentRails();

        return  buildSuccessResponse(paymentRailsDTO.getPaymentRails(), "Request successfully treated");

    }

    public ResponseEntity<Object> getAllPaymentRails(){

        List<String>  paymentRails = paymentRailSelectorService.getAllDefaultPaymentRail();

        return buildSuccessResponse(paymentRails, "Request successfully treated");
    }


    public ApiResponseJson<Object> fetch2faLoginStatus(){
        return ApiResponseJson.<Object>builder()
                .message("Request successfully treated")
                .success(true)
                .data(merchantService.fetch2faLoginStatus())
                .errors(new ArrayList<>())
                .build();
    }

    public void updateMerchantProfilePicture(String merchantId, ComplianceFileUploadRequest request) {
        merchantService.updateMerchantProfilePicture(merchantId, request);
    }

    public void updateMerchantFeeBearer(TransactionFeeCharge transactionFeeCharge) {
        merchantService.updateFeeBearer(transactionFeeCharge);
    }

   public ResponseEntity<Object> apple_verify_domain(String requestBody)throws Exception{

    try{
       KeyStore keyStore = KeyStore.getInstance("PKCS12");
       keyStore.load(new FileInputStream(pfxFilePath), pfxPassword.toCharArray());



       final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
       final SSLContext sslContext = SSLContextBuilder.create()
               .loadTrustMaterial(null, acceptingTrustStrategy)
               .loadKeyMaterial(keyStore, pfxPassword.toCharArray())
               .build();

       final SSLConnectionSocketFactory sslsf =
               new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
       final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
               .register("https", sslsf)
               .register("http", new PlainConnectionSocketFactory())
               .build();

       final BasicHttpClientConnectionManager connectionManager =
               new BasicHttpClientConnectionManager(socketFactoryRegistry);
       final CloseableHttpClient httpClient = HttpClients.custom()
               .setConnectionManager(connectionManager)
               .build();

       final HttpComponentsClientHttpRequestFactory requestFactory =
               new HttpComponentsClientHttpRequestFactory(httpClient);


       RestTemplate restTemplate = new RestTemplate(requestFactory);

       HttpHeaders headers = new HttpHeaders();

       headers.set("Host", apple_host);

       HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

       ResponseEntity<String> responseEntity = restTemplate.exchange(
               apple_url,
               HttpMethod.POST,
               requestEntity,
               String.class
       );

       if (responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getStatusCodeValue() == 204) {

        return  buildSuccessResponse(null, "Request successfully treated");
       } else {

           System.out.println("POST request failed. HTTP status code: " + responseEntity.getStatusCodeValue());
           throw new ZeusRuntimeException("POST request failed. HTTP status code: " + responseEntity.getStatusCode());
       }
   } catch (Exception e) {
        e.printStackTrace();
        throw new ZeusRuntimeException(e.getMessage());
    }



   }


}
