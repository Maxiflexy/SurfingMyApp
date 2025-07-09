/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.gateway.server.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */

@Configuration
public class SwaggerDocRoute {

 @Bean
 public RouteLocator administrationRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
  return routeLocatorBuilder
          .routes()
          .route(p -> p.path("/omnexa-backoffice/documentation/**").uri("lb://OMNEXA-BACKOFFICE"))
          .route(p -> p.path("/omnexa-merchant/documentation/**").uri("lb://OMNEXA-MERCHANT"))
          .build();
 }
}
