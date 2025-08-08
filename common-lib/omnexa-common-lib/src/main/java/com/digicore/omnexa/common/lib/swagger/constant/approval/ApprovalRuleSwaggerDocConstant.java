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

public class ApprovalRuleSwaggerDocConstant {
  public static final String APPROVAL_RULE_API = "approval/rule/process/";

  public static final String APPROVAL_RULE_CREATE_API = "create";
  public static final String APPROVAL_RULE_EDIT_API = "edit";
  public static final String APPROVAL_RULE_ENABLE_API = "enable-{roleName}";
  public static final String APPROVAL_RULE_DISABLE_API = "disable-{roleName}";
  public static final String APPROVAL_RULE_DELETE_API = "remove-{roleName}";
  public static final String APPROVAL_RULE_RETRIEVE_ALL_ACTIVITY_TYPES_API =
      "retrieve-all-activity-types";
  public static final String APPROVAL_RULE_RETRIEVE_ALL_RULES_API = "retrieve-all";
  public static final String APPROVAL_RULE_RETRIEVE_RULES_API = "retrieve";

  public static final String APPROVAL_RULE_CONTROLLER_TITLE = "Approval-Rule-Module";
  public static final String APPROVAL_RULE_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to manage approval rules.";
  public static final String APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_TITLE =
      "Get all approval rules";
  public static final String APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_DESCRIPTION =
      "This API is used to get all approval rules.";
  public static final String APPROVAL_RULE_CONTROLLER_GET_RULES_TITLE = "Get a approval rule";
  public static final String APPROVAL_RULE_CONTROLLER_GET_RULES_DESCRIPTION =
      "This API is used to get a approval rule.";
  public static final String APPROVAL_RULE_CONTROLLER_CREATE_RULE_TITLE = "Create approval rule";
  public static final String APPROVAL_RULE_CONTROLLER_CREATE_RULE_DESCRIPTION =
      "This API is used to create approval rule.";
  public static final String APPROVAL_RULE_CONTROLLER_EDIT_RULE_TITLE = "Edit approval rule";
  public static final String APPROVAL_RULE_CONTROLLER_EDIT_RULE_DESCRIPTION =
      "This API is used to edit approval rule.";
  public static final String APPROVAL_RULE_CONTROLLER_GET_ACTIVITY_TITLE = "Get all activity types";
  public static final String APPROVAL_RULE_CONTROLLER_GET_ACTIVITY_DESCRIPTION =
      "This API retrieves all activity types, where an activity type is a combination of an activity and a module. It defines the scope of an approval rule.";
  public static final String APPROVAL_RULE_CONTROLLER_GET_TREATED_REQUEST_TITLE =
      "Get treated request";
  public static final String APPROVAL_RULE_CONTROLLER_GET_TREATED_REQUEST_DESCRIPTION =
      "This API is used to get all treated request.";
  public static final String APPROVAL_RULE_CONTROLLER_GET_PENDING_REQUEST_TITLE =
      "Get all pending request";
  public static final String APPROVAL_RULE_CONTROLLER_GET_PENDING_REQUEST_DESCRIPTION =
      "This API is used to get all pending request.";

  public static final String APPROVAL_RULE_CONTROLLER_SEARCH_REQUEST_TITLE =
      "Search by status all request";
  public static final String APPROVAL_RULE_CONTROLLER_SEARCH_REQUEST_DESCRIPTION =
      "This API is used to search request by status";

  public static final String APPROVAL_RULE_CONTROLLER_FILTER_REQUEST_BY_DATE_TITLE =
      "Filter by date all request";
  public static final String APPROVAL_RULE_CONTROLLER_FILTER_REQUEST_BY_DATE_DESCRIPTION =
      "This API is used to filter request by date";
}
