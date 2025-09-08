package com.digicore.omni.root.services.modules.merchants.submerchant.controller;

import com.digicore.omni.data.lib.modules.merchant.enums.MerchantStatus;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.submerchant.facade.SubMerchantFacade;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantInviteRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantOnboardRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.service.SubMerchantService;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@RestController
@RequestMapping("/api/v1/sub-merchant")
@RequiredArgsConstructor
public class SubMerchantController {
    private final SubMerchantFacade subMerchantFacade;
    private final SubMerchantService subMerchantService;

    @PostMapping("/invite")
    @TokenValid()
    public ResponseEntity<Object> inviteSubMerchant(@Valid @RequestBody SubMerchantInviteRequest request){
        return CommonUtils.buildSuccessResponse(subMerchantFacade.subMerchantInvite(request));
    }


    @LogActivity(activity = LogActivityType.CREATE_ACTIVITY)
    @PostMapping("/complete-onboarding")
    public ResponseEntity<Object> onboardSubMerchant(@Valid @RequestBody SubMerchantOnboardRequest request) {
        return subMerchantFacade.registerSubMerchant(request);
    }


    @GetMapping("/all")
    @TokenValid()
    @Operation(
            summary = "Get All SubMerchants",
            description = "Retrieve all submerchants for the current merchant with pagination, search and filter options."
    )
    public ResponseEntity<Object> getAllSubMerchants(
            @Parameter(description = "Page number (0-based, default: 0)", example = "0")
            @Min(0)
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size (default: 16, max: 20)", example = "20")
            @Max(20)
            @RequestParam(defaultValue = "20") int pageSize,
            @Parameter(description = "Search term for business name or sub-merchant name", example = "John")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by merchant status", example = "ACTIVE || INACTIVE || PENDING_KYC")
            @RequestParam(required = false) MerchantStatus status) {

        return CommonUtils.buildSuccessResponse(subMerchantService.getAllSubMerchants(
                PageRequest.of(pageNumber, pageSize), search, status));
    }

    @GetMapping("/invites")
    @TokenValid()
    @Operation(
            summary = "Get Pending SubMerchant Invites",
            description = "Retrieve all pending submerchant invites that haven't completed onboarding."
    )
    public ResponseEntity<Object> getPendingInvites(
            @Parameter(description = "Page number (0-based, default: 0)", example = "0")
            @Min(0)
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(description = "Page size (default: 16, max: 20)", example = "20")
            @Max(20)
            @RequestParam(defaultValue = "20") int pageSize) {

        return CommonUtils.buildSuccessResponse(subMerchantService.getPendingInvites(
                PageRequest.of(pageNumber, pageSize)));
    }


    @GetMapping("/{subMerchantId}")
    @TokenValid()
    @Operation(
            summary = "Get SubMerchant by Merchant ID",
            description = "Retrieve a specific submerchant by their merchant ID (e.g., SME-123456). " +
                    "Only returns submerchants belonging to the current logged-in merchant."
    )
    public ResponseEntity<Object> getSubMerchantByMerchantId(
            @Parameter(description = "SubMerchant Merchant ID", required = true, example = "SME-123456")
            @PathVariable String subMerchantId) {

        return CommonUtils.buildSuccessResponse(subMerchantService.getSubMerchantByMerchantId(subMerchantId));
    }

    @GetMapping("/overview")
    @TokenValid()
    @Operation(
            summary = "Get SubMerchants Overview",
            description = "Retrieve a comprehensive overview of all submerchants including total count, " +
                    "total collections, card collections, and web/POS collections breakdown by payment tokens."
    )
    public ResponseEntity<Object> getSubMerchantOverview() {
        return CommonUtils.buildSuccessResponse(subMerchantService.getSubMerchantOverview());
    }
}