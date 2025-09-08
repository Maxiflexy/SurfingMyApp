/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.controller;

import com.digicore.omnexa.backoffice.modules.merchant.profile.approval.validator.MerchantProfileValidatorService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.feign.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_REQUET_SUBMITTED_MESSAGE;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.kyc.KycSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.profile.MerchantProfileSwaggerDoc.*;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@RestController
@RequestMapping(API_V1 + MERCHANT_API)
@RequiredArgsConstructor
@Tag(
    name = MERCHANT_PROFILE_CONTROLLER_TITLE,
    description = MERCHANT_PROFILE_CONTROLLER_DESCRIPTION)
public class MerchantProfileController {

  private final ProfileService merchantProfileService;
    private final MerchantProfileValidatorService merchantProfileValidatorService;


    /**
   * Retrieves all merchant profiles with pagination.
   *
   * <p>This endpoint allows back office users to view a paginated list of all merchant profiles in
   * the system. The results are ordered by creation date in descending order.
   *
   * @param pageNumber the page number (1-based indexing, optional, default: 1)
   * @param pageSize the page size (optional, maximum: 16, default: 16)
   * @return a paginated response containing merchant profile information
   */
  @GetMapping()
  @PreAuthorize("hasAuthority('view-merchant-profile')")
  @Operation(summary = GET_ALL_PROFILES_TITLE, description = GET_ALL_PROFILES_DESCRIPTION)
  public ResponseEntity<Object> getAllMerchantProfile(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildSuccessResponse(
        merchantProfileService.getAllProfiles(pageNumber, pageSize));
  }

  /**
   * Searches merchant profiles by business name or business email.
   *
   * <p>This endpoint allows back office users to search for merchant profiles using a search term
   * that matches against business name or business email. The search is case-insensitive and
   * supports partial matching.
   *
   * @param search the search term to match against business name or business email (required)
   * @param pageNumber the page number (1-based indexing, optional, default: 1)
   * @param pageSize the page size (optional, maximum: 16, default: 16)
   * @return a paginated response containing matching merchant profiles
   */
  @GetMapping(SEARCH_API)
  @PreAuthorize("hasAuthority('view-merchant-profile')")
  @Operation(summary = SEARCH_TITLE, description = SEARCH_DESCRIPTION)
  public ResponseEntity<Object> searchMerchantProfiles(
      @Parameter(
              description = "Search term to match against business name or business email",
              example = "Digicore",
              required = true)
          @RequestParam
          String search,
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        merchantProfileService.searchProfilesPaginated(search, pageNumber, pageSize));
  }

  /**
   * Filters merchant profiles by profile status.
   *
   * <p>This endpoint allows back office users to filter merchant profiles based on their current
   * status (ACTIVE, INACTIVE, LOCKED, or DEACTIVATED). This is useful for administrative operations
   * and reporting.
   *
   * @param profileStatus the profile status to filter by (required)
   * @param pageNumber the page number (1-based indexing, optional, default: 1)
   * @param pageSize the page size (optional, maximum: 16, default: 16)
   * @return a paginated response containing merchant profiles with the specified status
   */
  @GetMapping(FILTER_API)
  @PreAuthorize("hasAuthority('view-merchant-profile')")
  @Operation(summary = FILTER_TITLE, description = FILTER_DESCRIPTION)
  public ResponseEntity<Object> filterMerchantProfilesByStatus(
      @Parameter(description = "Profile status to filter by", example = "ACTIVE", required = true)
          @RequestParam
          ProfileStatus profileStatus,
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        merchantProfileService.getProfilesByStatusPaginated(profileStatus, pageNumber, pageSize));
  }


    /**
     * Retrieves a specific merchant KYC profile by profile ID.
     *
     * <p>This endpoint allows back office users to view detailed information about a specific
     * merchant's KYC profile, including business details, bank information, and KYC documents.
     *
     * @param merchantId the unique identifier of the merchant profile to retrieve (required)
     * @return a response containing the merchant KYC profile information
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('view-merchant-profile')")
    @Operation(summary = KYC_CONTROLLER_GET_TITLE, description = KYC_CONTROLLER_GET_DESCRIPTION)
    public ResponseEntity<Object> getMerchantProfile(
            @Parameter(
                    description = "The unique identifier of the merchant profile",
                    required = true)
            @RequestParam("merchantId")
            String merchantId) {

        return ControllerResponse.buildSuccessResponse(
                merchantProfileService.getProfileById(merchantId),
                "Merchant KYC Profile Retrieved Successfully");
    }

    /**
     * Updates a merchant KYC profile.
     *
     * <p>This endpoint allows back office users to update merchant KYC profile information,
     * including business details, bank information, and KYC status.
     *
     * @param merchantId the unique identifier of the merchant profile to update (required)
     * @param request the KYC profile update request containing the updated information
     * @return a response containing the update result
     */
    @PatchMapping("/profile")
    @Operation(summary = KYC_CONTROLLER_UPDATE_TITLE, description = KYC_CONTROLLER_UPDATE_DESCRIPTION)
    public ResponseEntity<Object> updateMerchantProfile(
            @Parameter(
                    description = "The unique identifier of the merchant profile to update",
                    required = true)
            @RequestParam("merchantId")
            String merchantId,
            @Valid @RequestBody Object request) {

        merchantProfileValidatorService.updateMerchantProfile(merchantId, request);
        return ControllerResponse.buildSuccessResponse(
                SYSTEM_REQUET_SUBMITTED_MESSAGE,
                "Merchant KYC Profile Update Request Submitted Successfully");

//        return ControllerResponse.buildSuccessResponse(
//                merchantProfileService.updateProfile(request, merchantId),
//                "Merchant KYC Profile Updated Successfully");
    }

