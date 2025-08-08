/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.gateway.server.filter;

import java.util.List;

import com.digicore.omnexa.gateway.server.config.GatewayServerPropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-12(Wed)-2025
 */

@Configuration
@RequiredArgsConstructor
public class CORSFilter {
  private final GatewayServerPropertyConfig securityPropertyConfig;

  @Bean
  public WebFilter corsFilter() {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      if (request.getHeaders().containsKey(HttpHeaders.ORIGIN)) {
        ServerHttpResponse response = exchange.getResponse();

        // Get allowed origins and ensure only one value is set
        List<String> allowedOrigins = securityPropertyConfig.getCorsAllowedOrigins();
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
          response
              .getHeaders()
              .set(
                  "Access-Control-Allow-Origin", allowedOrigins.getFirst()); // Use the first origin
        } else {
          response.getHeaders().set("Access-Control-Allow-Origin", "*");
        }

        response
            .getHeaders()
            .set(
                "Access-Control-Allow-Methods",
                String.join(",", securityPropertyConfig.getCorsAllowedMethods()));
        response
            .getHeaders()
            .set(
                "Access-Control-Allow-Headers",
                String.join(",", securityPropertyConfig.getCorsAllowedHeaders()));

        if (securityPropertyConfig.getCorsAllowedExposedHeaders() != null
            && !securityPropertyConfig.getCorsAllowedExposedHeaders().isEmpty()) {
          response
              .getHeaders()
              .set(
                  "Access-Control-Expose-Headers",
                  String.join(",", securityPropertyConfig.getCorsAllowedExposedHeaders()));
        }

        response.getHeaders().set("Access-Control-Allow-Credentials", "true");
      }
      return chain.filter(exchange);
    };
  }
}
