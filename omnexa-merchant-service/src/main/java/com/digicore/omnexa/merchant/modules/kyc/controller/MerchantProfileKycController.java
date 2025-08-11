/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.kyc.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ID_PLACEHOLDER;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.kyc.KycSwaggerDoc.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.file.contract.DocumentUploadService;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantKycProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-04(Mon)-2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + KYC_API)
@Tag(name = KYC_CONTROLLER_TITLE, description = KYC_CONTROLLER_DESCRIPTION)
public class MerchantProfileKycController {
  private final ProfileService merchantKycProfileService;
  private final DocumentUploadService<List<FileUploadedDTO>, MerchantKycProfileDocumentUploadDTO>
      merchantKycProfileDocumentUploadService;

  @PatchMapping()
  @Operation(summary = KYC_CONTROLLER_UPDATE_TITLE, description = KYC_CONTROLLER_UPDATE_DESCRIPTION)
  public ResponseEntity<Object> updateKyc(
      @Valid @RequestBody MerchantKycProfileResponse merchantKycProfileOnboardingRequest) {
    return ControllerResponse.buildSuccessResponse(
        merchantKycProfileService.updateProfile(merchantKycProfileOnboardingRequest),
        "Merchant KYC Profile Updated Successfully");
  }

  @GetMapping()
  @Operation(summary = KYC_CONTROLLER_GET_TITLE, description = KYC_CONTROLLER_GET_DESCRIPTION)
  public ResponseEntity<Object> getProfile() {
    return ControllerResponse.buildSuccessResponse(
        merchantKycProfileService.getProfileById(
            RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER)),
        "Merchant KYC Profile Retrieved Successfully");
  }

  @PostMapping(DOCUMENT_API)
  @Operation(summary = UPLOAD_DOCUMENT_TITLE, description = UPLOAD_DOCUMENT_DESCRIPTION)
  public ResponseEntity<Object> uploadDocuments(
      @ModelAttribute MerchantKycProfileDocumentUploadDTO request) {
    return ControllerResponse.buildSuccessResponse(
        merchantKycProfileDocumentUploadService.uploadMultipleDocument(request));
  }
}