//    /**
//     * Uploads documents for a merchant KYC profile.
//     *
//     * <p>This endpoint allows back office users to upload KYC documents for a merchant profile,
//     * such as business registration documents, director identification documents, and other
//     * required compliance documents.
//     *
//     * @param profileId the unique identifier of the merchant profile to upload documents for (required)
//     * @param request the document upload request containing the files and metadata
//     * @return a response containing the upload result
//     */
//    @PostMapping("/documents")
//    @Operation(summary = UPLOAD_DOCUMENT_TITLE, description = UPLOAD_DOCUMENT_DESCRIPTION)
//    public ResponseEntity<Object> uploadMerchantDocuments(
//            @Parameter(
//                    description = "The unique identifier of the merchant profile",
//                    required = true)
//            @RequestParam("profileId")
//            String profileId,
//            @ModelAttribute MerchantKycProfileDocumentUploadDTO request) {
//
//        return ControllerResponse.buildSuccessResponse(
//                merchantProfileService.uploadMerchantKycDocuments(request, profileId),
//                "Documents uploaded successfully");
//    }


    /**
     * Toggles the status of a merchant profile between ACTIVE and INACTIVE.
     *
     * <p>This endpoint allows back office users to activate or deactivate a merchant profile.
     * When enabled is true, the profile status is set to ACTIVE. When enabled is false,
     * the profile status is set to INACTIVE.
     *
     * @param merchantId the unique identifier of the merchant profile to toggle (required)
     * @param enabled true to activate the profile, false to deactivate the profile (required)
     * @return a response containing the result of the status toggle operation
     */
    @PatchMapping(STATUS_API)
    @Operation(summary = UPDATE_PROFILE_STATUS_TITLE, description = UPDATE_PROFILE_STATUS_DESCRIPTION)
    public ResponseEntity<Object> updateMerchantProfileStatus(
            @RequestParam("merchantId") String merchantId,
            @RequestParam("enabled") boolean enabled) {
        merchantProfileValidatorService.toggleMerchantProfileStatus(merchantId, enabled);
        return ControllerResponse.buildSuccessResponse(
                SYSTEM_REQUET_SUBMITTED_MESSAGE,
                "Merchant Profile Status Change Request Submitted Successfully");
    }

}
