/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for request-related operations.
 *
 * <p>This class provides helper methods to check if various types of objects (e.g., strings, lists,
 * sets) are null or empty. These methods are commonly used to validate input data in request
 * processing.
 *
 * <p>Features: - Check if a string is null or empty. - Check if a list is null or empty. - Check if
 * a set is null or empty.
 *
 * <p>Usage: - Use these methods to simplify null or empty checks in your code. - Example:
 *
 * <pre>
 *   if (RequestUtil.nullOrEmpty(myString)) {
 *       // Handle null or empty string
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class RequestUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

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
}
