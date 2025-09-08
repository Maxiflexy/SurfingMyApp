/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.kyc.controller;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.file.contract.DocumentUploadService;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantKycProfileResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.kyc.KycSwaggerDoc.*;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-11(Mon)-2025
 */
@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + BACKOFFICE_KYC_API)
@Tag(name = BACKOFFICE_KYC_CONTROLLER_TITLE_, description = KYC_CONTROLLER_DESCRIPTION)
public class MerchantProfileKycFeignController {

  private final ProfileService merchantKycProfileService;
  private final DocumentUploadService<List<FileUploadedDTO>, MerchantKycProfileDocumentUploadDTO>
          merchantKycProfileDocumentUploadService;

  @PostMapping()
  @Operation(summary = KYC_CONTROLLER_UPDATE_TITLE, description = KYC_CONTROLLER_UPDATE_DESCRIPTION)
  public ResponseEntity<Object> updateKyc(
          @RequestParam("merchantId") String merchantId,
          @Valid @RequestBody MerchantKycProfileResponse merchantKycProfileOnboardingRequest) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileService.updateProfile(merchantKycProfileOnboardingRequest, merchantId),
            "Merchant KYC Profile Updated Successfully");
  }

  @GetMapping()
  @Operation(summary = KYC_CONTROLLER_GET_TITLE, description = KYC_CONTROLLER_GET_DESCRIPTION)
  public ResponseEntity<Object> getProfile(
          @RequestParam("merchantId") String merchantId) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileService.getProfileById(merchantId),
            "Merchant KYC Profile Retrieved Successfully");
  }

  @PostMapping(DOCUMENT_API)
  @Operation(summary = UPLOAD_DOCUMENT_TITLE, description = UPLOAD_DOCUMENT_DESCRIPTION)
  public ResponseEntity<Object> uploadDocuments(
          @RequestParam("merchantId") String merchantId,
          @ModelAttribute MerchantKycProfileDocumentUploadDTO request) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileDocumentUploadService.uploadMultipleDocument(request,merchantId));
  }


  @PostMapping(PROFILE_STATUS_UPDATE_API)
  @Operation(summary = UPDATE_PROFILE_STATUS_TITLE, description = UPDATE_PROFILE_STATUS_DESCRIPTION)
  public ResponseEntity<Object> toggleProfileStatus(
          @Parameter(description = "The unique identifier of the merchant profile", example = "S-123456", required = true)
          @RequestParam("merchantId") String merchantId,
          @Parameter(description = "Enable or disable the merchant profile", example = "true", required = true)
          @RequestParam("enabled") boolean enabled) {
    merchantKycProfileService.updateProfileStatus(merchantId, enabled);
    return ControllerResponse.buildSuccessResponse(
            null,
            String.format("Merchant profile %s successfully", enabled ? "activated" : "deactivated"));
  }
}
