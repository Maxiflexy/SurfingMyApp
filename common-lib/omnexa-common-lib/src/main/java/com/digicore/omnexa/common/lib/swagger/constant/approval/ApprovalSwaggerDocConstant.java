/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.approval;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-29(Wed)-2025
 */

public class ApprovalSwaggerDocConstant {
  public static final String APPROVAL_API = "approval/process/";

  public static final String APPROVAL_CONTROLLER_TITLE = "Approval-Module";
  public static final String APPROVAL_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage approval requests.";
  public static final String APPROVAL_CONTROLLER_APPROVE_REQUEST_TITLE =
      "Approve/Decline pending request";
  public static final String APPROVAL_CONTROLLER_APPROVE_REQUEST_DESCRIPTION =
      """
This API allows the approval or decline of a pending request, provided the authenticated user has the required approval
permission for the request type.
If an approval rule is configured for the request type, it will be enforced. However, if no approval rule is set,
any user with the approval permission can approve or decline the request.
If approved, the request will be considered as treated; if declined, it will be marked accordingly.
""";
  public static final String APPROVAL_CONTROLLER_DECLINE_REQUEST_TITLE = "Decline pending request";
  public static final String APPROVAL_CONTROLLER_DECLINE_REQUEST_DESCRIPTION =
      "This API is used to decline a pending request.";
  public static final String APPROVAL_CONTROLLER_GET_REQUEST_TITLE = "Get a single pending request";
  public static final String APPROVAL_CONTROLLER_GET_REQUEST_DESCRIPTION =
      "This API is used to get a pending request.";
  public static final String APPROVAL_CONTROLLER_GET_TREATED_REQUEST_TITLE = "Get treated request";
  public static final String APPROVAL_CONTROLLER_GET_TREATED_REQUEST_DESCRIPTION =
      "This API is used to get all treated request.";
  public static final String APPROVAL_CONTROLLER_GET_PENDING_REQUEST_TITLE =
      "Get all pending request";
  public static final String APPROVAL_CONTROLLER_GET_PENDING_REQUEST_DESCRIPTION =
      "This API is used to get all pending request.";

  public static final String APPROVAL_CONTROLLER_SEARCH_REQUEST_TITLE =
      "Search by status all request";
  public static final String APPROVAL_CONTROLLER_SEARCH_REQUEST_DESCRIPTION =
      "This API is used to search request by status";

  public static final String APPROVAL_CONTROLLER_FILTER_REQUEST_BY_DATE_TITLE =
      "Filter by date all request";
  public static final String APPROVAL_CONTROLLER_FILTER_REQUEST_BY_DATE_DESCRIPTION =
      "This API is used to filter request by date";
}
