/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.profile.MerchantProfileSwaggerDoc.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
