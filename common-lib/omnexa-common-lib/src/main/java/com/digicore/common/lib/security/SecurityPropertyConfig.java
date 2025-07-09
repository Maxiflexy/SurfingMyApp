/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.security;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * Configuration class for security-related properties.
 *
 * <p>This class binds security-related properties defined in the application's configuration files
 * (e.g., `application.properties` or `application.yml`) to Java fields. It uses Spring Boot's
 * {@link ConfigurationProperties} annotation to map properties with the prefix `omnexa.security`.
 *
 * <p>Features: - Provides configuration for JWT keys and authorities. - Defines CORS settings such
 * as allowed origins, methods, and headers. - Configures login attempt limits and durations. -
 * Includes Redis connection details and platform-specific settings.
 *
 * <p>Usage: - Define security properties in the configuration file with the prefix
 * `omnexa.security`. - Example:
 *
 * <pre>
 *   omnexa.security.jwt-key-store-path=/path/to/keystore.jks
 *   omnexa.security.jwt-key-store-password=keystorePassword
 *   omnexa.security.jwt-key-alias=keyAlias
 *   omnexa.security.jwt-private-key-passphrase=privateKeyPassphrase
 *   omnexa.security.cors-allowed-origins=http://example.com
 *   omnexa.security.login-attempt-max-count=5
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.security")
@Getter
@Setter
public class SecurityPropertyConfig {

  /** Path to the JWT keystore file. */
  private String jwtKeyStorePath = "";

  /** Password for the JWT keystore. */
  private String jwtKeyStorePassword = "";

  /** Alias for the JWT key in the keystore. */
  private String jwtKeyAlias = "";

  /** Passphrase for the JWT private key. */
  private String jwtPrivateKeyPassphrase = "";

  /** Name of the authorities claim in the JWT. */
  private String jwtAuthoritiesName = "permissions";

  /** System-defined permissions for the application. */
  private String systemDefinedPermissions = null;

  /** List of allowed origins for CORS. */
  private List<String> corsAllowedOrigins = null;

  /** List of allowed methods for CORS. */
  private List<String> corsAllowedMethods = null;

  /** List of allowed headers for CORS. */
  private List<String> corsAllowedHeaders = null;

  /** List of exposed headers for CORS. */
  private List<String> corsAllowedExposedHeaders = null;

  /** List of URLs that are allowed without authentication. */
  private List<String> allowedUrls = null;

  /** List of URLs that are filtered for specific access. */
  private List<String> filteredUrls = null;

  /** List of client URLs that are allowed. */
  private List<String> allowedClientUrls = null;

  /** Maximum number of login attempts before locking the account. */
  private int loginAttemptMaxCount = 5;

  /** Number of login attempts before issuing a warning. */
  private int loginAttemptLimitBeforeWarning = 2;

  /** Duration (in minutes) for auto-unlocking a locked account. */
  private long loginAttemptAutoUnlockDuration = 30;

  /** Platform type for the application (e.g., ADMIN). */
  private String platform = "ADMIN";

  /** Redis host address for caching. */
  private String redisHost = "";

  /** Redis port for connecting to the server. */
  private String redisPort = "";

  /** Redis password for authentication. */
  private String redisPassword = "";

  /** Domain name for the application. */
  private String domain = ".digicoreltds.com";

  /** Flag to skip BVN OTP validation during login. */
  private boolean skipBvnOtpValidation = false;

  /** Flag to skip SMS validation during login. */
  private boolean skipSmsValidation = false;

  /** System-defined limits for the application. */
  private String systemDefinedLimits = null;

  /** System key for application-level security. */
  private String systemKey = null;

  /** Flag to validate the device during login. */
  private boolean validateDeviceOnLogin = false;
}
