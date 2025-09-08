package com.digicore.omni.root.services.modules.backoffice.settings.feeconfiguration.processor;

import com.digicore.omni.root.services.modules.backoffice.settings.feeconfiguration.service.FeeConfigurationService;
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
public class FeeConfigurationRequestProcessor {

    private final FeeConfigurationService feeConfigurationService;


    @RequestType(name = "updateGeneralFeeConfiguration")
    public void updateGeneralFeeConfiguration(Object request) {
        feeConfigurationService.updateGeneralFeeConfiguration(request, null);
    }
}
