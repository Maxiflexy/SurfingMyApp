/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.api;

/**
 * Represents the API version constants used in the application. This class provides a centralized
 * location for defining API version strings.
 *
 * <p>Usage: - Use `ApiVersion.API_V1` to refer to version 1 of the API.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class ApiVersion {
  /** Private constructor to prevent instantiation of this utility class. */
  private ApiVersion() {}

  /**
   * Constant representing version 1 of the API. This is used to define the base path for API
   * version 1 endpoints.
   */
  public static final String API_V1 = "/v1/";
}
