package com.digicore.omni.root.services.modules.backoffice.settings.feeconfiguration.controller;


import com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration;
import com.digicore.omni.data.lib.modules.common.enums.FeeConfigurationType;
import com.digicore.omni.root.services.modules.backoffice.settings.feeconfiguration.service.FeeConfigurationService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author: Monsuru <br/>
 * Created on 30/08/2022 22:07
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings/fee-configuration/process")
public class FeeConfigurationController {

    private final FeeConfigurationService feeConfigurationService;

    @TokenValid()
    @PostMapping()
    public ResponseEntity<Object> updateFeeConfig(@Valid @RequestBody BaseWebFeeConfiguration baseWebFeeConfiguration) {
        feeConfigurationService.updateGeneralFeeConfiguration(baseWebFeeConfiguration, null);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @GetMapping("get-{feeConfigurationType}-details")
    public ResponseEntity<Object> getFeeConfig(@PathVariable FeeConfigurationType feeConfigurationType) {
        return CommonUtils.buildSuccessResponse(feeConfigurationService.getFeeConfiguration(feeConfigurationType));
    }

    @TokenValid()
    @GetMapping("get-payment-processors")
    public ResponseEntity<Object> getPaymentProcessors(){
        return CommonUtils.buildSuccessResponse(feeConfigurationService.getPaymentProcessors());
    }

//    @TokenValid()
//    @GetMapping("get-recent-payment-processors")
//    public ResponseEntity<Object> getRecentPaymentProcessors(){
//        return CommonUtils.buildSuccessResponse(backOfficeFeeConfigurationService.getRecentFeeConfiguration());
//    }
}
