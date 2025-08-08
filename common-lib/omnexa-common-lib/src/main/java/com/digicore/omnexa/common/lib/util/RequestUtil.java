/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import com.digicore.omnexa.common.lib.config.LocalDateTimeTypeAdapter;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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

  private RequestUtil() {}

  @Getter
  private static final Gson gsonMapper =
      new GsonBuilder()
          .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
          .create();

  /**
   * Checks if a string is null or empty.
   *
   * @param value the string to check.
   * @return {@code true} if the object is null or empty, {@code false} otherwise.
   */
  public static boolean isNull(Object value) {
    return value == null;
  }

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

  public static boolean nullOrEmpty(Object object) {
    return object == null;
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

  public static long getTotalAmountInMinor(String jsonString) {
    try {
      JsonNode root = getObjectMapper().readTree(jsonString);
      return sumAmountInMinor(root);
    } catch (Exception e) {
      throw new OmnexaException("Error parsing JSON for amountInMinor", e);
    }
  }

  private static long sumAmountInMinor(JsonNode node) {
    long total = 0;

    if (node.isObject()) {
      Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> entry = fields.next();
        if ("amountInMinor".equals(entry.getKey())) {
          String value = entry.getValue().asText();
          try {
            total += Long.parseLong(value);
          } catch (NumberFormatException e) {
            log.error("Error parsing amountInMinor: {}", value, e);
          }
        } else {
          total += sumAmountInMinor(entry.getValue());
        }
      }
    } else if (node.isArray()) {
      for (JsonNode item : node) {
        total += sumAmountInMinor(item);
      }
    }

    return total;
  }

  public static String camelCaseToSentence(String methodName) {
    if (methodName == null || methodName.isEmpty()) {
      return "";
    }

    // Split the camelCase method name into words
    String[] words = methodName.split("(?<!^)(?=[A-Z])");

    StringBuilder description = new StringBuilder("A request to ");
    for (int i = 0; i < words.length; i++) {
      if (i == 0) {
        description.append(words[i].toLowerCase());
      } else {
        description.append(" ").append(words[i].toLowerCase());
      }
    }

    return description.toString();
  }

  public static String getLoggedInUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) return "SYSTEM";
    return auth.getName();
  }
}
