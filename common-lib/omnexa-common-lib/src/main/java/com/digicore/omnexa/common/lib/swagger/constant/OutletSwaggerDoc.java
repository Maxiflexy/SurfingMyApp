/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant;

/**
 * Contains Swagger documentation constants for the Merchant Outlet APIs.
 *
 * <p>This class provides titles and descriptions used in Swagger annotations for documenting the
 * merchant outlet endpoints.
 */
public class OutletSwaggerDoc {

  /** Default constructor. */
  public OutletSwaggerDoc() {}

  /** Base path for outlet APIs. */
  public static final String OUTLET_API = "outlet/";

  /** Title for the Outlet Controller in Swagger UI. */
  public static final String OUTLET_CONTROLLER_TITLE = "Merchant Outlets";

  /** Description for the Outlet Controller in Swagger UI. */
  public static final String OUTLET_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage merchant outlets.";

  /** Summary for the create outlet API operation. */
  public static final String OUTLET_CONTROLLER_CREATE = "Create Merchant Outlet";

  /** Description for the create outlet API operation. */
  public static final String OUTLET_CONTROLLER_CREATE_DESCRIPTION =
      "This API is used to create a new merchant outlet.";

  /** Summary for the get outlets API operation. */
  public static final String OUTLET_CONTROLLER_GET = "Get Merchant Outlets";

  /** Description for the get outlets API operation. */
  public static final String OUTLET_CONTROLLER_GET_DESCRIPTION =
      "This API is used to retrieve a paginated list of merchant outlets.";

  /** Summary for the update outlet API operation. */
  public static final String OUTLET_CONTROLLER_UPDATE = "Update Merchant Outlet";

  /** Description for the update outlet API operation. */
  public static final String OUTLET_CONTROLLER_UPDATE_DESCRIPTION =
      "This API is used to update an existing merchant outlet profile.";

  /** Summary for the update outlet status API operation. */
  public static final String OUTLET_CONTROLLER_UPDATE_STATUS = "Update Merchant Outlet Status";

  /** Description for the update outlet status API operation. */
  public static final String OUTLET_CONTROLLER_UPDATE_STATUS_DESCRIPTION =
      "This API is used to update the status of a merchant outlet.";
}
