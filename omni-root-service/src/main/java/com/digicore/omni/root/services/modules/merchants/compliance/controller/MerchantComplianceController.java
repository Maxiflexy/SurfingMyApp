package com.digicore.omni.root.services.modules.merchants.compliance.controller;


import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.data.lib.modules.merchant.dto.compliance.*;
import com.digicore.omni.root.lib.modules.fulfillment.service.validation.AccountNumberValidationService;
import com.digicore.omni.root.lib.modules.fulfillment.service.validation.impl.smile.request.BankAccountValidationRequest;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.compliance.service.MerchantComplianceService;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


/**
 * @author Monsuru <br/>
 * @since Sep-07(Wed)-2022
 */
@RestController
@RequestMapping("/api/v1/merchant-compliance/process/")
@Tag(name = "Merchant-Compliance-Controller",
        description = "Under this controller contains all the endpoints used to submit and upload merchant compliance information",
        externalDocs = @ExternalDocumentation(description = "New Update !!!", url = "https://docs.google.com/document/d/143Pe1kOt-YsEFAZP6z8OIf0p46nONQ_fao2ixnK5mU0"))

public class MerchantComplianceController {

    private final @Qualifier("sofri") AccountNumberValidationService accountNumberValidationService;
    private final MerchantService merchantService;
    private final MerchantComplianceService merchantComplianceService;

    @Value("${makerchecker:false}")
    private boolean makerCheckerActive;


    public MerchantComplianceController(
            @Qualifier("sofri") AccountNumberValidationService accountNumberValidationService,
            MerchantService merchantService,
            MerchantComplianceService merchantComplianceService) {
        this.accountNumberValidationService = accountNumberValidationService;
        this.merchantService = merchantService;
        this.merchantComplianceService = merchantComplianceService;
    }


    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("starter-merchant-personal-information-compliance")
    @Operation(summary = "This endpoint is new, check it out",description = "This endpoints is used to submit merchant of type starter information, in order to get a success response, the required compliance documents must have been uploaded")

            @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> updateStarterMerchantPersonalInformationCompliance(@Valid @RequestBody StarterPersonalInformationComplianceRequest request )  {
            return CommonUtils.buildSuccessResponse(merchantComplianceService.updateStarterMerchantPersonalInformationCompliance(request));

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("registered-merchant-personal-information-compliance")
    @Operation(summary = "This endpoint is new, check it out",description = "This endpoints is used to submit merchant of type registered business information, in order to get a success response, the required compliance documents must have been uploaded")

    @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> updateRegisteredMerchantPersonalInformationCompliance(@Valid @RequestBody RegisteredBusinessPersonalInformationComplianceRequest request )  {
        return CommonUtils.buildSuccessResponse(merchantComplianceService.updateRegisteredMerchantPersonalInformationCompliance(request));

    }

    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("government-merchant-personal-information-compliance")
    @Operation(summary = "This endpoint is new, check it out",description = "This endpoints is used to submit merchant of type government information, in order to get a success response, the required compliance documents must have been uploaded")

    @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> updateGovernmentMerchantPersonalInformationCompliance(@Valid @RequestBody GovernmentPersonalInformationComplianceRequest request )  {
        return CommonUtils.buildSuccessResponse(merchantComplianceService.updateGovernmentMerchantPersonalInformationCompliance(request));

    }

    @PostMapping(path = "upload-merchant-compliance-files")
    @Operation(summary = "This endpoint has been modified, check it out",description = "This endpoints is used to upload compliance documents, sadly this endpoint can't be tried out on swagger yet, however it works fine on postman")

    @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> uploadMerchantComplianceFiles(@ModelAttribute ComplianceFileUploadRequest request)  {
            merchantComplianceService.uploadMerchantComplianceFiles(request);
            return CommonUtils.buildSuccessResponse();

    }

    @TokenValid()
    @PostMapping("update-merchant-business-information-compliance")
    @Operation(summary = "This endpoint has been modified, check it out",description = "This endpoints is used to submit merchant business information, in order to get a success response, the required compliance documents must have been uploaded")

    @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> updateMerchantBusinessInformationCompliance( @Valid @RequestBody BusinessInformationComplianceRequest request)  {

            return CommonUtils.buildSuccessResponse(merchantComplianceService.updateBusinessInformationCompliance(request));

    }

   // @PreAuthorize("hasAuthority('edit-merchant-bank-information')")
    @TokenValid()
    //@LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PostMapping("update-merchant-bank-account-information-compliance")
    public ResponseEntity<Object> updateMerchantBankAccountInformationCompliance(@RequestBody BankAccountInformationComplianceRequest request)  {

            return CommonUtils.buildSuccessResponse(merchantComplianceService.updateMerchantBankAccountInformationCompliance(request));

    }

    @TokenValid()
    @GetMapping("get-merchant-compliance")
    public ResponseEntity<Object> getMerchantCompliance(Principal principal)  {

            return CommonUtils.buildSuccessResponse(merchantService.fetchUserByEmail(principal.getName()));

    }
    @TokenValid()
    @GetMapping("get-compliance-categories")
    public ResponseEntity<Object> getComplianceCategory()  {
        return CommonUtils.buildSuccessResponse(merchantService.fetchComplianceCategory("compliance-category"));
    }

    @GetMapping("supported-document-types")
    @Operation(summary = "This endpoint has been modified",description = "This endpoints is used to fetch the supported document types for compliance")

    @ApiResponse(responseCode = "200", description = "This means your request was successfully treated",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseJson.class)) })
    public ResponseEntity<Object> getAllDocumentType(){
        return merchantComplianceService.getSupportedDocumentTypes();
    }

    @TokenValid()
    @GetMapping("get-bank-institution-code")
    public ResponseEntity<Object> getAllBankInstitutionCode()  {

            return CommonUtils.buildSuccessResponse(merchantComplianceService.getBankInstitutionCodes());

    }

    @PostMapping("validate-account-number")
    public ResponseEntity<Object> accountNumberVerification(@Valid @RequestBody BankAccountValidationRequest request) {
       return CommonUtils.buildSuccessResponse(accountNumberValidationService.validateAccountNumber(request));
    }

}
