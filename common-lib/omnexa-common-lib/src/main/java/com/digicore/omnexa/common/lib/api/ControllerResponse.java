/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.api;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for building standardized API responses for controllers. This class provides
 * methods to construct success and failure responses in a consistent format using the
 * `ApiResponseJson` class.
 *
 * <p>The class is designed to be used statically and cannot be instantiated.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class ControllerResponse {
  /** Private constructor to prevent instantiation of this utility class. */
  private ControllerResponse() {}

  /** Default success message used when no custom message is provided. */
  private static final String DEFAULT_SUCCESS_MESSAGE = "Request Successfully Treated";

  /**
   * Builds a success response with custom data and message.
   *
   * @param responseData The data to be included in the response.
   * @param message The custom success message. If blank, the default success message is used.
   * @return A `ResponseEntity` containing the success response.
   */
  public static ResponseEntity<Object> buildSuccessResponse(Object responseData, String message) {
    return ResponseEntity.ok(
        ApiResponseJson.builder()
            .success(true)
            .data(responseData)
            .message(StringUtils.isBlank(message) ? DEFAULT_SUCCESS_MESSAGE : message)
            .errors(null)
            .build());
  }

  /**
   * Builds a success response with custom data and the default success message.
   *
   * @param responseData The data to be included in the response.
   * @return A `ResponseEntity` containing the success response.
   */
  public static ResponseEntity<Object> buildSuccessResponse(Object responseData) {
    return ResponseEntity.ok(
        ApiResponseJson.builder()
            .success(true)
            .data(responseData)
            .message(DEFAULT_SUCCESS_MESSAGE)
            .errors(null)
            .build());
  }

  /**
   * Builds a success response with no data and the default success message.
   *
   * @return A `ResponseEntity` containing the success response.
   */
  public static ResponseEntity<Object> buildSuccessResponse() {
    return ResponseEntity.ok(
        ApiResponseJson.builder()
            .success(true)
            .data(null)
            .message(DEFAULT_SUCCESS_MESSAGE)
            .errors(null)
            .build());
  }

  /**
   * Builds a failure response with a list of errors and an HTTP status. The response message is
   * determined based on the HTTP status code.
   *
   * @param apiErrors A list of `ApiError` objects describing the errors.
   * @param httpStatus The HTTP status to be set for the response.
   * @return A `ResponseEntity` containing the failure response.
   */
  public static ResponseEntity<Object> buildFailureResponse(
      List<ApiError> apiErrors, HttpStatus httpStatus) {
    String message = "";
    if (httpStatus.is4xxClientError()) {
      message =
          "Kindly ensure you are passing the right information, check the errors field for what could be wrong with your request";
    } else if (httpStatus.is5xxServerError()) {
      message = "Oops, sorry we were unable to process your request, reach out to support for help";
    }
    return new ResponseEntity<>(
        ApiResponseJson.builder()
            .success(false)
            .data(null)
            .message(message)
            .errors(apiErrors)
            .build(),
        httpStatus);
  }
}
