/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.exception;

import com.digicore.common.lib.api.ApiError;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling application-specific errors.
 *
 * <p>This class extends {@link RuntimeException} and provides additional fields for error codes,
 * HTTP status, and a list of {@link ApiError} objects to represent detailed error information. It
 * includes multiple constructors to support various use cases for exception handling.
 *
 * <p>Features: - Default error code: "SER500". - Default error message: "unable to process
 * request". - Supports HTTP status and detailed error information.
 *
 * <p>Usage: - Throw this exception to indicate application-specific errors. - Use constructors to
 * customize error messages, codes, HTTP status, and error details.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Getter
public class OmnexaException extends RuntimeException {

  /** Default error code for the exception. */
  private String code = "SER500";

  /** Default error message for the exception. */
  private static final String REQUEST_ERROR = "unable to process request";

  /** HTTP status associated with the exception. */
  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  /** List of detailed error information. */
  private List<ApiError> errors = new ArrayList<>();

  /** Default constructor. */
  public OmnexaException() {}

  /**
   * Constructor with a custom message and error code.
   *
   * @param message The error message.
   * @param code The error code.
   */
  public OmnexaException(String message, String code) {
    super(message);
    this.code = code;
  }

  /**
   * Constructor with a custom message, error code, and HTTP status.
   *
   * @param message The error message.
   * @param code The error code.
   * @param httpStatus The HTTP status.
   */
  public OmnexaException(String message, String code, HttpStatus httpStatus) {
    super(message);
    this.code = code;
    this.httpStatus = httpStatus;
  }

  /**
   * Constructor with a custom message and HTTP status.
   *
   * @param message The error message.
   * @param httpStatus The HTTP status.
   */
  public OmnexaException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  /**
   * Constructor with a custom message, HTTP status, and a list of errors.
   *
   * @param message The error message.
   * @param httpStatus The HTTP status.
   * @param errors The list of detailed errors.
   */
  public OmnexaException(String message, HttpStatus httpStatus, List<ApiError> errors) {
    super(message);
    this.httpStatus = httpStatus;
    this.errors = errors;
  }

  /**
   * Constructor with HTTP status and a list of errors.
   *
   * @param httpStatus The HTTP status.
   * @param errors The list of detailed errors.
   */
  public OmnexaException(HttpStatus httpStatus, List<ApiError> errors) {
    super(REQUEST_ERROR);
    this.httpStatus = httpStatus;
    this.errors = errors;
  }

  /**
   * Constructor with HTTP status and a single error.
   *
   * @param httpStatus The HTTP status.
   * @param error The detailed error.
   */
  public OmnexaException(HttpStatus httpStatus, ApiError error) {
    super(REQUEST_ERROR);
    this.httpStatus = httpStatus;
    this.errors = List.of(error);
  }

  /**
   * Constructor with a single error.
   *
   * @param error The detailed error.
   */
  public OmnexaException(ApiError error) {
    super(REQUEST_ERROR);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errors = List.of(error);
  }

  /**
   * Constructor with a custom message and a throwable cause.
   *
   * @param message The error message.
   * @param inner The throwable cause.
   */
  public OmnexaException(String message, Throwable inner) {
    super(message, inner);
  }

  /**
   * Constructor with a custom message.
   *
   * @param message The error message.
   */
  public OmnexaException(String message) {
    super(message);
  }
}
