package com.digicore.omni.root.services.modules.backoffice.settings.feeconfiguration.service;


import com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration;
import com.digicore.omni.data.lib.modules.backoffice.dto.PaymentProcessorDTO;
import com.digicore.omni.data.lib.modules.common.enums.FeeConfigurationType;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeFeeConfigurationService;
import com.digicore.omni.root.services.config.PropertyConfig;
import com.digicore.request.processor.annotations.MakerChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ibrahim Lawal
 * @createdOn 01/October/2023
 */

@Service
@RequiredArgsConstructor
public class FeeConfigurationService {

    private final BackOfficeFeeConfigurationService backOfficeFeeConfigurationService;

    private final PropertyConfig propertyConfig;

    @MakerChecker(checkerPermission = "approve-or-decline-fee-config-update", makerPermission = "update-fee-configuration", requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration")
    public void updateGeneralFeeConfiguration(Object request, Object[] pendingFileDTOs) {
        backOfficeFeeConfigurationService.updateGeneralFeeConfiguration((BaseWebFeeConfiguration) request);
    }


    public List<BaseWebFeeConfiguration> getFeeConfiguration(FeeConfigurationType feeConfigurationType) {
        return backOfficeFeeConfigurationService.getFeeConfiguration(feeConfigurationType);
    }


    public List<PaymentProcessorDTO> getPaymentProcessors(){
        List<PaymentProcessorDTO> processorDTOList = backOfficeFeeConfigurationService.getPaymentProcessors();
        processorDTOList.forEach(paymentProcessorDTO -> {
            switch (paymentProcessorDTO.getFeeConfigurationType()){
                case WEB_CARD -> paymentProcessorDTO.setCurrencies(propertyConfig.getWebCardCurrency());
                case WEB_QR -> paymentProcessorDTO.setCurrencies(propertyConfig.getWebQRCurrency());
                case WEB_TRANSFER -> paymentProcessorDTO.setCurrencies(propertyConfig.getWebTransferCurrency());
                case WEB_USSD -> paymentProcessorDTO.setCurrencies(propertyConfig.getWebUSSDCurrency());
                case POS_CARD -> paymentProcessorDTO.setCurrencies(propertyConfig.getPosCardCurrency());
                case POS_QR -> paymentProcessorDTO.setCurrencies(propertyConfig.getPosQRCurrency());
                case POS_TRANSFER -> paymentProcessorDTO.setCurrencies(propertyConfig.getPosTransferCurrency());
                case POS_USSD -> paymentProcessorDTO.setCurrencies(propertyConfig.getPosUSSDCurrency());
                case API_CARD -> paymentProcessorDTO.setCurrencies(propertyConfig.getApiCardCurrency());
                case API_QR -> paymentProcessorDTO.setCurrencies(propertyConfig.getApiQRCurrency());
                case API_TRANSFER -> paymentProcessorDTO.setCurrencies(propertyConfig.getApiTransferCurrency());
                case API_USSD -> paymentProcessorDTO.setCurrencies(propertyConfig.getApiUSSDCurrency());
            }
        });
        return processorDTOList;
    }
}
