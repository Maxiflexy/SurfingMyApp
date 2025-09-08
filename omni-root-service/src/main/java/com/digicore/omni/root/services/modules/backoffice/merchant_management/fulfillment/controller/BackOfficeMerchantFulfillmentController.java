package com.digicore.omni.root.services.modules.backoffice.merchant_management.fulfillment.controller;

import com.digicore.omni.data.lib.modules.merchant.dto.ManualMerchantFulfillmentDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantFulfillmentDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.compliance.ComplianceFileUploadRequest;
import com.digicore.omni.root.lib.modules.fulfillment.response.FulfillmentValidationResult;
import com.digicore.omni.root.services.modules.backoffice.merchant_management.fulfillment.service.BackOfficeMerchantFulfillmentService;
import com.digicore.omni.root.services.modules.backoffice.merchant_management.service.BackOfficeFulfillmentEmailService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backoffice-fulfillment/process/")
public class BackOfficeMerchantFulfillmentController {

    private final BackOfficeMerchantFulfillmentService backOfficeMerchantFulfillmentService;

    private final BackOfficeFulfillmentEmailService backOfficeFulfillmentEmailService;

    @TokenValid()
    @PostMapping("send-merchant-fulfillment-for-validation")
    public ResponseEntity<Object> sendMerchantFulfillmentVerification(@RequestBody MerchantFulfillmentDTO merchantFulfillmentDTO) {

        return CommonUtils.buildSuccessResponse(backOfficeMerchantFulfillmentService.validateAllIdAndSave(merchantFulfillmentDTO));

    }

    @TokenValid()
    @PostMapping("save-merchant-fulfillment")
    public ResponseEntity<Object> saveMerchantFulfillmentVerification(@RequestBody FulfillmentValidationResult merchantFulfillmentDTO) {
        backOfficeMerchantFulfillmentService.saveAllIdAndSave(merchantFulfillmentDTO);
        return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @GetMapping("fetch-merchant-fulfillment-summary")
    public ResponseEntity<Object> fetchBackofficeMerchantFulfillmentSummary(
            @RequestParam(value = "merchantId") String merchantId) {

        return CommonUtils.buildSuccessResponse(backOfficeMerchantFulfillmentService.retrieveBackofficeMerchantFulfillmentSummary(merchantId));

    }

    @TokenValid()
    @PostMapping("update-failed-merchant-fulfillment")
    public ResponseEntity<Object> updateFailedMerchantFulfillment(@RequestBody ManualMerchantFulfillmentDTO fulfillmentDTO) {
        backOfficeMerchantFulfillmentService.updateFailedMerchantFulfillment(fulfillmentDTO);
        return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @PostMapping("decline-merchant-fulfillment")
    public ResponseEntity<Object> declineMerchantFulfillment(@RequestBody MerchantFulfillmentDTO declineDTO) {
        backOfficeMerchantFulfillmentService.declineMerchantFulfillment(declineDTO);
        backOfficeFulfillmentEmailService.sendMerchantDeclineEmail(declineDTO.getMerchantId());
        return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @PostMapping("upload-merchant-fulfillment-files")
    public ResponseEntity<Object> uploadMerchantComplianceFile(@RequestParam String merchantId, @ModelAttribute ComplianceFileUploadRequest request) {
        backOfficeMerchantFulfillmentService.uploadBackofficeFulfillmentFile(merchantId, request);
        return CommonUtils.buildSuccessResponse();
    }

}
