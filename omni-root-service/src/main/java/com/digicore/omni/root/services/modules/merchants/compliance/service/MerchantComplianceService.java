package com.digicore.omni.root.services.modules.merchants.compliance.service;


import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;

import com.digicore.omni.data.lib.modules.backoffice.model.BackOfficeProfile;
import com.digicore.omni.data.lib.modules.backoffice.repository.BackOfficeProfileRepository;
import com.digicore.omni.data.lib.modules.common.dtos.BankInstitutionCodeDTO;
import com.digicore.omni.data.lib.modules.common.enums.UserStatus;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.compliance.*;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.util.MerchantServiceUtils;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;
import com.digicore.omni.root.lib.modules.fulfillment.service.fulfillment.FulfillmentRetrieverService;

import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.services.config.PropertyConfig;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantComplianceService {

    private final MerchantService merchantService;


    private final BackOfficeProfileRepository backOfficeProfileRepository;

    private final PropertyConfig propertyConfig;

    private final AuditLogProcessor auditLogProcessor;

    private final AuthInformationUtils authInformationUtils;

    private final FulfillmentRetrieverService fulfillmentRetrieverService;

    private final NotificationDispatcher notificationDispatcher;

    private final MerchantServiceUtils merchantServiceUtils;

    @Value("${bank_institution_codes}")
    private String bankInstitutionCodePath;

    @Value( "${omni.root.mail.compliance-completion: Pending Compliance}")
    private String  pendingCompliance;

    @Value( "${omni.root.mail.pending-fulfillment-approval-update.subject:Pending Fulfillment Approval Update}")
    private String pendingFulfillmentApprovalSubject;


    public Object updateBusinessInformationCompliance(Object request)  {
        Object object = merchantService.updateBusinessInformationCompliance((BusinessInformationComplianceRequest) request);
        if(object != null) {
            AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY, "Compliance update- Business Information");
        }
        return object;

    }

    public Object updateStarterMerchantPersonalInformationCompliance(StarterPersonalInformationComplianceRequest request) {
        MerchantProfileApiModel merchantProfileApiModel =  merchantService.updateStarterPersonalInformationCompliance(request);
            AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY, "Compliance update- Personal Information");
        return merchantProfileApiModel;

    }

    public Object updateRegisteredMerchantPersonalInformationCompliance(RegisteredBusinessPersonalInformationComplianceRequest request) {
        MerchantProfileApiModel merchantProfileApiModel =  merchantService.updateRegisteredPersonalInformationCompliance(request);
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY, "Compliance update- Personal Information");
        return merchantProfileApiModel;

    }

    public Object updateGovernmentMerchantPersonalInformationCompliance(GovernmentPersonalInformationComplianceRequest request) {
        MerchantProfileApiModel merchantProfileApiModel =  merchantService.updateGovernmentPersonalInformationCompliance(request);
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY, "Compliance update- Personal Information");
        return merchantProfileApiModel;

    }

    public void uploadMerchantComplianceFiles(ComplianceFileUploadRequest complianceFileUploadRequest ) {
//        ComplianceFileUploadRequest complianceFileUploadRequest = new ComplianceFileUploadRequest();
//        int i = 0;
//        MerchantDoc[] merchantDocs = new MerchantDoc[files.length];
//
//        for (MultipartFile file : files){
//            merchantDocs[i] = new MerchantDoc(extras[i],file);
//        }
//        complianceFileUploadRequest.setFiles(merchantDocs);
         merchantService.uploadMerchantComplianceFiles(complianceFileUploadRequest);
    }




    public Object updateMerchantBankAccountInformationCompliance(Object request) {
        Object object = merchantService.updateBankAccountInformationCompliance((BankAccountInformationComplianceRequest) request);
        if(object != null) {
            AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.UPDATE_ACTIVITY, "Compliance update- Bank Details Information");
        }

        sendPendingReviewUpdateToAnalyst();

        return object;
    }

    public List<BankInstitutionCodeDTO> getBankInstitutionCodes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(Paths.get(bankInstitutionCodePath).toFile(), new TypeReference<>() {});
        } catch (IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }


    public ResponseEntity<Object> getSupportedDocumentTypes(){
        return CommonUtils.buildSuccessResponse(propertyConfig.getComplianceDocumentTypes());
    }



    private void sendPendingReviewUpdateToAnalyst() {

        log.info("----------> send notification to fulfillment analyst");

        String roleName = "FULFILLMENT_ANALYST";




        List<BackOfficeProfile> backOfficeProfiles = retrieveBackOfficeByUserStatusAndRole(UserStatus.ENABLED,roleName);
        MerchantProfile merchantProfile = fetchMerchantProfile();
        for(BackOfficeProfile backOfficeProfile : backOfficeProfiles) {

            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                    .notificationSubject(pendingFulfillmentApprovalSubject)
                    .recipients(Collections.singletonList(backOfficeProfile.getEmail()))
                    .firstName(backOfficeProfile.getFirstName())
                    .merchantFullName(merchantProfile.getFirstName().concat(" ").concat(merchantProfile.getLastName()))
                    .status(merchantProfile.getComplianceStatus().toString())
                    .businessName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                    .notificationRequestType(NotificationRequestType.SEND_PENDING_FULFILLMENT_UPDATE_EMAIL)
                    .build());


        }

    }




    private List<String> retrieveAllAnalystEmails(String roleName) {

        List<BackOfficeProfile> backOfficeProfiles = fulfillmentRetrieverService.findAllBackOfficeProfileByRole(roleName);
        return backOfficeProfiles.stream()
                .map(BackOfficeProfile::getEmail)
                .collect(Collectors.toList());
    }

    private List<BackOfficeProfile> retrieveBackOfficeByUserStatusAndRole(UserStatus userStatus, String roleName) {

        List<BackOfficeProfile> backOfficeProfiles = backOfficeProfileRepository.findAllByUserStatusAndRole(userStatus, roleName);
        return backOfficeProfiles;
    }

    private List<String> retrieveAllAnalystFirstName(String roleName) {

        List<BackOfficeProfile> backOfficeProfiles = fulfillmentRetrieverService.findAllBackOfficeProfileByRole(roleName);
        return backOfficeProfiles.stream()
                .map(BackOfficeProfile::getFirstName)
                .collect(Collectors.toList());
    }

    private MerchantProfile fetchMerchantProfile() {
        return merchantServiceUtils.getMerchantProfile();
    }

}
