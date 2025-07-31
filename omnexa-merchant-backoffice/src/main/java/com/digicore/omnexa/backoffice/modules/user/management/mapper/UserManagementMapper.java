/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.management.mapper;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper class for user management operations.
 *
 * <p>Handles validation and transformation of user management request parameters. This class
 * follows the Single Responsibility Principle by focusing only on validation and transformation
 * logic.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-29(Tue)-2025
 */
@Component
@Slf4j
public class UserManagementMapper {

  /**
   * Validates and trims the search term.
   *
   * @param searchTerm the search term to validate
   * @return the trimmed search term
   * @throws OmnexaException if the search term is null or empty
   */
  public String validateAndTrimSearchTerm(String searchTerm) {
    if (RequestUtil.nullOrEmpty(searchTerm)) {
      log.error("Search term is required but was null or empty");
      throw new OmnexaException("Search term is required", HttpStatus.BAD_REQUEST);
    }

    String trimmedSearch = searchTerm.trim();
    if (trimmedSearch.isEmpty()) {
      log.error("Search term is required but was empty after trimming");
      throw new OmnexaException("Search term cannot be empty", HttpStatus.BAD_REQUEST);
    }

    log.debug("Validated search term: '{}'", trimmedSearch);
    return trimmedSearch;
  }

  /**
   * Validates the profile status parameter.
   *
   * @param profileStatus the profile status string to validate
   * @return the validated profile status string
   * @throws OmnexaException if the profile status is invalid
   */
  public String validateProfileStatus(String profileStatus) {
    if (RequestUtil.nullOrEmpty(profileStatus)) {
      log.error("Profile status is required but was null or empty");
      throw new OmnexaException("Profile status is required", HttpStatus.BAD_REQUEST);
    }

    String trimmedStatus = profileStatus.trim().toUpperCase();

    try {
      ProfileStatus.valueOf(trimmedStatus);
      log.debug("Validated profile status: '{}'", trimmedStatus);
      return trimmedStatus;
    } catch (IllegalArgumentException e) {
      log.error(
          "Invalid profile status provided: '{}'. Valid values are: {}",
          profileStatus,
          java.util.Arrays.toString(ProfileStatus.values()));
      throw new OmnexaException(
          String.format(
              "Invalid profile status: '%s'. Valid values are: %s",
              profileStatus, java.util.Arrays.toString(ProfileStatus.values())),
          HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Validates pagination parameters and sets defaults if needed.
   *
   * @param pageNumber the page number (1-based)
   * @param pageSize the page size
   * @return an array containing [validatedPageNumber, validatedPageSize]
   */
  public int[] validatePaginationParameters(Integer pageNumber, Integer pageSize) {
    // Validate and set defaults for pagination parameters
    int validatedPageNumber = Math.max(1, pageNumber != null ? pageNumber : 1);
    int validatedPageSize = Math.min(16, Math.max(1, pageSize != null ? pageSize : 16));

    if (pageNumber != null && pageNumber < 1) {
      log.warn("Invalid page number provided: {}. Using default value: 1", pageNumber);
    }

    if (pageSize != null && (pageSize < 1 || pageSize > 16)) {
      log.warn(
          "Invalid page size provided: {}. Using bounded value: {}", pageSize, validatedPageSize);
    }

    log.debug(
        "Validated pagination parameters: pageNumber={}, pageSize={}",
        validatedPageNumber,
        validatedPageSize);

    return new int[] {validatedPageNumber, validatedPageSize};
  }
}
