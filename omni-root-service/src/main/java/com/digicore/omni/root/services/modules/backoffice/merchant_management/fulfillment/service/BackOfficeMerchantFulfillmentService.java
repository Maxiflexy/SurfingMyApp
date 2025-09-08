package com.digicore.omni.root.services.modules.backoffice.merchant_management.fulfillment.service;


import com.digicore.common.util.ClientUtil;
import com.digicore.notification.lib.request.FulfillmentNotificationRequest;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;

import com.digicore.omni.data.lib.modules.backoffice.model.BackOfficeProfile;
import com.digicore.omni.data.lib.modules.merchant.dto.ManualMerchantFulfillmentDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantFulfillmentDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.compliance.ComplianceFileUploadRequest;
import com.digicore.omni.data.lib.modules.merchant.model.*;
import com.digicore.omni.root.lib.modules.fulfillment.response.FulfillmentValidationResult;
import com.digicore.omni.root.lib.modules.fulfillment.service.fulfillment.BackOfficeFulfillmentService;
import com.digicore.omni.root.lib.modules.fulfillment.service.fulfillment.FulfillmentRetrieverService;

import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class BackOfficeMerchantFulfillmentService {

    @Value( "${omni.root.mail.fulfillment-approval-update.subject:Fulfillment Approval Update}")
    private String fulfillmentApprovalUpdateSubject;

    @Value( "${omni.root.mail.approval-update.subject:Approval Update}")
    private String approvalUpdateSubject;

    private final BackOfficeFulfillmentService backOfficeFulfillmentService;

    private final NotificationDispatcher notificationDispatcher;

    private final FulfillmentRetrieverService fulfillmentRetrieverService;


    public FulfillmentValidationResult validateAllIdAndSave(MerchantFulfillmentDTO merchantFulfillmentDTO)  {
        FulfillmentValidationResult validationResult = backOfficeFulfillmentService.validateAllIdAndSave(merchantFulfillmentDTO);
        sendFulfillmentAnalystApprovalEmail(merchantFulfillmentDTO.getMerchantId());
        sendApprovalAnalystEmail(merchantFulfillmentDTO.getMerchantId());

        return validationResult;
    }

    public void saveAllIdAndSave(FulfillmentValidationResult merchantFulfillmentDTO) {
        backOfficeFulfillmentService.saveManualMerchantValidationResult(merchantFulfillmentDTO);
        sendFulfillmentAnalystApprovalEmail(merchantFulfillmentDTO.getMerchantId());
        sendApprovalAnalystEmail(merchantFulfillmentDTO.getMerchantId());
    }

    public FulfillmentValidationResult retrieveBackofficeMerchantFulfillmentSummary(String merchantId)  {
        return backOfficeFulfillmentService.retrieveBackofficeMerchantFulfillmentAggregate(merchantId);
    }

    public void updateFailedMerchantFulfillment(ManualMerchantFulfillmentDTO fulfillmentDTO)  {
        backOfficeFulfillmentService.updateMerchantFulfillment(fulfillmentDTO);
    }

    public void declineMerchantFulfillment(MerchantFulfillmentDTO declineDTO)  {
         backOfficeFulfillmentService.declineMerchantFulfillment(declineDTO);
    }

    private void sendFulfillmentAnalystApprovalEmail(String merchantId) {

        MerchantProfile merchantProfile = fulfillmentRetrieverService.getMerchantProfile(merchantId);

        BackOfficeProfile backOfficeProfile = fulfillmentRetrieverService.fetchBackOfficeProfile();

        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .notificationSubject(fulfillmentApprovalUpdateSubject)
                .recipients(List.of(backOfficeProfile.getEmail()))
                .firstName(backOfficeProfile.getFirstName())
                .fulfillmentNotificationRequest(FulfillmentNotificationRequest.builder()
                        .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                        .status("SUCCESS")
                        .build())
                .notificationRequestType(NotificationRequestType.SEND_FULFILLMENT_ANALYST_APPROVAL_UPDATE_EMAIL)
                .build());
    }

    private void sendApprovalAnalystEmail(String merchantId) {
        String roleName = "FRANCHISE_RISK_APPROVER";
        MerchantProfile merchantProfile = fulfillmentRetrieverService.getMerchantProfile(merchantId);
        MerchantFulfillment fulfillment = fulfillmentRetrieverService.retrieveMerchantFulfillment(merchantId);

        Map<String, String> validationDetailsMap = new HashMap<>();
        validationDetailsMap.put("TIN", getFieldValueOrNull(fulfillment.getTinValidationDetails(), TINValidationDetails::getTaxIdentificationNumber));
        validationDetailsMap.put("CAC", getFieldValueOrNull(fulfillment.getCacValidationDetails(), CACValidationDetails::getCacNumber));
        validationDetailsMap.put("firstDirector", getFieldValueOrNull(fulfillment.getCacValidationDetails(), CACValidationDetails::getFirstDirectorName));
        validationDetailsMap.put("secondDirector", getFieldValueOrNull(fulfillment.getCacValidationDetails(), CACValidationDetails::getSecondDirectorName));
        validationDetailsMap.put("address", getFieldValueOrNull(fulfillment.getCacValidationDetails(), CACValidationDetails::getCompanyAddress));
        validationDetailsMap.put("nigerianId", getFieldValueOrNull(fulfillment.getNigerianIdValidationDetails(), NigerianIDValidationDetails::getIdNumber));
        validationDetailsMap.put("nigerianIdType", getFieldValueOrNull(fulfillment.getNigerianIdValidationDetails(), NigerianIDValidationDetails::getIdType));
        validationDetailsMap.put("nigerianIdName", getFieldValueOrNull(fulfillment.getNigerianIdValidationDetails(), NigerianIDValidationDetails::getNameOnId));
        validationDetailsMap.put("bvn", getFieldValueOrNull(fulfillment.getBvnValidationDetails(), BVNValidationDetails::getBvn));
        validationDetailsMap.put("bvnName", getFieldValueOrNull(fulfillment.getBvnValidationDetails(), BVNValidationDetails::getFullName));
        validationDetailsMap.put("dateOfBirth", getFieldValueOrNull(fulfillment.getBvnValidationDetails(), BVNValidationDetails::getDateOfBirth));
        validationDetailsMap.put("gender", getFieldValueOrNull(fulfillment.getBvnValidationDetails(), BVNValidationDetails::getGender));
        validationDetailsMap.put("phoneNumber", getFieldValueOrNull(fulfillment.getBvnValidationDetails(), BVNValidationDetails::getPhoneNumber));

        List<String> firstNames = retrieveAllAnalystFirstName(roleName);
        List<String> emailRecipients = retrieveAllAnalystEmails(roleName);

        Map<String, List<String>> firstNameToEmailMap = IntStream.range(0, firstNames.size())
                .boxed()
                .collect(Collectors.groupingBy(firstNames::get,
                        Collectors.mapping(emailRecipients::get, Collectors.toList())));

        IntStream.range(0, firstNames.size())
                .forEach(i -> {
                    String firstName = firstNames.get(i);
                    String emailRecipient = firstNameToEmailMap.get(firstName).toString();

                    notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                            .notificationSubject(approvalUpdateSubject)
                            .recipients(Collections.singletonList(emailRecipient))
                            .fulfillmentNotificationRequest(FulfillmentNotificationRequest.builder()
                                    .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                                    .merchantId(merchantId)
                                    .tin(validationDetailsMap.get("TIN"))
                                    .cac(validationDetailsMap.get("CAC"))
                                    .firstDirector(validationDetailsMap.get("firstDirector"))
                                    .secondDirector(validationDetailsMap.get("secondDirector"))
                                    .address(validationDetailsMap.get("address"))
                                    .nigerianId(validationDetailsMap.get("nigerianId"))
                                    .nigerianIdType(validationDetailsMap.get("nigerianIdType"))
                                    .nigerianIdName(validationDetailsMap.get("nigerianIdName"))
                                    .bvn(validationDetailsMap.get("bvn"))
                                    .bvnName(validationDetailsMap.get("bvnName"))
                                    .dateOfBirth(validationDetailsMap.get("dateOfBirth"))
                                    .gender(validationDetailsMap.get("gender"))
                                    .phoneNumber(validationDetailsMap.get("phoneNumber"))
                                    .adverseMediaCheck(fulfillment.getAdverseMediaCheck())
                                    .ofacScreening(fulfillment.getOfacScreeningCheck())
                                    .onWatchList(String.valueOf(fulfillment.isOnInternationalWatchlist()))
                                    .politicallyExposed(String.valueOf(fulfillment.isPoliticallyExposedPerson()))
                                    .riskProfiling(fulfillment.getMerchantRiskProfileStatus())
                                    .fulfillmentAnalyst(fulfillmentRetrieverService.retrieveMerchantFulfillmentValidator(ClientUtil.getLoggedInUsername()))
                                    .matchStatus(fulfillment.getProfileCheckMatch())
                                    .build())
                            .firstName(firstName)
                           .notificationRequestType(NotificationRequestType.SEND_RISK_APPROVAL_FULFILLMENT_UPDATE_EMAIL)
                            .build());
                });
    }

    private <T, R> R getFieldValueOrNull(T obj, Function<T, R> fieldExtractor) {
        return obj != null ? fieldExtractor.apply(obj) : null;
    }


    private List<String> retrieveAllAnalystEmails(String roleName) {

        List<BackOfficeProfile> backOfficeProfiles = fulfillmentRetrieverService.findAllBackOfficeProfileByRole(roleName);
        return backOfficeProfiles.stream()
                .map(BackOfficeProfile::getEmail)
                .collect(Collectors.toList());
    }


    private List<String> retrieveAllAnalystFirstName(String roleName) {

        List<BackOfficeProfile> backOfficeProfiles = fulfillmentRetrieverService.findAllBackOfficeProfileByRole(roleName);
        return backOfficeProfiles.stream()
                .map(BackOfficeProfile::getFirstName)
                .collect(Collectors.toList());
    }


    public void uploadBackofficeFulfillmentFile(String merchantId, ComplianceFileUploadRequest request) {
        backOfficeFulfillmentService.updateMerchantComplianceFile(merchantId, request);
    }

}
