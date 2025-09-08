package com.digicore.omni.root.services.modules.backoffice.settings.general_settings;

import com.digicore.omni.root.lib.modules.common.services.GeneralSettingService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Apr-01(Sat)-2023
 */

@RestController
@RequestMapping("/api/v1/settings/general-configuration/process/")
@RequiredArgsConstructor
public class GeneralSettingsController {

    private final GeneralSettingService generalSettingService;
//    @TokenValid()
//    @GetMapping("fetch-all-settings")
//    public ResponseEntity<Object> getAllSettings() {
//        return CommonUtils.buildSuccessResponse(generalSettingService.fetchMerchantValidationSettingPaged());
//    }

    @TokenValid()
    @GetMapping("fetch-merchant-validation-status")
    public ResponseEntity<Object> getMerchantValidationStatus() {
        return CommonUtils.buildSuccessResponse(generalSettingService.fetchMerchantValidationSetting());
    }

    @TokenValid()
    @PatchMapping("update-merchant-validation-status")
    public ResponseEntity<Object> updateMerchantValidationStatus(@RequestParam("isAutomatic")boolean isAutomatic) {
        return CommonUtils.buildSuccessResponse(generalSettingService.updateMerchantValidationSetting(isAutomatic));
    }

    @TokenValid()
    @GetMapping("fetch-all-settings")
    public ResponseEntity<Object> getAllSettings() {
        return CommonUtils.buildSuccessResponse(generalSettingService.fetchAllSettingPaged());
    }

    @TokenValid()
    @GetMapping("fetch-afrigo-processor-status")
    public ResponseEntity<Object> getAfrigoProcessorStatus() {
        return CommonUtils.buildSuccessResponse(generalSettingService.fetchAfrigoProcessorSetting());
    }


    @TokenValid()
    @PatchMapping("update-afrigo-processor-status")
    public ResponseEntity<Object> updateAfrigoProcessorStatus(@RequestParam("isActivated")boolean isActivated) {
        return CommonUtils.buildSuccessResponse(generalSettingService.updateAfrigoProcessorSetting(isActivated));
    }
}
