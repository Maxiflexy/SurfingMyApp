/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.api;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * Represents an API error response object. This class is used to encapsulate error details for API
 * responses. It includes information such as the error message, error code, API path, and the
 * timestamp when the error occurred.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiError {
  /** The error message describing the issue. */
  private String message;

  /** The error code associated with the issue. */
  private String code;

  /** The API path where the error occurred. */
  private String apiPath;

  /** The timestamp when the error occurred. Defaults to the current time. */
  private ZonedDateTime errorTimeStamp = ZonedDateTime.now(ZoneOffset.UTC);

  /**
   * Constructor to create an ApiError with only a message.
   *
   * @param message The error message.
   */
  public ApiError(String message) {
    this.message = message;
  }

  /**
   * Constructor to create an ApiError with a message and API path.
   *
   * @param message The error message.
   * @param apiPath The API path where the error occurred.
   */
  public ApiError(String message, String apiPath) {
    this.message = message;
    this.apiPath = apiPath;
  }

  /**
   * Constructor to create an ApiError with a message, error code, and API path.
   *
   * @param message The error message.
   * @param code The error code.
   * @param apiPath The API path where the error occurred.
   */
  public ApiError(String message, String code, String apiPath) {
    this.message = message;
    this.code = code;
    this.apiPath = apiPath;
  }
}
