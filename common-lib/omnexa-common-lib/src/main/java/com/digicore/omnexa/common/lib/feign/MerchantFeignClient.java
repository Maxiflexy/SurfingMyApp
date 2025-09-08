package com.digicore.omnexa.common.lib.feign;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.kyc.KycSwaggerDoc.BACKOFFICE_KYC_API;

import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.feign.config.FeignClientConfig;
import com.digicore.omnexa.common.lib.feign.dto.request.MerchantKycProfileDocumentUploadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@FeignClient(name = "omnexa-merchant", configuration = FeignClientConfig.class)
public interface MerchantFeignClient {

  /**
   * Retrieves all merchant profiles with pagination.
   *
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing merchant profile information
   */
  @GetMapping(API_V1 + MERCHANT_API)
  ResponseEntity<ApiResponseJson<Object>> getAllMerchantProfile(
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);

  /**
   * Searches merchant profiles by business name or business email.
   *
   * @param search the search term to match against business name or business email
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing matching merchant profiles
   */
  @GetMapping(API_V1 + MERCHANT_API + "/search")
  ResponseEntity<ApiResponseJson<Object>> searchMerchantProfiles(
      @RequestParam String search,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);

  /**
   * Filters merchant profiles by profile status.
   *
   * @param profileStatus the profile status to filter by
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing merchant profiles with the specified status
   */
  @GetMapping(API_V1 + MERCHANT_API + "/filter")
  ResponseEntity<ApiResponseJson<Object>> filterMerchantProfilesByStatus(
      @RequestParam ProfileStatus profileStatus,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);

  /**
   * Retrieves a merchant KYC profile by profile ID.
   *
   * @param profileId the profile ID to retrieve
   * @return response containing merchant KYC profile information
   */
  @GetMapping(API_V1 + BACKOFFICE_KYC_API)
  ResponseEntity<ApiResponseJson<Object>> getMerchantKycProfile(
      @RequestParam("merchantId") String profileId);

  /**
   * Updates a merchant KYC profile.
   *
   * @param profileId the profile ID to update
   * @param request the KYC profile update request
   * @return response containing update result
   */
  @PostMapping(API_V1 + BACKOFFICE_KYC_API)
  ResponseEntity<ApiResponseJson<Object>> updateMerchantKycProfile(
          @RequestParam("merchantId") String profileId,
          @RequestBody Object request);

  /**
   * Uploads documents for a merchant KYC profile.
   *
   * @param profileId the profile ID to upload documents for
   * @param request the document upload request
   * @return response containing upload result
   */
  @PostMapping(API_V1 + BACKOFFICE_KYC_API + DOCUMENT_API)
  ResponseEntity<ApiResponseJson<Object>> uploadMerchantKycDocuments(
      @RequestParam("merchantId") String profileId,
      @RequestPart("request") MerchantKycProfileDocumentUploadDTO request);

  /**
   * Toggles the status of a merchant profile between ACTIVE and INACTIVE.
   *
   * @param profileId the profile ID to toggle status for
   * @param enabled   true to activate the profile, false to deactivate
   */
  @PostMapping(API_V1 + BACKOFFICE_KYC_API + PROFILE_STATUS_UPDATE_API)
  void toggleMerchantProfileStatus(
          @RequestParam("merchantId") String profileId,
          @RequestParam("enabled") boolean enabled);
}
