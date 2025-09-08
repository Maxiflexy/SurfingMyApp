/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.terminal;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-07(Thu)-2025
 */
public class TerminalSwaggerDoc {
  /** Default constructor. */
  public TerminalSwaggerDoc() {}

  /** Base path for terminal APIs. */
  public static final String TERMINAL_API = "terminal/";

  /** Base path for terminal app build APIs. */
  public static final String TERMINAL_APP_BUILD_API = "terminal/app-build/";

  /** Base path for merchant terminal request APIs. */
  public static final String MERCHANT_TERMINAL_REQUEST_API = "terminal/request/";

  /** Title for the Terminal Controller in Swagger UI. */
  public static final String TERMINAL_CONTROLLER_TITLE = "Terminal-Management-Module";

  /** Description for the Terminal Controller in Swagger UI. */
  public static final String TERMINAL_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage Terminals.";

  /** Summary for the create terminal API operation. */
  public static final String TERMINAL_CONTROLLER_CREATE = "Create Terminal";

  /** Description for the create terminal API operation. */
  public static final String TERMINAL_CONTROLLER_CREATE_DESCRIPTION =
      "This API is used to register new Terminals.";

  /** Summary for the get terminals API operation. */
  public static final String TERMINAL_CONTROLLER_GET = "Get Terminals";

  /** Description for the get terminals API operation. */
  public static final String TERMINAL_CONTROLLER_GET_DESCRIPTION =
      "This API is used to retrieve a paginated list of terminals.";

  /** Summary for the get single terminal API operation. */
  public static final String TERMINAL_CONTROLLER_GET_SINGLE = "Get Terminal by Serial ID";

  /** Description for the single terminal API operation. */
  public static final String TERMINAL_CONTROLLER_GET_SINGLE_DESCRIPTION =
      "This API is used to get single terminal details by serialId.";

  /** Title for the Terminal App Build Controller in Swagger UI. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_TITLE = "Terminal-APP-Build-Module";

  /** Description for the Terminal App Build Controller in Swagger UI. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage Terminal App Builds.";

  /** Summary for the creation terminal app build API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_CREATE = "Create Terminal App Build";

  /** Description for the creation terminal app build API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_CREATE_DESCRIPTION =
      "This API is used to register new Terminal App Builds.";

  /** Summary for the get terminal app builds API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_GET = "Get Terminal App Builds";

  /** Description for the get terminal app builds API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_GET_DESCRIPTION =
      "This API is used to retrieve a paginated list of terminal app builds.";

  /** Summary for the get single terminal app build API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_GET_SINGLE =
      "Get Terminal App Build by App Build ID";

  /** Description for the get single terminal app build API operation. */
  public static final String TERMINAL_APP_BUILD_CONTROLLER_GET_SINGLE_DESCRIPTION =
      "This API is used to get single terminal app build details by appBuildId.";

  /** Title for the Merchant Terminal Request Controller in Swagger UI. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_TITLE =
      "Merchant-Terminal-Request-Module";

  /** Description for the Merchant Terminal Request Controller in Swagger UI. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage Merchant Terminal Requests.";

  /** Summary for the create merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_CREATE =
      "Create Merchant Terminal Request";

  /** Description for the create merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_CREATE_DESCRIPTION =
      "This API is used to create a new Merchant Terminal Request for terminal provisioning.";

  /** Summary for the get merchant terminal requests API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_GET =
      "Get Merchant Terminal Requests";

  /** Description for the get merchant terminal requests API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_GET_DESCRIPTION =
      "This API is used to retrieve a paginated list of merchant terminal requests for terminal provisioning.";

  /** Summary for the get single merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_GET_SINGLE =
      "Get Merchant Terminal Request by Terminal Request ID";

  /** Description for the get single merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_GET_SINGLE_DESCRIPTION =
      "This API is used to get single merchant terminal request details by terminalRequestId.";

  /** Summary for the withdrawal of merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_DELETE =
      "Withdraw Merchant Terminal Request";

  /** Description for the withdrawal of merchant terminal request API operation. */
  public static final String MERCHANT_TERMINAL_REQUEST_CONTROLLER_DELETE_DESCRIPTION =
      """
              This API is used to withdraw a merchant terminal request by terminalRequestId.
              The request must be in AWAITING_APPROVAL status to be withdrawable.
              If the request is not in AWAITING_APPROVAL status, an error will be returned.
            """;
}
