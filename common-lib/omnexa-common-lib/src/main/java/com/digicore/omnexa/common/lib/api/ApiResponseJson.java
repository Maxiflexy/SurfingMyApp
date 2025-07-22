/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 * Represents a generic API response object. This class is used to encapsulate the structure of API
 * responses, including the response message, success status, data payload, and any associated
 * errors.
 *
 * @param <T> The type of the data payload.
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class ApiResponseJson<T> {

  @Schema(description = "Indicates if the request was successful")
  private boolean success;

  @Schema(description = "Response message")
  private String message;

  @Schema(description = "Request identifier for tracking (optional)")
  private String requestId;

  @Schema(description = "Response timestamp (optional)")
  private ZonedDateTime timestamp;

  @Schema(description = "Response data payload")
  private T data;

  @Schema(description = "List of errors if request failed")
  private List<ApiError> errors = new ArrayList<>();

  /**
   * Adds an error to the list of errors.
   *
   * @param error The ApiError object to be added.
   */
  public void addError(ApiError error) {
    errors.add(error);
  }

  /**
   * Creates a successful response without tracking fields.
   *
   * @param message the success message
   * @param data the response data
   * @param <T> the type of the data
   * @return a successful ApiResponseJson
   */
  public static <T> ApiResponseJson<T> success(String message, T data) {
    return ApiResponseJson.<T>builder().success(true).message(message).data(data).build();
  }

  /**
   * Creates a successful response with tracking fields.
   *
   * @param message the success message
   * @param data the response data
   * @param requestId the request identifier
   * @param timestamp the response timestamp
   * @param <T> the type of the data
   * @return a successful ApiResponseJson with tracking fields
   */
  public static <T> ApiResponseJson<T> success(
      String message, T data, String requestId, ZonedDateTime timestamp) {
    return ApiResponseJson.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .requestId(requestId)
        .timestamp(timestamp)
        .build();
  }

  /**
   * Creates an error response without tracking fields.
   *
   * @param message the error message
   * @param <T> the type of the data
   * @return an error ApiResponseJson
   */
  public static <T> ApiResponseJson<T> error(String message) {
    return ApiResponseJson.<T>builder().success(false).message(message).build();
  }

  /**
   * Creates an error response with tracking fields.
   *
   * @param message the error message
   * @param requestId the request identifier
   * @param timestamp the response timestamp
   * @param <T> the type of the data
   * @return an error ApiResponseJson with tracking fields
   */
  public static <T> ApiResponseJson<T> error(
      String message, String requestId, ZonedDateTime timestamp) {
    return ApiResponseJson.<T>builder()
        .success(false)
        .message(message)
        .requestId(requestId)
        .timestamp(timestamp)
        .build();
  }
}
