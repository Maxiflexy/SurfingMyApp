/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.properties;

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

  /**
   * Retrieves a user-related message by key.
   *
   * @param key the key for the user message.
   * @return the user message corresponding to the key, or defaultValue if not found.
   */
  public String getUserMessage(String key, String defaultValue) {
    return user != null ? user.getOrDefault(key, defaultValue) : null;
  }

  /**
   * Retrieves a role-related message by key.
   *
   * @param key the key for the role message.
   * @return the role message corresponding to the key, or defaultValue if not found.
   */
  public String getRoleMessage(String key, String defaultValue) {
    return role != null ? role.getOrDefault(key, defaultValue) : null;
  }

  /**
   * Retrieves a login-related message by key.
   *
   * @param key the key for the login message.
   * @return the login message corresponding to the key, or defaultValue if not found.
   */
  public String getLoginMessage(String key, String defaultValue) {
    return login != null ? login.getOrDefault(key, defaultValue) : null;
  }

  /**
   * Retrieves an onboarding-related message by key.
   *
   * @param key the key for the onboarding message.
   * @return the onboarding message corresponding to the key, or defaultValue if not found.
   */
  public String getOnboardMessage(String key, String defaultValue) {
    return onboard != null ? onboard.getOrDefault(key, defaultValue) : null;
  }
}
