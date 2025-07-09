/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration class for the application.
 *
 * <p>This class configures the security settings for the application, including authentication,
 * authorization, and session management. It uses Spring Security to define a security filter chain
 * and customize the behavior of the OAuth2 resource server.
 *
 * <p>Features: - Disables CSRF protection. - Configures URL-based authorization rules. - Sets up
 * stateless session management. - Configures JWT-based authentication and authorization. -
 * Delegates authentication and access denial handling to a custom entry point.
 *
 * <p>Usage: - Annotate this class as a Spring configuration to enable its use in the application. -
 * Ensure the `SecurityPropertyConfig` and `DelegatedAuthenticationEntryPoint` beans are properly
 * configured.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-23(Thu)-2025
 */
@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableAsync
public class SecurityConfig {
  private final SecurityPropertyConfig securityPropertyConfig;
  private final DelegatedAuthenticationEntryPoint authEntryPoint;

  /**
   * Configures the security filter chain for the application.
   *
   * <p>This method defines the security rules for HTTP requests, including permitted URLs,
   * authentication requirements, session management, and OAuth2 resource server settings.
   *
   * @param http the {@link HttpSecurity} object used to configure security settings.
   * @return the configured {@link SecurityFilterChain}.
   * @throws Exception if an error occurs during configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            requests -> {
              if (securityPropertyConfig.getAllowedUrls() != null
                  && !securityPropertyConfig.getAllowedUrls().isEmpty()) {

                // Permit specific URLs
                securityPropertyConfig
                    .getAllowedUrls()
                    .forEach(
                        url ->
                            requests.requestMatchers(new AntPathRequestMatcher(url)).permitAll());
              }
              // Authenticate all other requests
              requests.anyRequest().authenticated();
            })
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter()))
                    .authenticationEntryPoint(authEntryPoint)
                    .accessDeniedHandler(authEntryPoint));

    return http.build();
  }

  /**
   * Configures the JWT authentication converter.
   *
   * <p>This method customizes the JWT authentication process by setting the authorities claim name
   * and removing the default authority prefix.
   *
   * @return the configured {@link JwtAuthenticationConverter}.
   */
  protected JwtAuthenticationConverter authenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("");
    authoritiesConverter.setAuthoritiesClaimName(securityPropertyConfig.getJwtAuthoritiesName());
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return converter;
  }
}
