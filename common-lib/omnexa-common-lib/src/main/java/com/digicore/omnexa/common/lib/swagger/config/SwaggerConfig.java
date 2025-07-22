/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 *
 * <p>This class sets up the OpenAPI specification for the application, including API metadata,
 * security schemes, servers, and external documentation links.
 *
 * <p>Features: - Configures API metadata such as title, description, and version. - Defines
 * security schemes for bearer token authentication. - Adds server information for development and
 * deployed environments. - Links external documentation such as issue trackers.
 *
 * <p>Usage: - Annotate this class as a Spring configuration to enable Swagger/OpenAPI integration.
 * - Ensure the `SwaggerPropertyConfig` bean is properly configured with project-specific details.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
  private final SwaggerPropertyConfig swaggerPropertyConfig;

  /**
   * Configures the OpenAPI specification for the application.
   *
   * <p>This method sets up API metadata, security schemes, server details, and external
   * documentation.
   *
   * @return the configured {@link OpenAPI} object.
   */
  @Bean
  public OpenAPI springShopOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
        .info(
            new Info()
                .title(swaggerPropertyConfig.getProjectTitle())
                .description(swaggerPropertyConfig.getProjectDescription())
                .version(swaggerPropertyConfig.getProjectVersion()))
        .externalDocs(
            new ExternalDocumentation()
                .description("Issue tracker")
                .url(swaggerPropertyConfig.getIssueTrackerUrl()))
        .addServersItem(
            new Server()
                .description("Local Dev Server")
                .url(swaggerPropertyConfig.getDevelopmentServerUrl()))
        .addServersItem(
            new Server()
                .description(swaggerPropertyConfig.getServerDescription())
                .url(swaggerPropertyConfig.getDeployedServerUrl()))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }
}
