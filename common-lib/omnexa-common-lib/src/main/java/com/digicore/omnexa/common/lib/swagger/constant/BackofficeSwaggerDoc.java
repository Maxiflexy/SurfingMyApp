package com.digicore.omnexa.common.lib.swagger.constant;

/**
 * Constants for Swagger documentation related to the Backoffice module.
 *
 * <p>This class provides constant values used for defining Swagger API documentation for the
 * Backoffice module. It includes API paths, controller titles, and descriptions for better
 * organization and readability of the Swagger documentation.
 *
 * <p>Features: - Defines the base API path for the Backoffice module. - Provides titles and
 * descriptions for the Backoffice controller and its APIs.
 *
 * <p>Usage: - Use these constants in Swagger annotations to maintain consistency across the
 * documentation. - Example:
 *
 * <pre>
 *   @Tag(name = BackofficeSwaggerDoc.BACKOFFICE_CONTROLLER_TITLE,
 *        description = BackofficeSwaggerDoc.BACKOFFICE_CONTROLLER_DESCRIPTION)
 *   </pre>
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-01(Tue)-2025
 */
public class BackofficeSwaggerDoc {

  private BackofficeSwaggerDoc() {}

  /** Base API path for the user management module. */
  public static final String BACKOFFICE_API = "backoffice/users/";

  /** Title for the Backoffice controller in Swagger documentation. */
  public static final String BACKOFFICE_CONTROLLER_TITLE = "Backoffice-User-Module";

  /** Description for the BACKOFFICE controller in Swagger documentation. */
  public static final String BACKOFFICE_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to complete BACKOFFICE.";

  /** Title for the Onboard API in Swagger documentation. */
  public static final String BACKOFFICE_CONTROLLER_ONBOARD_TITLE = "Onboard a user";

  /** Description for the Onboard API in Swagger documentation. */
  public static final String BACKOFFICE_CONTROLLER_ONBOARD_DESCRIPTION =
      "This API is used to onboard a user.";
}
