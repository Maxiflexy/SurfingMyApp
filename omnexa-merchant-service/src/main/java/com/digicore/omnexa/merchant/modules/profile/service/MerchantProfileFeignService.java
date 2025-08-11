/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantProfileInfoResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation for merchant profile management operations accessible via Feign clients.
 *
 * <p>This service handles external service requests for merchant profile information, ensuring data
 * integrity and providing paginated responses.
 *
 * <p>Implements the {@link ProfileService} interface. Provides methods for updating profile
 * statuses, retrieving all profiles, searching profiles, and filtering profiles by status.
 *
 * <p>Uses {@link ProfileHelper} for repository interactions.
 *
 * <p>Pagination is handled using Spring Data's {@link Pageable} and {@link Page}. Default and
 * maximum page size constraints are applied.
 *
 * <p>Logs errors and operations using SLF4J {@link Logger}.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileFeignService implements ProfileService {
  private static final Logger log = LoggerFactory.getLogger(MerchantProfileFeignService.class);

  private final ProfileHelper profileHelper;

  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_PAGE_SIZE = 16;
  private static final int MAX_PAGE_SIZE = 16;

  /**
   * Updates the status and verification status of a merchant profile.
   *
   * @param profileId the unique identifier of the profile
   * @param profileStatus the new status of the profile
   * @param profileVerificationStatus the new verification status of the profile
   */
  @Override
  public void updateProfileStatus(
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {
    profileHelper
        .getMerchantProfileRepository()
        .conditionalUpdateProfileStatuses(
            profileStatus != null ? profileStatus.toString() : null,
            profileVerificationStatus != null ? profileVerificationStatus.toString() : null,
            profileId);
  }

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

    int validatedPageNumber = validatePageNumber(pageNumber);
    int validatedPageSize = validatePageSize(pageSize);

    Pageable pageable = PageRequest.of(validatedPageNumber - 1, validatedPageSize);

    try {
      // Fetch paginated data from repository
      Page<MerchantProfileInfoResponse> merchantProfilePage =
          profileHelper.getMerchantProfileRepository().findAllMerchantProfileInfo(pageable);

      // Convert to ProfileInfoResponse list
      List<ProfileInfoResponse> profileInfoResponses =
          merchantProfilePage.getContent().stream()
              .map(ProfileInfoResponse.class::cast)
              .collect(Collectors.toList());

      // Build paginated response using the correct field names
      PaginatedResponse<ProfileInfoResponse> response =
          PaginatedResponse.<ProfileInfoResponse>builder()
              .content(profileInfoResponses)
              .currentPage(validatedPageNumber)
              .totalItems(merchantProfilePage.getTotalElements())
              .totalPages(merchantProfilePage.getTotalPages())
              .isFirstPage(merchantProfilePage.isFirst())
              .isLastPage(merchantProfilePage.isLast())
              .build();

      return response;

    } catch (Exception e) {
      log.error("Error occurred while fetching merchant user profiles", e);
      throw new RuntimeException("Failed to fetch merchant user profiles", e);
    }
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

    if (search == null || search.trim().isEmpty()) {
      throw new IllegalArgumentException("Search term cannot be null or empty");
    }

    int validatedPageNumber = validatePageNumber(pageNumber);
    int validatedPageSize = validatePageSize(pageSize);

    // Create pageable (Spring Data uses 0-based indexing)
    Pageable pageable = PageRequest.of(validatedPageNumber - 1, validatedPageSize);

    try {
      Page<MerchantProfileInfoResponse> merchantProfilePage =
          profileHelper
              .getMerchantProfileRepository()
              .searchMerchantProfileInfo(search.trim(), pageable);

      return buildPaginatedResponse(
          merchantProfilePage, validatedPageNumber, "search results for: " + search);

    } catch (Exception e) {
      log.error("Error occurred while searching merchant profiles with term: '{}'", search, e);
      throw new RuntimeException("Failed to search merchant profiles", e);
    }
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
    log.info(
        "Filtering merchant profiles by status: {}, page: {}, size: {}",
        profileStatus,
        pageNumber,
        pageSize);

    if (profileStatus == null) {
      throw new IllegalArgumentException("Profile status cannot be null");
    }

    int validatedPageNumber = validatePageNumber(pageNumber);
    int validatedPageSize = validatePageSize(pageSize);

    // Create pageable (Spring Data uses 0-based indexing)
    Pageable pageable = PageRequest.of(validatedPageNumber - 1, validatedPageSize);

    try {
      // Fetch paginated filtered results from repository using MerchantProfileRepository
      Page<MerchantProfileInfoResponse> merchantProfilePage =
          profileHelper
              .getMerchantProfileRepository()
              .findMerchantProfileInfoByStatus(profileStatus, pageable);

      return buildPaginatedResponse(
          merchantProfilePage, validatedPageNumber, "profiles with status: " + profileStatus);

    } catch (Exception e) {
      log.error("Error occurred while filtering merchant profiles by status: {}", profileStatus, e);
      throw new RuntimeException("Failed to filter merchant profiles by status", e);
    }
  }

  /**
   * Retrieves merchant profiles filtered by status (string input) with pagination.
   *
   * @param profileStatus the status to filter profiles by (as a string)
   * @param pageNumber the page number to retrieve (1-based index)
   * @param pageSize the number of profiles per page
   * @return a paginated response containing profiles with the specified status
   */
  public PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
      String profileStatus, Integer pageNumber, Integer pageSize) {

    if (profileStatus == null || profileStatus.trim().isEmpty()) {
      throw new IllegalArgumentException("Profile status cannot be null or empty");
    }

    ProfileStatus status;
    try {
      status = ProfileStatus.valueOf(profileStatus.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid profile status: "
              + profileStatus
              + ". Valid values are: "
              + java.util.Arrays.toString(ProfileStatus.values()));
    }

    return getProfilesByStatusPaginated(status, pageNumber, pageSize);
  }

  /**
   * Helper method to build a paginated response from repository results.
   *
   * @param merchantProfilePage the page of merchant profile information
   * @param validatedPageNumber the validated page number
   * @param operationDescription a description of the operation for logging
   * @return a paginated response containing profile information
   */
  private PaginatedResponse<ProfileInfoResponse> buildPaginatedResponse(
      Page<MerchantProfileInfoResponse> merchantProfilePage,
      int validatedPageNumber,
      String operationDescription) {

    // Convert to ProfileInfoResponse list
    List<ProfileInfoResponse> profileInfoResponses =
        merchantProfilePage.getContent().stream()
            .map(ProfileInfoResponse.class::cast)
            .collect(Collectors.toList());

    // Build paginated response using the correct field names
    PaginatedResponse<ProfileInfoResponse> response =
        PaginatedResponse.<ProfileInfoResponse>builder()
            .content(profileInfoResponses)
            .currentPage(validatedPageNumber)
            .totalItems(merchantProfilePage.getTotalElements())
            .totalPages(merchantProfilePage.getTotalPages())
            .isFirstPage(merchantProfilePage.isFirst())
            .isLastPage(merchantProfilePage.isLast())
            .build();

    log.info(
        "Successfully fetched {} merchant profiles out of {} total for {}",
        profileInfoResponses.size(),
        merchantProfilePage.getTotalElements(),
        operationDescription);

    return response;
  }

  /**
   * Validates and applies default page number if the input is invalid.
   *
   * @param pageNumber the page number to validate
   * @return the validated page number
   */
  private int validatePageNumber(Integer pageNumber) {
    if (pageNumber == null || pageNumber < 1) {
      return DEFAULT_PAGE_NUMBER;
    }
    return pageNumber;
  }

  /**
   * Validates and applies default or maximum page size constraints.
   *
   * @param pageSize the page size to validate
   * @return the validated page size
   */
  private int validatePageSize(Integer pageSize) {
    if (pageSize == null || pageSize < 1) {
      return DEFAULT_PAGE_SIZE;
    }
    return Math.min(pageSize, MAX_PAGE_SIZE);
  }
}
