/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.kyc.controller;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.file.contract.DocumentUploadService;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantKycProfileResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(API_V1 + KYC_API_FEIGN)
@Tag(name = KYC_CONTROLLER_TITLE_FEIGN, description = KYC_CONTROLLER_DESCRIPTION)
public class MerchantProfileKycFeignController {

  private final ProfileService merchantKycProfileService;
  private final DocumentUploadService<List<FileUploadedDTO>, MerchantKycProfileDocumentUploadDTO>
          merchantKycProfileDocumentUploadService;

  @PostMapping()
  @Operation(summary = KYC_CONTROLLER_UPDATE_TITLE, description = KYC_CONTROLLER_UPDATE_DESCRIPTION)
  public ResponseEntity<Object> updateKyc(
          @RequestParam("profileId") String profileId,
          @Valid @RequestBody MerchantKycProfileResponse merchantKycProfileOnboardingRequest) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileService.updateProfile(merchantKycProfileOnboardingRequest, profileId),
            "Merchant KYC Profile Updated Successfully");
  }

  @GetMapping()
  @Operation(summary = KYC_CONTROLLER_GET_TITLE, description = KYC_CONTROLLER_GET_DESCRIPTION)
  public ResponseEntity<Object> getProfile(
          @RequestParam("profileId") String profileId) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileService.getProfileById(profileId),
            "Merchant KYC Profile Retrieved Successfully");
  }

  @PostMapping(DOCUMENT_API)
  @Operation(summary = UPLOAD_DOCUMENT_TITLE, description = UPLOAD_DOCUMENT_DESCRIPTION)
  public ResponseEntity<Object> uploadDocuments(
          @RequestParam("profileId") String profileId,
          @ModelAttribute MerchantKycProfileDocumentUploadDTO request) {
    return ControllerResponse.buildSuccessResponse(
            merchantKycProfileDocumentUploadService.uploadMultipleDocument(request,profileId));
  }


  @PatchMapping("/status")
  @Operation(
          summary = "Toggle Merchant Profile Status",
          description = "Updates the merchant profile status between ACTIVE and INACTIVE")
  public ResponseEntity<Object> toggleProfileStatus(
          @RequestParam("profileId") String profileId,
          @RequestParam("enabled") boolean enabled) {

    ProfileStatus newStatus = enabled ? ProfileStatus.ACTIVE : ProfileStatus.INACTIVE;
    merchantKycProfileService.updateProfileStatus(profileId, newStatus);

    String statusMessage = enabled ? "activated" : "deactivated";
    return ControllerResponse.buildSuccessResponse(
            null,
            String.format("Merchant profile %s successfully", statusMessage));
  }
}
