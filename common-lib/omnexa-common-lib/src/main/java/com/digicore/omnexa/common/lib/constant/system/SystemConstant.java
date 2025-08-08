/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.constant.system;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
public class SystemConstant {
  private SystemConstant() {}

  public static final String SYSTEM_MERCHANT_ROLE_NAME = "CUSTODIAN";
  public static final String SYSTEM_AUTHORIZER_ROLE_NAME = "AUTHORIZER";
  public static final String SYSTEM_INITIATOR_ROLE_NAME = "INITIATOR";
  public static final String SYSTEM_MERCHANT_ROLE_DESCRIPTION =
      "This role has full system access with all permissions.";
  public static final String SYSTEM_AUTHORIZER_ROLE_DESCRIPTION =
      "This role is responsible for reviewing and approving requests initiated by others. This role ensures compliance, verifies accuracy, and grants final authorization for execution.";
  public static final String SYSTEM_INITIATOR_ROLE_DESCRIPTION =
      "This role is responsible for initiating or creating requests, transactions, or processes within the system. Typically has permissions to input and submit data but not to approve or finalize actions.";
  public static final String APPROVE_PERMISSION_PREFIX = "approve-";
  public static final String VIEW_PERMISSION_PREFIX = "view-";
  public static final String SYSTEM_TREAT_REQUEST_PERMISSION = "treat-requests";
  public static final String SYSTEM_AUTHORIZER_EMAIL_NAME = "systemChecker@omnexa.com";
  public static final String SYSTEM_INITIATOR_EMAIL_NAME = "systemMaker@omnexa.com";
  public static final String SYSTEM_AUTHORIZER_DEFAULT_PASSWORD = "@uth0rIP@ssw0rd_0mn3x@";
  public static final String SYSTEM_INITIATOR_DEFAULT_PASSWORD = "1n1ti@P@ssw0rd_0mn3x@";
  public static final String SYSTEM_DEFAULT_INVALID_REQUEST_ERROR = "Request is invalid";
  public static final String SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR =
      "Required field is missing";
  public static final String SYSTEM_DEFAULT_NOT_FOUND_ERROR = "Not found";
  public static final String SYSTEM_DEFAULT_FAILED_FOUND_ERROR = "Request processing failed";
  public static final String SYSTEM_DEFAULT_DUPLICATE_ERROR = "Duplicate data found";
  public static final String SYSTEM_DEFAULT_CONFLICT_ERROR =
      "There is a conflict with the existing data";
  public static final String SYSTEM_DEFAULT_LOCKED_ERROR = "Profile is locked";
  public static final String SYSTEM_DEFAULT_WARNING_ERROR = "Continue with caution";
  public static final String SYSTEM_DEFAULT_DENIED_ERROR = "Access denied";
  public static final String SYSTEM_DEFAULT_TREATED_ERROR = "Request has been treated";
  public static final String SYSTEM_MERCHANT_ID_PLACEHOLDER = "merchantId";
  public static final String SYSTEM_REQUET_SUBMITTED_MESSAGE =
      "Request submitted successfully, await approval";
}
