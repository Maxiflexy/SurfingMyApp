package com.digicore.omni.root.services.modules.common.utils;


import com.digicore.omni.data.lib.modules.merchant.model.Transaction;
import com.digicore.omni.root.services.modules.common.apiModels.TransactionResponseApiModel;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.format.DateTimeFormatter;


@Component
public class TransactionUtils {


    public static String getMerchantByEmail(Principal principal){
        return principal.getName();
    }


    public static TransactionResponseApiModel mapTransactionToTransactionResponseApiModel(Transaction transaction){
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("E, dd-MMMM-yyyy HH:mm");
            return TransactionResponseApiModel.builder()
                .transactionId(transaction.getTransactionId())
                .transactionStatus(String.valueOf(transaction.getTransactionStatus()))
                .dateTime(dateFormat.format(transaction.getCreatedOn()))
                .payerName(transaction.getCustomer().getFirstName() + " " + transaction.getCustomer().getLastName())
                .amount(transaction.getAmountInMinor())
                .Channel(transaction.getPaymentChannel().toString())
                .build();
    }
}
