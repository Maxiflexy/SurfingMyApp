/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Delegated authentication entry point and access denied handler.
 *
 * <p>This class serves as both an {@link AuthenticationEntryPoint} and an {@link
 * AccessDeniedHandler}. It delegates exception handling to a {@link HandlerExceptionResolver},
 * allowing centralized exception resolution for authentication and access denial scenarios.
 *
 * <p>Features: - Handles authentication failures by delegating to the exception resolver. - Handles
 * access denial by delegating to the exception resolver.
 *
 * <p>Usage: - Annotate this class as a Spring component to enable its use in the security
 * configuration. - Example:
 *
 * <pre>
 *   @Bean
 *   public DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint(
 *       HandlerExceptionResolver resolver) {
 *       return new DelegatedAuthenticationEntryPoint(resolver);
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-23(Thu)-2025
 */
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint
    implements AuthenticationEntryPoint, AccessDeniedHandler {

  /** The exception resolver used to handle authentication and access denial exceptions. */
  private final HandlerExceptionResolver resolver;

  /**
   * Constructs a new instance of {@code DelegatedAuthenticationEntryPoint}.
   *
   * @param resolver the {@link HandlerExceptionResolver} to delegate exception handling.
   */
  public DelegatedAuthenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  /**
   * Handles authentication failures by delegating to the exception resolver.
   *
   * @param request the HTTP request.
   * @param response the HTTP response.
   * @param authException the authentication exception to handle.
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {
    resolver.resolveException(request, response, null, authException);
  }

  /**
   * Handles access denial by delegating to the exception resolver.
   *
   * @param request the HTTP request.
   * @param response the HTTP response.
   * @param accessDeniedException the access denied exception to handle.
   * @throws IOException if an input/output error occurs.
   * @throws ServletException if a servlet error occurs.
   */
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    resolver.resolveException(request, response, null, accessDeniedException);
  }
}
