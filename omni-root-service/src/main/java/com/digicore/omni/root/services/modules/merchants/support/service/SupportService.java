package com.digicore.omni.root.services.modules.merchants.support.service;

import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.root.services.config.NotificationEmailPropertyForSupportConfig;
import com.digicore.omni.root.services.util.SupportServiceRequest;
import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.digicore.omni.root.services.util.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportService {


    private final NotificationDispatcher notificationDispatcher;

    private final NotificationEmailPropertyForSupportConfig config;


    public Map<String ,String> getSupportEmail(){
            Map<String, String> subjectPerMail = new HashMap<>();
            subjectPerMail.put(ONBOARDING, config.getOnboarding());
            subjectPerMail.put(PROFILE_ACTIVATION, config.getProfileActivation());
            subjectPerMail.put(TRANSACTIONS, config.getTransactions());
            subjectPerMail.put(DISPUTE_AND_REFUNDS, config.getDisputeAndRefunds());
            subjectPerMail.put(SETTLEMENT , config.getSettlement());
            subjectPerMail.put(GENERAL_ENQUIRIES, config.getGeneralEnquiries());
            return subjectPerMail;
        }

        public void sendSupportMail(SupportServiceRequest request) {
            log.trace("about to send email to the support team");

            notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                            .notificationSubject(request.getSubject())
                            .recipients(List.of(request.getReceiver()))
                            .notificationContent(modifyMailContent(request.getBody(), request.getSender()))
                            .notificationRequestType(NotificationRequestType.SEND_SUPPORT_EMAIL)
                    .build());

            log.trace("sent successful to rabbit server {}", request.getSender());
        }


    private String modifyMailContent(String body, String senderEmail){
       return  body.concat(SEPARATORS).concat(senderEmail);
    }
}
