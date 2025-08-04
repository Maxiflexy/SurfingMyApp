/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import static com.digicore.omnexa.common.lib.util.RequestUtil.nullOrEmpty;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-03(Sun)-2025
 */
@Slf4j
public class PagenationUtil {

  private PagenationUtil() {}

  // Default pagination constants
  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_PAGE_SIZE = 15;
  private static final int MAX_PAGE_SIZE = 15;
  private static final int MIN_PAGE_SIZE = 1;
  private static final int MIN_PAGE_NUMBER = 1;
  private static final String SYSTEM_SORT_DATE = "createdDate";

  /**
   * Validates pagination parameters and sets defaults if needed.
   *
   * <p>This method ensures that: - Page number is at least 1 (defaults to 1 if null or invalid) -
   * Page size is between 1 and 16 (defaults to 16 if null, bounds if invalid) - Logs warnings for
   * invalid values
   *
   * @param pageNumber the page number (1-based, can be null)
   * @param pageSize the page size (can be null)
   * @return an array containing [validatedPageNumber, validatedPageSize]
   */
  public static int[] validatePaginationParameters(Integer pageNumber, Integer pageSize) {
    // Validate and set defaults for pagination parameters
    int validatedPageNumber =
        Math.max(MIN_PAGE_NUMBER, pageNumber != null ? pageNumber : DEFAULT_PAGE_NUMBER);
    int validatedPageSize =
        Math.min(
            MAX_PAGE_SIZE,
            Math.max(MIN_PAGE_SIZE, pageSize != null ? pageSize : DEFAULT_PAGE_SIZE));

    logExtractedParams(pageNumber, pageSize, MAX_PAGE_SIZE, validatedPageSize, validatedPageNumber);

    return new int[] {validatedPageNumber, validatedPageSize};
  }

  /**
   * Validates pagination parameters with custom limits and sets defaults if needed.
   *
   * @param pageNumber the page number (1-based, can be null)
   * @param pageSize the page size (can be null)
   * @param defaultPageSize the default page size to use if pageSize is null
   * @param maxPageSize the maximum allowed page size
   * @return an array containing [validatedPageNumber, validatedPageSize]
   */
  public static int[] validatePaginationParameters(
      Integer pageNumber, Integer pageSize, int defaultPageSize, int maxPageSize) {
    // Validate and set defaults for pagination parameters
    int validatedPageNumber =
        Math.max(MIN_PAGE_NUMBER, pageNumber != null ? pageNumber : DEFAULT_PAGE_NUMBER);
    int validatedPageSize =
        Math.min(
            maxPageSize, Math.max(MIN_PAGE_SIZE, pageSize != null ? pageSize : defaultPageSize));

    logExtractedParams(pageNumber, pageSize, MAX_PAGE_SIZE, validatedPageSize, validatedPageNumber);

    return new int[] {validatedPageNumber, validatedPageSize};
  }

  private static void logExtractedParams(
      Integer pageNumber,
      Integer pageSize,
      int maxPageSize,
      int validatedPageSize,
      int validatedPageNumber) {
    // Log warnings for invalid values
    if (pageNumber != null && pageNumber < MIN_PAGE_NUMBER) {
      log.warn(
          "Invalid page number provided: {}. Using default value: {}",
          pageNumber,
          DEFAULT_PAGE_NUMBER);
    }

    if (pageSize != null && (pageSize < MIN_PAGE_SIZE || pageSize > maxPageSize)) {
      log.warn(
          "Invalid page size provided: {}. Using bounded value: {}", pageSize, validatedPageSize);
    }

    log.debug(
        "Validated pagination parameters: pageNumber={}, pageSize={}",
        validatedPageNumber,
        validatedPageSize);
  }

  public static ValidationResult getValidatedPaginationParameters(
      Integer pageNumber, Integer pageSize) {
    int[] validatedParams = PagenationUtil.validatePaginationParameters(pageNumber, pageSize);
    int validatedPageNumber = validatedParams[0];
    int validatedPageSize = validatedParams[1];
    return new ValidationResult(validatedPageNumber, validatedPageSize);
  }

  public record ValidationResult(int validatedPageNumber, int validatedPageSize) {}

  /**
   * Creates a Pageable object with sorting by the specified field in descending order.
   *
   * @param validatedPageNumber the validated page number (1-based)
   * @param validatedPageSize the validated page size
   * @return Pageable object with sorting
   */
  public static Pageable createPageable(int validatedPageNumber, int validatedPageSize) {

    return createPageable(validatedPageNumber, validatedPageSize, SYSTEM_SORT_DATE);
  }

  /**
   * Creates a Pageable object with sorting by the specified field in descending order.
   *
   * @param validatedPageNumber the validated page number (1-based)
   * @param validatedPageSize the validated page size
   * @param sortByField the field to sort by (e.g., "createdDate")
   * @return Pageable object with sorting
   */
  public static Pageable createPageable(
      int validatedPageNumber, int validatedPageSize, String sortByField) {
    // Convert to 0-based page number for Spring Data
    int zeroBasedPageNumber = validatedPageNumber - 1;

    return PageRequest.of(
        zeroBasedPageNumber, validatedPageSize, Sort.by(Sort.Direction.DESC, sortByField));
  }

  /**
   * Creates a Pageable object with custom sorting.
   *
   * @param validatedPageNumber the validated page number (1-based)
   * @param validatedPageSize the validated page size
   * @param sort the Sort object defining the sorting criteria
   * @return Pageable object with custom sorting
   */
  public static Pageable createPageable(int validatedPageNumber, int validatedPageSize, Sort sort) {
    // Convert to 0-based page number for Spring Data
    int zeroBasedPageNumber = validatedPageNumber - 1;

    return PageRequest.of(zeroBasedPageNumber, validatedPageSize, sort);
  }

  /**
   * Validates a search term to ensure it's not null or empty after trimming.
   *
   * @param searchTerm the search term to validate
   * @param fieldName the name of the field being validated (for error messages)
   * @return the trimmed search term
   * @throws OmnexaException if the search term is null, empty, or blank
   */
  public static String validateSearchTerm(String searchTerm, String fieldName) {
    if (nullOrEmpty(searchTerm)) {
      throw new OmnexaException(fieldName + " is required and cannot be null or empty");
    }

    String trimmed = searchTerm.trim();
    if (trimmed.isEmpty()) {
      throw new OmnexaException(fieldName + " cannot be empty after trimming whitespace");
    }

    return trimmed;
  }

  /**
   * Validates a search term with a default field name.
   *
   * @param searchTerm the search term to validate
   * @return the trimmed search term
   * @throws OmnexaException if the search term is null, empty, or blank
   */
  public static String validateSearchTerm(String searchTerm) {
    return validateSearchTerm(searchTerm, "Search term");
  }
}
