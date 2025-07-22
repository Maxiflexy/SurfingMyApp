/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.exception;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application.
 *
 * <p>This class provides centralized exception handling for various types of exceptions that may
 * occur during the application's runtime. It uses Spring's {@link ControllerAdvice} to intercept
 * exceptions and return appropriate HTTP responses.
 *
 * <p>Features: - Handles common exceptions such as {@link IOException}, {@link
 * SQLIntegrityConstraintViolationException}, and {@link MethodArgumentNotValidException}. - Logs
 * exception details for debugging purposes. - Constructs standardized error responses using {@link
 * ApiError} and {@link ApiResponseJson}.
 *
 * <p>Usage: - Exceptions thrown in controllers are automatically intercepted and handled by this
 * class.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@ControllerAdvice
@Slf4j
public class OmnexaControllerAdvice {

  /**
   * Handles {@link IOException} and returns a BAD_REQUEST response.
   *
   * @param exception The thrown {@link IOException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler({IOException.class})
  public ResponseEntity<Object> handleFileNotFoundException(
      IOException exception, WebRequest request) {
    log.info("io exception : {}", exception.getMessage());
    exception.printStackTrace();
    List<ApiError> apiErrors = new ArrayList<>();
    apiErrors.add(new ApiError("file not found", request.getDescription(false)));
    ApiResponseJson<Object> responseJson =
        ApiResponseJson.builder()
            .data(null)
            .errors(apiErrors)
            .message("file requested not found")
            .build();
    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link SQLIntegrityConstraintViolationException} and returns a BAD_REQUEST response.
   *
   * @param exception The thrown {@link SQLIntegrityConstraintViolationException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> handleOmnexaException(
      SQLIntegrityConstraintViolationException exception, WebRequest request) {
    ApiError error =
        new ApiError(
            "Seems some of the information supplied are not valid, Kindly contact support for help",
            request.getDescription(false));
    ApiResponseJson<String> responseJson =
        ApiResponseJson.<String>builder()
            .data(null)
            .errors(new ArrayList<>())
            .message(
                "Seems some of the information supplied are not valid, Kindly contact support for help")
            .success(false)
            .build();
    responseJson.addError(error);

    return new ResponseEntity<>((responseJson), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link MethodArgumentNotValidException} and returns a BAD_REQUEST response.
   *
   * @param ex The thrown {@link MethodArgumentNotValidException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the validation error details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationException(
      MethodArgumentNotValidException ex, WebRequest request) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    List<ApiError> errors = new ArrayList<>();
    for (FieldError fieldError : fieldErrors) {
      ApiError error = new ApiError(fieldError.getDefaultMessage(), request.getDescription(false));
      errors.add(error);
    }

    return ControllerResponse.buildFailureResponse(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link MissingServletRequestParameterException} and returns a BAD_REQUEST response.
   *
   * @param ex The thrown {@link MissingServletRequestParameterException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, WebRequest request) {
    return ControllerResponse.buildFailureResponse(
        List.of(new ApiError(ex.getMessage(), request.getDescription(false))),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link OmnexaException} and returns a custom response based on the exception details.
   *
   * @param exception The thrown {@link OmnexaException}.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(OmnexaException.class)
  public ResponseEntity<Object> handleOmnexaException(OmnexaException exception) {
    log.debug("cause of error is : {}", exception.getMessage());
    return ControllerResponse.buildFailureResponse(
        exception.getErrors(), exception.getHttpStatus());
  }

  /**
   * Handles {@link HttpRequestMethodNotSupportedException} and returns a METHOD_NOT_ALLOWED
   * response.
   *
   * @param ex The thrown {@link HttpRequestMethodNotSupportedException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    return ControllerResponse.buildFailureResponse(
        List.of(
            new ApiError(
                ex.getMethod().concat(" is not supported"), request.getDescription(false))),
        HttpStatus.METHOD_NOT_ALLOWED);
  }

  /**
   * Handles {@link HttpMessageNotReadableException} and returns a BAD_REQUEST response.
   *
   * @param ex The thrown {@link HttpMessageNotReadableException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, WebRequest request) {
    return ControllerResponse.buildFailureResponse(
        List.of(new ApiError("The required payload is missing", request.getDescription(false))),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link ClassNotFoundException} and returns an INTERNAL_SERVER_ERROR response.
   *
   * @param exception The thrown {@link ClassNotFoundException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(ClassNotFoundException.class)
  public ResponseEntity<Object> handleClassNotFoundException(
      ClassNotFoundException exception, WebRequest request) {
    return ControllerResponse.buildFailureResponse(
        List.of(
            new ApiError("Kindly reach out to support for help", request.getDescription(false))),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handles {@link DateTimeParseException} and returns a BAD_REQUEST response.
   *
   * @param exception The thrown {@link DateTimeParseException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Object> handleDateTimeParseException(
      DateTimeParseException exception, WebRequest request) {
    return ControllerResponse.buildFailureResponse(
        List.of(
            new ApiError(
                exception.getParsedString().concat(" is not in the valid date format"),
                request.getDescription(false))),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link FileSizeLimitExceededException} and returns a BAD_REQUEST response.
   *
   * @param exception The thrown {@link FileSizeLimitExceededException}.
   * @param request The current web request.
   * @return A {@link ResponseEntity} containing the error details.
   */
  @ExceptionHandler({FileSizeLimitExceededException.class})
  public ResponseEntity<Object> handleFieldExceededException(
      FileSizeLimitExceededException exception, WebRequest request) {
    List<ApiError> apiErrors = new ArrayList<>();
    apiErrors.add(new ApiError(exception.getMessage(), request.getDescription(false)));
    ApiResponseJson<Object> responseJson =
        ApiResponseJson.builder()
            .data(null)
            .errors(apiErrors)
            .message(exception.getMessage())
            .build();
    return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
  }
}
