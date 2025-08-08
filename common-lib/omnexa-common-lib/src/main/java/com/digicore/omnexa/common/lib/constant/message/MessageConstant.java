/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.constant.message;

/**
 * A utility class that defines constant string values for common message keys.
 *
 * <p>This class provides predefined constants that can be used throughout the application to
 * represent various message states, actions, and descriptions. It helps ensure consistency and
 * avoids hardcoding string literals in multiple places.
 *
 * <p>Usage: - Use these constants to reference specific message keys in the application. - Example:
 * {@code MessageConstant.NOT_FOUND}
 *
 * <p>Features: - Includes constants for status messages (e.g., NOT_FOUND, SUCCESSFUL). - Includes
 * constants for actions (e.g., CREATE, EDIT). - Includes constants for states (e.g., LOCKED,
 * INACTIVE).
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class MessageConstant {

  private MessageConstant() {}

  /** Represents a "not found" message key. */
  public static final String NOT_FOUND = "not_found";

  /** Represents a "duplicate" message key. */
  public static final String DUPLICATE = "duplicate";

  /** Represents a "conflict" message key. */
  public static final String CONFLICT = "conflict";

  /** Represents an "invalid" message key. */
  public static final String INVALID = "invalid";

  /** Represents a "required" message key. */
  public static final String REQUIRED = "required";

  /** Represents a "mismatch" message key. */
  public static final String MISMATCH = "mismatch";

  /** Represents a "successful" message key. */
  public static final String SUCCESSFUL = "successful";

  /** Represents a "failed" message key. */
  public static final String FAILED = "failed";

  /** Represents a "denied" message key. */
  public static final String DENIED = "denied";

  /** Represents a "description" message key. */
  public static final String DESCRIPTION = "description";

  /** Represents a "warning" message key. */
  public static final String WARNING = "warning";

  /** Represents a "locked" message key. */
  public static final String LOCKED = "locked";

  /** Represents an "inactive" message key. */
  public static final String INACTIVE = "inactive";

  /** Represents a "create" action message key. */
  public static final String CREATE = "create";

  /** Represents an "edit" action message key. */
  public static final String EDIT = "edit";

  /** Represents a "delete" action message key. */
  public static final String DELETE = "delete";

  /** Represents an "approve" action message key. */
  public static final String APPROVE = "approve";

  /** Represents a "decline" action message key. */
  public static final String DECLINE = "decline";

  /** Represents an "enable" action message key. */
  public static final String ENABLE = "enable";

  /** Represents a "disable" action message key. */
  public static final String DISABLE = "disable";

  /** Represents an "onboard" action message key. */
  public static final String ONBOARD = "onboard";

  /** Represents an "onboard" action message key. */
  public static final String CONFIGURATION_REQUIRED = "configuration_required";
}
