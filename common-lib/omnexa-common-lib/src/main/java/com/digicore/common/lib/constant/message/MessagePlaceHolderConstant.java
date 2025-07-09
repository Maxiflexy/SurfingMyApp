/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.constant.message;

/**
 * A utility class that defines constant placeholders for dynamic message content.
 *
 * <p>This class provides predefined constants that represent placeholders used in dynamic messages
 * throughout the application. These placeholders can be replaced with actual values at runtime to
 * construct meaningful messages.
 *
 * <p>Usage: - Use these constants to reference placeholders in message templates. - Example: {@code
 * "Hello, " + MessagePlaceHolderConstant.NAME + "!"}
 *
 * <p>Features: - Includes placeholders for user-related information (e.g., ROLE_NAME, EMAIL, USER).
 * - Includes placeholders for action-related information (e.g., ACTION, MODULE). - Includes
 * placeholders for time, limits, and fees.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
public class MessagePlaceHolderConstant {

  private MessagePlaceHolderConstant() {}

  /** Placeholder for the role name. */
  public static final String ROLE_NAME = "{roleName}";

  /** Placeholder for the email address. */
  public static final String EMAIL = "{email}";

  /** Placeholder for the module name. */
  public static final String MODULE = "{module}";

  /** Placeholder for the action performed. */
  public static final String ACTION = "{action}";

  /** Placeholder for the user name. */
  public static final String USER = "{user}";

  /** Placeholder for the profile name. */
  public static final String PROFILE = "{profile}";

  /** Placeholder for the time value. */
  public static final String TIME = "{time}";

  /** Placeholder for the name. */
  public static final String NAME = "{name}";

  /** Placeholder for the limit value. */
  public static final String LIMIT = "{limit}";

  /** Placeholder for the fee value. */
  public static final String FEE = "{fee}";

  /** Placeholder for the permissions list. */
  public static final String PERMISSIONS = "{permissions}";
}
