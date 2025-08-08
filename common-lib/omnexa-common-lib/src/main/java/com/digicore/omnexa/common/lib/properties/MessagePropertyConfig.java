/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.properties;

import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_FAILED_FOUND_ERROR;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_NOT_FOUND_ERROR;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * Configuration class for message properties.
 *
 * <p>This class is used to bind message-related properties defined in the application's
 * configuration files (e.g., `application.properties` or `application.yml`) to Java fields. It uses
 * Spring Boot's {@link ConfigurationProperties} annotation to map properties with the prefix
 * `omnexa.message`.
 *
 * <p>Features: - Provides mappings for user, role, login, and onboarding messages. - Includes
 * utility methods to retrieve specific messages by key with default values.
 *
 * <p>Usage: - Define message properties in the configuration file with the prefix `omnexa.message`.
 * - Example:
 *
 * <pre>
 *   omnexa.message.user.welcome=Welcome, User!
 *   omnexa.message.role.admin=Administrator Access Granted
 *   omnexa.message.login.success=Login Successful
 *   omnexa.message.onboard.complete=Onboarding Complete
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.message")
@Getter
@Setter
public class MessagePropertyConfig {

  /** Map containing user-related messages. */
  private Map<String, String> user;

  /** Map containing role-related messages. */
  private Map<String, String> role;

  /** Map containing login-related messages. */
  private Map<String, String> login;

  /** Map containing onboarding-related messages. */
  private Map<String, String> onboard;

  /** Map containing activation-related messages. */
  private Map<String, String> activation;

  /** Map containing terminal-related messages. */
  private Map<String, String> terminal;

  /** Map containing approval-related messages. */
  private Map<String, String> approval;

  /** Map containing audit-related messages. */
  private Map<String, String> audit;

  /**
   * Retrieves a user-related message by key.
   *
   * @param key the key for the user message.
   * @return the user message corresponding to the key, or defaultValue if not found.
   */
  public String getUserMessage(String key, String defaultValue) {
    return user != null ? user.getOrDefault(key, defaultValue) : defaultValue;
  }

  /**
   * Retrieves a role-related message by key.
   *
   * @param key the key for the role message.
   * @return the role message corresponding to the key, or defaultValue if not found.
   */
  public String getRoleMessage(String key, String defaultValue) {
    return role != null ? role.getOrDefault(key, defaultValue) : defaultValue;
  }

  /**
   * Retrieves a login-related message by key.
   *
   * @param key the key for the login message.
   * @return the login message corresponding to the key, or defaultValue if not found.
   */
  public String getLoginMessage(String key, String defaultValue) {
    return login != null ? login.getOrDefault(key, defaultValue) : defaultValue;
  }

  /**
   * Retrieves an onboarding-related message by key.
   *
   * @param key the key for the onboarding message.
   * @return the onboarding message corresponding to the key, or defaultValue if not found.
   */
  public String getOnboardMessage(String key, String defaultValue) {
    return onboard != null ? onboard.getOrDefault(key, defaultValue) : defaultValue;
  }

  /**
   * Retrieves an activation-related message by key.
   *
   * @param key the key for the activation message.
   * @return the activation message corresponding to the key, or defaultValue if not found.
   */
  public String getActivationMessage(String key) {
    return activation != null
        ? activation.getOrDefault(key, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
        : SYSTEM_DEFAULT_NOT_FOUND_ERROR;
  }

  /**
   * Retrieves an onboarding-related message by key.
   *
   * @param key the key for the onboarding message.
   * @return the onboarding message corresponding to the key, or defaultValue if not found.
   */
  public String getTerminalMessage(String key, String defaultValue) {
    return terminal != null ? terminal.getOrDefault(key, defaultValue) : null;
  }

  /**
   * Retrieves an onboarding-related message by key.
   *
   * @param key the key for the onboarding message.
   * @return the onboarding message corresponding to the key, or defaultValue if not found.
   */
  public String getApprovalMessage(String key) {
    return approval != null ? approval.getOrDefault(key, SYSTEM_DEFAULT_FAILED_FOUND_ERROR) : null;
  }

  /**
   * Retrieves an onboarding-related message by key.
   *
   * @param key the key for the onboarding message.
   * @return the onboarding message corresponding to the key, or defaultValue if not found.
   */
  public String getAuditMessage(String key) {
    return approval != null ? approval.getOrDefault(key, SYSTEM_DEFAULT_FAILED_FOUND_ERROR) : null;
  }
}
