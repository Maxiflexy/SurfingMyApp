/*
 * Created by Monsuru (19/9/2022)
 */

package com.digicore.omni.root.services.modules.merchants.settings.controller;

import com.digicore.omni.data.lib.modules.common.dtos.UpdatePasswordDTO;
import com.digicore.omni.data.lib.modules.common.enums.TransactionFeeCharge;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentRailsDTO;

import com.digicore.omni.data.lib.modules.merchant.dto.compliance.ComplianceFileUploadRequest;
import com.digicore.omni.data.lib.modules.merchant.dto.settings.MerchantBusinessProfileUpdateDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.settings.MerchantUserProfileUpdateDTO;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.settings.service.SettingsService;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Monsuru <br/>
 * @since Sep-19(Mon)-2022
 */
@RestController
@RequestMapping("/api/v1/merchant-information/process/")
public class SettingsController {

    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @TokenValid()
    @GetMapping("get-merchant-profile")
    public ResponseEntity<Object> getMerchantProfile(Principal principal)  {
            return CommonUtils.buildSuccessResponse(settingsService.getMerchantProfile(principal));

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-merchant-user-profile")
    public ResponseEntity<Object> updateMerchantUserProfile(@RequestBody MerchantUserProfileUpdateDTO merchantUserProfileUpdateDTO,
                                                         Principal principal)  {
            return CommonUtils.buildSuccessResponse(settingsService.updateMerchantUserProfile(merchantUserProfileUpdateDTO, principal));

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-merchant-business-profile")
    public ResponseEntity<Object> updateMerchantBusinessProfile(@RequestBody MerchantBusinessProfileUpdateDTO merchantBusinessProfileUpdateDTO,
                                                                 Principal principal)  {
            return CommonUtils.buildSuccessResponse(settingsService.updateMerchantBusinessProfile(merchantBusinessProfileUpdateDTO, principal));

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-password")
    public ResponseEntity<Object> updateAccountPassword( @Valid @RequestBody UpdatePasswordDTO recoverPasswordDTO)  {

            settingsService.updateAccountPassword(recoverPasswordDTO);
            return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("toggle-2fa-status")
    public ResponseEntity<Object> toggle2Fa()  {
        return CommonUtils.buildSuccessResponse(settingsService.toggle2faStatus());
    }

    @TokenValid()
    @GetMapping("get-2fa-status")
    public ResponseEntity<Object> fetch2faLoginStatus()  {
        return CommonUtils.buildSuccessResponse(settingsService.fetch2faLoginStatus());
    }




    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-merchant-profile-picture")
    public ResponseEntity<Object> updateMerchantProfilePicture(@RequestParam String merchantId, @ModelAttribute ComplianceFileUploadRequest request)  {
        settingsService.updateMerchantProfilePicture(merchantId, request);
        return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-fee-bearer")
    public ResponseEntity<Object> updateMerchantFeeBearer(@RequestParam TransactionFeeCharge transactionFeeCharge)  {
        settingsService.updateMerchantFeeBearer(transactionFeeCharge);
        return CommonUtils.buildSuccessResponse();

    }


    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("update-payment-rail")
    public ResponseEntity<Object> updateMerchantPaymentRails(@Valid @RequestBody PaymentRailsDTO paymentRailsDTO)  {
     return    settingsService.updateMerchantPaymentRails(paymentRailsDTO);


    }




    @TokenValid()
    @GetMapping("get-merchant-payment-rail")
    public ResponseEntity<Object> getMerchantPaymentRails()  {
       return CommonUtils.buildSuccessResponse(settingsService.getMerchantPaymentRails());


    }


    @TokenValid()
    @GetMapping("get-default-payment-rail")
    public ResponseEntity<Object> getAllPaymentRails()  {
        return CommonUtils.buildSuccessResponse(settingsService.getAllPaymentRails());


    }


    @PostMapping("apple-verify-domain")
    public ResponseEntity<Object> appleVerifyDomain(@RequestBody String requestBody) throws Exception {

        System.out.println(requestBody);

      return    settingsService.apple_verify_domain(requestBody);

    }

}
