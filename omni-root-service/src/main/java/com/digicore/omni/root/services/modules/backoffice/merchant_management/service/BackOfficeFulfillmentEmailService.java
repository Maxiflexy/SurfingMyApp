package com.digicore.omni.root.services.modules.backoffice.merchant_management.service;

import com.digicore.notification.lib.request.FulfillmentNotificationRequest;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;

import com.digicore.omni.data.lib.modules.backoffice.model.BackOfficeProfile;
import com.digicore.omni.data.lib.modules.merchant.model.*;
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


@Service
@RequiredArgsConstructor
public class BackOfficeFulfillmentEmailService {
    //todo refactor this class

    @Value( "${omni.root.mail.approval-update.subject:Approval Update}")
    private String approvalUpdateSubject;

    @Value( "${omni.root.mail.decline-update.subject:Decline Update}")
    private String declineUpdateSubject;

    private final NotificationDispatcher notificationDispatcher;

    private final FulfillmentRetrieverService fulfillmentRetrieverService;

    public void sendMerchantApprovalEmail(String merchantId) {
        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .notificationSubject(approvalUpdateSubject)
                .recipients(List.of(fulfillmentRetrieverService.getMerchantProfile(merchantId).getEmail()))
                .firstName(fulfillmentRetrieverService.getMerchantProfile(merchantId).getFirstName())
                .notificationRequestType(NotificationRequestType.SEND_MERCHANT_APPROVAL_EMAIL)
                .build());
    }

    public void sendMerchantDeclineEmail(String merchantId){
        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .firstName(fulfillmentRetrieverService.getMerchantProfile(merchantId).getFirstName())
                .notificationSubject(declineUpdateSubject)
                .recipients(List.of(fulfillmentRetrieverService.getMerchantProfile(merchantId).getEmail()))
                .fulfillmentNotificationRequest(FulfillmentNotificationRequest.builder()
                        .declineComment(fulfillmentRetrieverService.getMerchantFulfillment(merchantId)
                                .getAnalystDeclineComments().get(0).getComment()).build())
                .notificationRequestType(NotificationRequestType.SEND_MERCHANT_DECLINE_EMAIL)
                .build());
    }

    public void sendFulfillmentAnalystMerchantDeclineEmail(String merchantId) {

        String roleName = "FULFILLMENT_ANALYST";
        MerchantFulfillment fulfillment = fulfillmentRetrieverService.retrieveMerchantFulfillment(merchantId);
        String franchiseRiskApprovalName = fulfillmentRetrieverService.fetchBackOfficeProfile().getFirstName().concat(" ").concat(fulfillmentRetrieverService.fetchBackOfficeProfile().getLastName());

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

        List<String> analystEmails = retrieveAllAnalystEmails(roleName);
        List<String> analystFirstNames = retrieveAllAnalystFirstName(roleName);

        Map<String, String> firstNameToEmailMap = IntStream.range(0, analystFirstNames.size())
                .boxed()
                .collect(Collectors.toMap(analystFirstNames::get, analystEmails::get));

        IntStream.range(0, analystFirstNames.size())
                .forEach(i -> {
                    String firstName = analystFirstNames.get(i);
                    String emailRecipient = firstNameToEmailMap.get(firstName);

                    notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                            .notificationSubject(declineUpdateSubject)
                            .recipients(Collections.singletonList(emailRecipient))
                            .fulfillmentNotificationRequest(FulfillmentNotificationRequest.builder()
                                    .merchantName(fulfillmentRetrieverService.getMerchantProfile(merchantId).getMerchantBusinessDetails().getBusinessName())
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
                                    .matchStatus(fulfillment.getProfileCheckMatch())
                                    .franchiseRiskApproval(franchiseRiskApprovalName)
                                    .declineComment(fulfillment.getApprovalDeclineComments().get(0).getComment())
                                    .build())
                            .firstName(firstName)
                            .notificationRequestType(NotificationRequestType.SEND_FULFILLMENT_ANALYST_DECLINE_UPDATE_EMAIL)
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
}
