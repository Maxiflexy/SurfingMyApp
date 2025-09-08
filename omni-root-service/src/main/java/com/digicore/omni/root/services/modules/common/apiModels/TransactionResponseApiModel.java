package com.digicore.omni.root.services.modules.common.apiModels;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TransactionResponseApiModel {

    private String transactionId;
    private String payerName;
    private String Channel;
    private String dateTime;
    private String transactionStatus;
    private String amount;
}
