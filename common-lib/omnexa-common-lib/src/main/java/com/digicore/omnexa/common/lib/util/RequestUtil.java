/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utility class for request-related operations.
 *
 * <p>This class provides helper methods to check if various types of objects (e.g., strings, lists,
 * sets) are null or empty. These methods are commonly used to validate input data in request
 * processing.
 *
 * <p>Features: - Check if a string is null or empty. - Check if a list is null or empty. - Check if
 * a set is null or empty. - Pagination utilities for consistent pagination handling across
 * services.
 *
 * <p>Usage: - Use these methods to simplify null or empty checks in your code. - Example:
 *
 * <pre>
 *   if (RequestUtil.nullOrEmpty(myString)) {
 *       // Handle null or empty string
 *   }
 *
 *   // For pagination
 *   int[] validatedParams = RequestUtil.validatePaginationParameters(pageNumber, pageSize);
 *   Pageable pageable = RequestUtil.createPageable(validatedParams[0], validatedParams[1], "createdDate");
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Slf4j
public class RequestUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  // Default pagination constants
  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_PAGE_SIZE = 16;
  private static final int MAX_PAGE_SIZE = 16;
  private static final int MIN_PAGE_SIZE = 1;
  private static final int MIN_PAGE_NUMBER = 1;

  private RequestUtil() {}

  /**
   * Checks if a string is null or empty.
   *
   * @param str the string to check.
   * @return {@code true} if the string is null or empty, {@code false} otherwise.
   */
  public static boolean nullOrEmpty(String str) {
    return str == null || str.isBlank();
  }

  /**
   * Checks if a list is null or empty.
   *
   * @param collection the list to check.
   * @return {@code true} if the list is null or empty, {@code false} otherwise.
   */
  public static boolean nullOrEmpty(List<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Checks if a set is null or empty.
   *
   * @param collection the set to check.
   * @return {@code true} if the set is null or empty, {@code false} otherwise.
   */
  public static boolean nullOrEmpty(Set<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * Generates a unique profile ID consisting of the current timestamp and a random alphanumeric
   * string.
   *
   * <p>The format is: yyyyMMddHHmmss + 5 random alphanumeric characters, all in uppercase. Example:
   * 20250701123045AB12C
   *
   * @return a unique, uppercase profile ID string.
   */
  public static String generateProfileId() {
    return ZonedDateTime.now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        .concat(RandomStringUtils.secure().nextAlphanumeric(5))
        .toUpperCase();
  }

  public static ObjectMapper getObjectMapper() {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

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

  public static String getValueFromAccessToken(String value) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
      Map<String, Object> claims = jwtAuthenticationToken.getTokenAttributes();
      Object extractedValue = claims.get(value);
      return extractedValue != null ? extractedValue.toString() : "";
    }
    return "";
  }

  public static String getIpAddress(HttpServletRequest request) {
    String ip = Optional.ofNullable(request.getHeader("X-Real-IP")).orElse(request.getRemoteAddr());
    if (ip.equals("0:0:0:0:0:0:0:1")) {
      ip = "127.0.0.1";
    }

    return ip;
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

  /**
   * Creates a Pageable object with sorting by the specified field in descending order.
   *
   * @param validatedPageNumber the validated page number (1-based)
   * @param validatedPageSize the validated page size
   * @return Pageable object with sorting
   */
  public static Pageable createPageable(int validatedPageNumber, int validatedPageSize) {

    return createPageable(validatedPageNumber, validatedPageSize, "createdDate");
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
   * @throws IllegalArgumentException if the search term is null, empty, or blank
   */
  public static String validateSearchTerm(String searchTerm, String fieldName) {
    if (nullOrEmpty(searchTerm)) {
      throw new IllegalArgumentException(fieldName + " is required and cannot be null or empty");
    }

    String trimmed = searchTerm.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty after trimming whitespace");
    }

    return trimmed;
  }

  /**
   * Validates a search term with a default field name.
   *
   * @param searchTerm the search term to validate
   * @return the trimmed search term
   * @throws IllegalArgumentException if the search term is null, empty, or blank
   */
  public static String validateSearchTerm(String searchTerm) {
    return validateSearchTerm(searchTerm, "Search term");
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
}
