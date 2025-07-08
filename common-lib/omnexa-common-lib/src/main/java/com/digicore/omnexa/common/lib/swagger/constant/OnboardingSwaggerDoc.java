/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant;

/**
 * Constants for Swagger documentation related to the Onboarding module.
 *
 * <p>This class provides constant values used for defining Swagger API documentation for the
 * Onboarding module. It includes API paths, controller titles, and descriptions for better
 * organization and readability of the Swagger documentation.
 *
 * <p>Features: - Defines the base API path for the Onboarding module. - Provides titles and
 * descriptions for the Onboarding controller and its APIs.
 *
 * <p>Usage: - Use these constants in Swagger annotations to maintain consistency across the
 * documentation. - Example:
 *
 * <pre>
 *   @Tag(name = OnboardingSwaggerDoc.ONBOARDING_CONTROLLER_TITLE,
 *        description = OnboardingSwaggerDoc.ONBOARDING_CONTROLLER_DESCRIPTION)
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class OnboardingSwaggerDoc {

  private OnboardingSwaggerDoc() {}

  /** Base API path for the Onboarding module. */
  public static final String ONBOARDING_API = "onboarding/";

  /** Title for the Onboarding controller in Swagger documentation. */
  public static final String ONBOARDING_CONTROLLER_TITLE = "Onboarding-Module";

  /** Description for the Onboarding controller in Swagger documentation. */
  public static final String ONBOARDING_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to complete onboarding.";

  /** Title for the Onboard API in Swagger documentation. */
  public static final String ONBOARDING_CONTROLLER_ONBOARD_TITLE = "Onboard a user";

  /** Description for the Onboard API in Swagger documentation. */
  public static final String ONBOARDING_CONTROLLER_ONBOARD_DESCRIPTION =
      "This API is used to onboard a user.";
}
