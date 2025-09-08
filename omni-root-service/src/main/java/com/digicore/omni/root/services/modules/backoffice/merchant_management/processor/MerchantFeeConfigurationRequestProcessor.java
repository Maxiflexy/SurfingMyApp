package com.digicore.omni.root.services.modules.backoffice.merchant_management.processor;

import com.digicore.omni.root.services.modules.backoffice.merchant_management.service.MerchantManagementComplianceService;
import com.digicore.request.processor.annotations.RequestHandler;
import com.digicore.request.processor.annotations.RequestType;
import com.digicore.request.processor.enums.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/**
 * @author Ibrahim Lawal
 * @createdOn 01/October/2023
 */

@RequestHandler(type = RequestHandlerType.FEE_CONFIGURATION_REQUEST)
@RequiredArgsConstructor
public class MerchantFeeConfigurationRequestProcessor {

    private final MerchantManagementComplianceService merchantManagementComplianceService;

    @RequestType(name = "updateMerchantFeeConfiguration")
    public void updateMerchantFeeConfiguration(Object request) {
        merchantManagementComplianceService.updateMerchantFeeConfiguration(request, null);
    }
}
