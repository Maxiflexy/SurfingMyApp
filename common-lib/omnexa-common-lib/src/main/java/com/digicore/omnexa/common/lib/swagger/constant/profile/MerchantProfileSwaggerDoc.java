/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.profile;

/**
 * Constants for Swagger documentation related to the Merchant Profile module.
 *
 * <p>This class provides constant values used for defining Swagger API documentation for the
 * Merchant Profile module. It includes API paths, controller titles, and descriptions for better
 * organization and readability of the Swagger documentation.
 *
 * <p>Features: - Defines the base API path for the Merchant Profile module - Provides titles and
 * descriptions for the Merchant Profile controller and its APIs - Maintains consistency across all
 * merchant profile related endpoints
 *
 * <p>Usage: - Use these constants in Swagger annotations to maintain consistency across the
 * documentation. - Example:
 *
 * <pre>
 *   @Tag(name = MerchantProfileSwaggerDoc.MERCHANT_PROFILE_CONTROLLER_TITLE,
 *        description = MerchantProfileSwaggerDoc.MERCHANT_PROFILE_CONTROLLER_DESCRIPTION)
 * </pre>
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
public class MerchantProfileSwaggerDoc {

  private MerchantProfileSwaggerDoc() {}

  /** Base API path for the merchant profile module. */
  public static final String MERCHANT_PROFILE_API = "backoffice/merchant/profiles/";

  /** Title for the Merchant Profile controller in Swagger documentation. */
  public static final String MERCHANT_PROFILE_CONTROLLER_TITLE =
      "Merchant-Profile-Management-Module";

  /** Description for the Merchant Profile controller in Swagger documentation. */
  public static final String MERCHANT_PROFILE_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs for managing merchant profiles,"
          + " It provides functionality to view, search, and filter merchant profiles";

  /** Title for the Get All Merchant Profiles API in Swagger documentation. */
  public static final String GET_ALL_PROFILES_TITLE = "Get all merchant profiles";

  /** Description for the Get All Merchant Profiles API in Swagger documentation. */
  public static final String GET_ALL_PROFILES_DESCRIPTION =
      "Retrieve a paginated list of all merchant profiles in the system.";
}
