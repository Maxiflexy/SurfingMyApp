/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.interceptor;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.workflow.request.RequestHandlerPostProcessor;
import com.digicore.omnexa.common.lib.approval.workflow.request.RequestHandlers;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-29(Wed)-2025
 */

// @ConditionalOnProperty(name = "lucid.processors.enabled", havingValue = "true")
@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalRequestInterceptor {

  private RequestHandlers requestHandlers;
  private final RequestHandlerPostProcessor requestHandlerPostProcessor;

  @PostConstruct
  public void init() {
    requestHandlers = requestHandlerPostProcessor.getHandlers();
  }

  public Object process(ApprovalDecisionDTO approvalDecisionDTO) {
    if (approvalDecisionDTO == null) {
      return ApiResponseJson.builder()
          .data(null)
          .message("Invalid Request")
          .errors(List.of(new ApiError("Invalid Request", "99")))
          .build();
    }

    return requestHandlers.handle(
        approvalDecisionDTO.getRequestType(), Object.class, approvalDecisionDTO);
  }
}
