/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.pos;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-07(Thu)-2025
 */
public class PosSwaggerDoc {
  /** Default constructor. */
  public PosSwaggerDoc() {}

  /** Base path for terminal APIs. */
  public static final String POS_API = "pos/";

  /** Title for the Terminal Controller in Swagger UI. */
  public static final String POS_CONTROLLER_TITLE = "POS-Engine-Module";

  /** Description for the Terminal Controller in Swagger UI. */
  public static final String POS_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to process transactions for Terminals.";

  public static final String INITIATE_TRANSACTION_TITLE = "Initiate Transaction";

  /** Description for the Get All Merchant Profiles API in Swagger documentation. */
  public static final String INITIATE_TRANSACTION_DESCRIPTION =
      "This API is used to initiate a transaction for a terminal.";

  /** Summary for the get terminals API operation. */
  public static final String POS_CONTROLLER_GET = "Get Terminals";

  /** Description for the get terminals API operation. */
  public static final String POS_CONTROLLER_GET_DESCRIPTION =
      "This API is used to retrieve a paginated list of terminals.";

  /** Summary for the get single terminal API operation. */
  public static final String POS_CONTROLLER_GET_SINGLE = "Get Terminal by Serial ID";

  /** Description for the single terminal API operation. */
  public static final String POS_CONTROLLER_GET_SINGLE_DESCRIPTION =
      "This API is used to get single terminal details by serialId.";
}
