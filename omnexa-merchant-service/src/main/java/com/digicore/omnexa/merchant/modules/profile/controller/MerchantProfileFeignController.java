/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing merchant profiles. Provides endpoints to retrieve, search, and filter
 * merchant profiles.
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Hidden
@RestController
@RequestMapping(API_V1 + MERCHANT_API)
@RequiredArgsConstructor
public class MerchantProfileFeignController {

  // Service for handling merchant profile operations
  private final ProfileService merchantProfileFeignService;

  /**
   * Retrieves a paginated list of all merchant profiles.
   *
   * @param pageNumber the page number for pagination (optional)
   * @param pageSize the page size for pagination (optional)
   * @return a ResponseEntity containing the paginated list of merchant profiles
   */
  @GetMapping()
  @PreAuthorize("hasAuthority('view-merchant-profile')")
  public ResponseEntity<Object> getAllMerchantProfile(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildCreateSuccessResponse(
        merchantProfileFeignService.getAllProfiles(pageNumber, pageSize));
  }

  /**
   * Searches merchant profiles by first name, last name, or email.
   *
   * @param search the search term to match against first name, last name, or email (required)
   * @param pageNumber the page number for pagination (optional)
   * @param pageSize the page size for pagination (optional)
   * @return a ResponseEntity containing the paginated list of matching merchant profiles
   */
  @GetMapping("/search")
  @PreAuthorize("hasAuthority('view-merchant-profile')")
  public ResponseEntity<Object> searchMerchantProfiles(
      @Parameter(
              description = "Search term to match against first name, last name, or email",
              example = "john",
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
        merchantProfileFeignService.searchProfilesPaginated(search, pageNumber, pageSize));
  }

  /**
   * Filters merchant profiles by their status.
   *
   * @param profileStatus the profile status to filter by (e.g., ACTIVE, INACTIVE, LOCKED,
   *     DEACTIVATED) (required)
   * @param pageNumber the page number for pagination (optional)
   * @param pageSize the page size for pagination (optional)
   * @return a ResponseEntity containing the paginated list of filtered merchant profiles
   */
  @GetMapping("/filter")
  @PreAuthorize("hasAuthority('view-merchant-profile')")
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
        merchantProfileFeignService.getProfilesByStatusPaginated(
            profileStatus, pageNumber, pageSize));
  }
}
