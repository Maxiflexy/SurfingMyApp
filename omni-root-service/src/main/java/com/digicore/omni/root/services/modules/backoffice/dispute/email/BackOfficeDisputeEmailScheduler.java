package com.digicore.omni.root.services.modules.backoffice.dispute.email;

import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;


import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.enums.DisputeStatus;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantTransactionService;
import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BackOfficeDisputeEmailScheduler {


    private final ScheduledExecutorService scheduler;

    private final MerchantService merchantService;

    private final MerchantTransactionService merchantTransactionService;


    private final NotificationDispatcher notificationDispatcher;

    @Value("${omni.root.mail.transaction.dispute:Urgent: Pending Transaction Dispute}")
    private String transactionDispute;

    @Value("${dispute.remind-interval:4}")
    private int remindInterval;

    @Value("${dispute.remind-till:20}")
    private int remindUntil;

    @Async
    public void scheduleDisputeEmail(MerchantTransaction merchantTransaction) {

        MerchantProfileApiModel merchantProfileApiModel = merchantService.fetchUserById(merchantTransaction.getMerchantId());

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                MerchantTransaction merchantTransactionCheck = merchantTransactionService
                        .fetchMerchantTransactionByTransactionReference(merchantTransaction.getTransactionReference());
                if (merchantTransactionCheck.getDisputeStatus().equals(DisputeStatus.DISPUTE_OPEN)) {
                    notificationDispatcher.dispatchEmail((NotificationServiceRequest.builder()
                            .firstName(merchantProfileApiModel.getFirstName())
                            .recipients(List.of(merchantProfileApiModel.getEmail()))
                            .notificationSubject(transactionDispute)
                            .notificationRequestType(NotificationRequestType.SEND_MERCHANT_TRANSACTION_DISPUTE_EMAIL)
                            .build()));
                }
            }
        }, 0, remindInterval, TimeUnit.HOURS);

        try {
            if (!scheduler.awaitTermination(remindUntil, TimeUnit.HOURS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

}
