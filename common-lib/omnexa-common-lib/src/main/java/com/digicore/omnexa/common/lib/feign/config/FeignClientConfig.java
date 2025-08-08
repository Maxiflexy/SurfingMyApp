package com.digicore.omnexa.common.lib.feign.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Configuration
public class FeignClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      requestTemplate.header("Authorization", "Bearer " + getBearerToken());
    };
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    // Return a custom error decoder if needed
    return new FeignErrorDecoder();
  }

  private String getBearerToken() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
      Jwt jwt = jwtAuth.getToken();
      return jwt.getTokenValue();
    }
    return null;
  }
}
