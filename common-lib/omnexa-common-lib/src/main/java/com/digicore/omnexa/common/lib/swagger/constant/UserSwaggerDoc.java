package com.digicore.omnexa.common.lib.swagger.constant;

/**
 * @author Onyekachi Ejemba
 * @createdOn Jul-29(Tue)-2025
 */
public class UserSwaggerDoc {

  private UserSwaggerDoc() {}

  /** Base API path for the USER module. */
  public static final String USER_API = "user/";

  /** Title for the USER controller in Swagger documentation. */
  public static final String USER_CONTROLLER_TITLE = "User-Module";

  /** Description for the User controller in Swagger documentation. */
  public static final String USER_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage all users.";

  /** Title for the USER API in Swagger documentation. */
  public static final String USER_CONTROLLER_GET_ALL_TITLE = "Get all Users";

  /** Description for the USER API in Swagger documentation. */
  public static final String USER_CONTROLLER_GET_ALL_DESCRIPTION =
      "This API is used to fetch all users.";

  public static final String USER_CONTROLLER_GET_ALL_BY_SEARCH_FILTER_DESCRIPTION =
      "Retrieves a paginated list of backoffice users filtered by search term in firstName, lastName, or email";

  public static final String USER_CONTROLLER_GET_ALL_BY_PROFILE_STATUS_FILTER_DESCRIPTION =
      "Retrieves a paginated list of backoffice users filtered by profile status (ACTIVE, INACTIVE, LOCKED, DEACTIVATED)";
}
