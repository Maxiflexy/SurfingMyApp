/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.logging.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Servlet filter for managing trace IDs in logging.
 *
 * <p>This filter intercepts HTTP requests and ensures that each request is associated with a unique
 * trace ID. If a trace ID is provided in the request header, it uses that; otherwise, it generates
 * a new trace ID. The trace ID is stored in the MDC (Mapped Diagnostic Context) for use in logging
 * and cleared after the request is processed.
 *
 * <p>Features: - Ensures traceability of requests in logs. - Automatically generates a trace ID if
 * not provided. - Clears MDC after request processing to avoid memory leaks.
 *
 * <p>Usage: - This filter is automatically applied to all incoming requests in a Spring Boot
 * application.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Component
public class TraceIdFilter implements Filter {

  /** The key used to store the trace ID in the MDC. */
  private static final String TRACE_ID = "traceId";

  /**
   * Filters incoming requests to manage trace IDs.
   *
   * <p>Checks for a trace ID in the request header. If absent, generates a new trace ID. Stores the
   * trace ID in the MDC for logging purposes and clears it after the request is processed.
   *
   * @param servletRequest The incoming servlet request.
   * @param servletResponse The outgoing servlet response.
   * @param filterChain The filter chain to pass the request and response to the next filter.
   * @throws IOException If an I/O error occurs during request processing.
   * @throws ServletException If a servlet error occurs during request processing.
   */
  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
      String traceId = httpRequest.getHeader(TRACE_ID);
      if (traceId == null || traceId.isBlank()) {
        traceId = UUID.randomUUID().toString();
      }
      MDC.put(TRACE_ID, traceId);
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      MDC.clear();
    }
  }
}
