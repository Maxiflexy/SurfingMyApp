/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ApiResponseJson<T> {
  /** The message describing the response. */
  private String message;

  /** Indicates whether the API call was successful. */
  private boolean success;

  /**
   * The data payload returned by the API. This is a generic type to allow flexibility in the
   * response structure.
   */
  private T data;

  /** A list of errors associated with the API response. Defaults to an empty list. */
  private List<ApiError> errors = new ArrayList<>();

  /**
   * Adds an error to the list of errors.
   *
   * @param error The ApiError object to be added.
   */
  public void addError(ApiError error) {
    errors.add(error);
  }
}
