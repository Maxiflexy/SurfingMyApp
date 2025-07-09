/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.swagger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * Configuration class for Swagger-related properties.
 *
 * <p>This class binds Swagger configuration properties defined in the application's configuration
 * files (e.g., `application.properties` or `application.yml`) to Java fields. It uses Spring Boot's
 * {@link ConfigurationProperties} annotation to map properties with the prefix `omnexa.swagger`.
 *
 * <p>Features: - Provides configuration for server URLs, issue tracker links, and project metadata.
 * - Enables customization of Swagger documentation for the project.
 *
 * <p>Usage: - Define Swagger properties in the configuration file with the prefix `omnexa.swagger`.
 * - Example:
 *
 * <pre>
 *   omnexa.swagger.development-server-url=http://localhost:8080
 *   omnexa.swagger.deployed-server-url=https://api.example.com
 *   omnexa.swagger.issue-tracker-url=https://issues.example.com
 *   omnexa.swagger.project-title=Project API Documentation
 *   omnexa.swagger.project-version=1.0.0
 *   omnexa.swagger.project-description=API documentation for the project.
 *   omnexa.swagger.server-description=Digicore Development Server
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.swagger")
@Getter
@Setter
public class SwaggerPropertyConfig {

  /** URL of the development server. */
  private String developmentServerUrl = "";

  /** URL of the deployed server. */
  private String deployedServerUrl = "";

  /** URL of the issue tracker for the project. */
  private String issueTrackerUrl = "";

  /** Title of the project for Swagger documentation. */
  private String projectTitle = "";

  /** Version of the project for Swagger documentation. */
  private String projectVersion = "";

  /**
   * Description of the project for Swagger documentation.
   *
   * <p>This description provides an overview of the APIs exposed by the project. It specifies that
   * all APIs, except authentication and reset password APIs, require a valid authenticated user JWT
   * access token for invocation.
   */
  private String projectDescription =
      "This documentation contains all the APIs exposed for the project. Aside the authentication and reset password APIs, all other APIs requires a valid authenticated user jwt access token before they can be invoked";

  /** Description of the server for Swagger documentation. */
  private String serverDescription = "Digicore Development Server";
}
