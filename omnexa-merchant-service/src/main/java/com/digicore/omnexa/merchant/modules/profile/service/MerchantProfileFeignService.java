/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import com.digicore.omnexa.merchant.modules.profile.user.dto.response.MerchantUserProfileInfoResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for merchant profile management operations accessible via Feign clients.
 *
 * <p>This service handles external service requests for merchant profile information,
 * ensuring data integrity and providing paginated responses.
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

  @Override
  public PaginatedResponse<ProfileInfoResponse> getAllProfiles(Integer pageNumber, Integer pageSize) {

    int validatedPageNumber = validatePageNumber(pageNumber);
    int validatedPageSize = validatePageSize(pageSize);

    Pageable pageable = PageRequest.of(validatedPageNumber - 1, validatedPageSize);

    try {
      // Fetch paginated data from repository
      Page<MerchantUserProfileInfoResponse> merchantUserProfilePage =
              profileHelper.getMerchantUserProfileRepository()
                      .findAllMerchantUserProfileInfo(pageable);

      // Convert to ProfileInfoResponse list
      List<ProfileInfoResponse> profileInfoResponses = merchantUserProfilePage.getContent()
              .stream()
              .map(ProfileInfoResponse.class::cast)
              .collect(Collectors.toList());

      // Build paginated response using the correct field names
      PaginatedResponse<ProfileInfoResponse> response = PaginatedResponse.<ProfileInfoResponse>builder()
              .content(profileInfoResponses)
              .currentPage(validatedPageNumber)
              .totalItems(merchantUserProfilePage.getTotalElements())
              .totalPages(merchantUserProfilePage.getTotalPages())
              .isFirstPage(merchantUserProfilePage.isFirst())
              .isLastPage(merchantUserProfilePage.isLast())
              .build();

      log.info("Successfully fetched {} merchant user profiles out of {} total",
              profileInfoResponses.size(), merchantUserProfilePage.getTotalElements());

      return response;

    } catch (Exception e) {
      log.error("Error occurred while fetching merchant user profiles", e);
      throw new RuntimeException("Failed to fetch merchant user profiles", e);
    }
  }


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
      Page<MerchantUserProfileInfoResponse> merchantUserProfilePage =
              profileHelper.getMerchantUserProfileRepository()
                      .searchMerchantUserProfileInfo(search.trim(), pageable);

      return buildPaginatedResponse(merchantUserProfilePage, validatedPageNumber,
              "search results for: " + search);

    } catch (Exception e) {
      log.error("Error occurred while searching merchant user profiles with term: '{}'", search, e);
      throw new RuntimeException("Failed to search merchant user profiles", e);
    }
  }

  @Override
  public PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
          ProfileStatus profileStatus, Integer pageNumber, Integer pageSize) {
    log.info("Filtering merchant user profiles by status: {}, page: {}, size: {}",
            profileStatus, pageNumber, pageSize);

    if (profileStatus == null) {
      throw new IllegalArgumentException("Profile status cannot be null");
    }

    int validatedPageNumber = validatePageNumber(pageNumber);
    int validatedPageSize = validatePageSize(pageSize);

    // Create pageable (Spring Data uses 0-based indexing)
    Pageable pageable = PageRequest.of(validatedPageNumber - 1, validatedPageSize);

    try {
      // Fetch paginated filtered results from repository
      Page<MerchantUserProfileInfoResponse> merchantUserProfilePage =
              profileHelper.getMerchantUserProfileRepository()
                      .findMerchantUserProfileInfoByStatus(profileStatus, pageable);

      return buildPaginatedResponse(merchantUserProfilePage, validatedPageNumber,
              "profiles with status: " + profileStatus);

    } catch (Exception e) {
      log.error("Error occurred while filtering merchant user profiles by status: {}", profileStatus, e);
      throw new RuntimeException("Failed to filter merchant user profiles by status", e);
    }
  }

  /**
   * Helper method to build paginated response from repository result.
   */
  private PaginatedResponse<ProfileInfoResponse> buildPaginatedResponse(
          Page<MerchantUserProfileInfoResponse> merchantUserProfilePage,
          int validatedPageNumber,
          String operationDescription) {

    // Convert to ProfileInfoResponse list
    List<ProfileInfoResponse> profileInfoResponses = merchantUserProfilePage.getContent()
            .stream()
            .map(ProfileInfoResponse.class::cast)
            .collect(Collectors.toList());

    // Build paginated response using the correct field names
    PaginatedResponse<ProfileInfoResponse> response = PaginatedResponse.<ProfileInfoResponse>builder()
            .content(profileInfoResponses)
            .currentPage(validatedPageNumber)
            .totalItems(merchantUserProfilePage.getTotalElements())
            .totalPages(merchantUserProfilePage.getTotalPages())
            .isFirstPage(merchantUserProfilePage.isFirst())
            .isLastPage(merchantUserProfilePage.isLast())
            .build();

    log.info("Successfully fetched {} merchant user profiles out of {} total for {}",
            profileInfoResponses.size(), merchantUserProfilePage.getTotalElements(), operationDescription);

    return response;
  }

  /**
   * Validates and applies default page number.
   *
   * @param pageNumber the page number to validate
   * @return validated page number
   */
  private int validatePageNumber(Integer pageNumber) {
    if (pageNumber == null || pageNumber < 1) {
      return DEFAULT_PAGE_NUMBER;
    }
    return pageNumber;
  }

  /**
   * Validates and applies default/max page size constraints.
   *
   * @param pageSize the page size to validate
   * @return validated page size
   */
  private int validatePageSize(Integer pageSize) {
    if (pageSize == null || pageSize < 1) {
      return DEFAULT_PAGE_SIZE;
    }
    return Math.min(pageSize, MAX_PAGE_SIZE);
  }




}