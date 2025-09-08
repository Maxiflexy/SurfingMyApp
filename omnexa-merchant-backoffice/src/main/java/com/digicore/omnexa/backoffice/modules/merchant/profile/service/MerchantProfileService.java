/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.service;

import com.digicore.omnexa.backoffice.modules.merchant.profile.dto.response.MerchantProfileInfoResponse;
import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.feign.MerchantFeignClient;
//import com.digicore.omnexa.common.lib.feign.dto.response.MerchantKycProfileResponse;
import com.digicore.omnexa.common.lib.feign.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Service implementation for managing merchant profiles in the backoffice module.
 *
 * <p>This service interacts with the {@link MerchantFeignClient} to fetch and process merchant
 * profile data. It provides methods to retrieve all profiles, search profiles by a term, and filter
 * profiles by status, all with pagination support.
 *
 * <p>Implements the {@link ProfileService} interface. Uses {@link RequestUtil} for response data
 * conversion.
 *
 * <p>Logs errors and operations using SLF4J {@link lombok.extern.slf4j.Slf4j}. Handles exceptions
 * gracefully and transforms raw response data into a paginated format.
 *
 * <p>Author: Onyekachi Ejemba Created On: Aug-04(Mon)-2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantProfileService implements ProfileService {

  private final MerchantFeignClient merchantFeignClient;

  /**
   * Retrieves all merchant profiles with pagination.
   *
   * @param pageNumber the page number to retrieve (1-based index)
   * @param pageSize the number of profiles per page
   * @return a paginated response containing profile information
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> getAllProfiles(
      Integer pageNumber, Integer pageSize) {
    return executeProfileRequest(
        () -> merchantFeignClient.getAllMerchantProfile(pageNumber, pageSize), "getAllProfiles");
  }

  /**
   * Searches merchant profiles based on a search term with pagination.
   *
   * @param search the search term to filter profiles
   * @param pageNumber the page number to retrieve (1-based index)
   * @param pageSize the number of profiles per page
   * @return a paginated response containing profile information matching the search term
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> searchProfilesPaginated(
      String search, Integer pageNumber, Integer pageSize) {
    return executeProfileRequest(
        () -> merchantFeignClient.searchMerchantProfiles(search, pageNumber, pageSize),
        "searchProfilesPaginated");
  }

  /**
   * Retrieves merchant profiles filtered by status with pagination.
   *
   * @param profileStatus the status to filter profiles by
   * @param pageNumber the page number to retrieve (1-based index)
   * @param pageSize the number of profiles per page
   * @return a paginated response containing profiles with the specified status
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
      ProfileStatus profileStatus, Integer pageNumber, Integer pageSize) {
    return executeProfileRequest(
        () ->
            merchantFeignClient.filterMerchantProfilesByStatus(profileStatus, pageNumber, pageSize),
        "getProfilesByStatusPaginated");
  }


  /**
   * Retrieves a merchant profile by its ID.
   *
   * @param profileId the ID of the profile to retrieve
   * @return the profile information response
   */
  @Override
  public Object getProfileById(String profileId) {
    try {
      ResponseEntity<ApiResponseJson<Object>> response =
              merchantFeignClient.getMerchantKycProfile(profileId);

      if (response.getBody() != null && response.getBody().getData() != null) {
        return RequestUtil.getObjectMapper()
                .convertValue(response.getBody().getData(), Object.class);
      }
    } catch (Exception e) {
      log.error("Failed to retrieve merchant profile with ID {}: {}", profileId, e.getMessage());
    }
    return null;
  }

  /**
   * Updates a merchant profile.
   *
   * @param request the profile edit request containing updated profile details
   * @param profileId the ID of the profile to update
   * @return the response containing the result of the profile update
   */
  @Override
  public Object updateProfile(Object request, String profileId) {
    try {
      ResponseEntity<ApiResponseJson<Object>> response =
              merchantFeignClient.updateMerchantKycProfile(profileId, request);

      if (response.getBody() != null && response.getBody().getData() != null) {
        return RequestUtil.getObjectMapper()
                .convertValue(response.getBody().getData(), Object.class);
      }
    } catch (Exception e) {
      log.error("Failed to update merchant profile with ID {}: {}", profileId, e.getMessage());
    }
    return null;
  }

  /**
   * Uploads documents for a merchant KYC profile.
   *
   * @param request the document upload request
   * @param profileId the ID of the profile to upload documents for
   * @return the list of uploaded file information
   */
  @Override
  public List<FileUploadedDTO> uploadMerchantKycDocuments(
          MerchantKycProfileDocumentUploadDTO request, String profileId) {
    try {
      ResponseEntity<ApiResponseJson<Object>> response =
              merchantFeignClient.uploadMerchantKycDocuments(profileId, request);

      if (response.getBody() != null && response.getBody().getData() != null) {
        return RequestUtil.getObjectMapper()
                .convertValue(response.getBody().getData(),
                        new TypeReference<>() {
                        });
      }
    } catch (Exception e) {
      log.error("Failed to upload documents for merchant profile with ID {}: {}",
              profileId, e.getMessage());
    }
    return null;
  }

  /**
   * Executes a profile request using the provided Feign client call and operation name.
   *
   * <p>This method handles the Feign client call, processes the response, and converts it into a
   * paginated response format. Logs any errors encountered during execution.
   *
   * @param feignCall the Feign client call to execute
   * @param operationName the name of the operation being executed (for logging purposes)
   * @return a paginated response containing profile information, or null if an error occurs
   */
  private PaginatedResponse<ProfileInfoResponse> executeProfileRequest(
      Supplier<ResponseEntity<ApiResponseJson<Object>>> feignCall, String operationName) {
    try {
      ResponseEntity<ApiResponseJson<Object>> response = feignCall.get();

      if (response.getBody() != null) {
        return convertToPaginatedProfileResponse(response.getBody().getData());
      }
    } catch (Exception e) {
      log.error("Failed to execute {} operation: {}", operationName, e.getMessage());
      // throw new OmnexaException("Failed to retrieve merchant profiles", SERVICE_UNAVAILABLE);
    }
    return null;
  }


  /**
   * Toggles the status of a merchant profile between ACTIVE and INACTIVE.
   *
   * @param profileId the ID of the profile to toggle status for
   * @param enabled true to set status to ACTIVE, false to set status to INACTIVE
   */
  @Override
  public void toggleProfileStatus(String profileId, boolean enabled) {
    try {
        merchantFeignClient.toggleMerchantProfileStatus(profileId, enabled);
    } catch (Exception e) {
      log.error("Failed to toggle merchant profile status for ID {}: {}", profileId, e.getMessage());
      //throw new RuntimeException("Failed to toggle merchant profile status", e);
    }
  }

  /**
   * Converts raw response data into a paginated response of profile information.
   *
   * <p>This method uses {@link RequestUtil} to map the raw response data into a {@link
   * PaginatedResponse} of {@link MerchantProfileInfoResponse}, and then transforms it into a {@link
   * PaginatedResponse} of {@link ProfileInfoResponse}.
   *
   * @param responseData the raw response data to convert
   * @return a paginated response containing profile information
   */
  private PaginatedResponse<ProfileInfoResponse> convertToPaginatedProfileResponse(
      Object responseData) {
    PaginatedResponse<MerchantProfileInfoResponse> merchantProfileInfoResponse =
        RequestUtil.getObjectMapper()
            .convertValue(
                responseData,
                new TypeReference<PaginatedResponse<MerchantProfileInfoResponse>>() {});

    // Convert to ProfileInfoResponse list
    List<ProfileInfoResponse> profileInfoResponses =
        merchantProfileInfoResponse.getContent().stream()
            .map(ProfileInfoResponse.class::cast)
            .collect(Collectors.toList());

    // Build paginated response using the correct field names
    return PaginatedResponse.<ProfileInfoResponse>builder()
        .content(profileInfoResponses)
        .currentPage(merchantProfileInfoResponse.getCurrentPage())
        .totalItems(merchantProfileInfoResponse.getTotalItems())
        .totalPages(merchantProfileInfoResponse.getTotalPages())
        .isFirstPage(merchantProfileInfoResponse.isFirstPage())
        .isLastPage(merchantProfileInfoResponse.isLastPage())
        .build();
  }
}
