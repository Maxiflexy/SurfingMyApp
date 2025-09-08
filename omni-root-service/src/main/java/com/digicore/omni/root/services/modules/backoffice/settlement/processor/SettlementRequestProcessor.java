package com.digicore.omni.root.services.modules.backoffice.settlement.processor;

import com.digicore.omni.root.services.modules.backoffice.settlement.service.BackOfficeSettlementService;
import com.digicore.request.processor.annotations.RequestHandler;
import com.digicore.request.processor.annotations.RequestType;
import com.digicore.request.processor.enums.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/**
 * @author Ibrahim Lawal
 * @createdOn 13/November/2023
 */

@RequestHandler(type = RequestHandlerType.SETTLEMENT_PROCESS_REQUEST)
@RequiredArgsConstructor
public class SettlementRequestProcessor {

    private final BackOfficeSettlementService backOfficeSettlementService;

    @RequestType(name = "processDueMerchantSettlement")
    public void processDueMerchantSettlement(Object request){
        backOfficeSettlementService.processDueMerchantSettlement(request, null);
    }

}
