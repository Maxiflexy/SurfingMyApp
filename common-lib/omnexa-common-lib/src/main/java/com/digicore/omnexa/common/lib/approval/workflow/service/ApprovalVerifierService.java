/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.service;

import static com.digicore.omnexa.common.lib.util.RequestUtil.getObjectMapper;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getValueFromAccessToken;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.data.repository.ApprovalRequestRepository;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalResponseDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalRuleService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.audit.service.OmnexaAuditLogService;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-22(Sat)-2025
 */

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalVerifierService {
  private final ApprovalRequestRepository approvalRequestRepository;
  private final ApprovalRuleService approvalRuleService;
  private final OmnexaAuditLogService regulatoryLoggingService;

  public ApprovalResponseDTO triggerActualMethodCall(
      ProceedingJoinPoint joinPoint,
      String checker,
      MakerChecker makerChecker,
      ApprovalRequest approvalRequest) {
    ApprovalResponseDTO approvalResponseDTO =
        getApprovalResponseDTO(joinPoint, checker, makerChecker, approvalRequest);
    regulatoryLoggingService.log(
        getValueFromAccessToken("role"),
        getValueFromAccessToken("email"),
        getValueFromAccessToken("name"),
        "APPROVE",
        makerChecker.module(),
        approvalRequest);

    return approvalResponseDTO;
  }

  @Async("approvalExecutor")
  public void triggerActualMethodCallSilently(
      ProceedingJoinPoint joinPoint,
      String checker,
      MakerChecker makerChecker,
      ApprovalRequest approvalRequest,
      SecurityContext securityContext,
      String ipAddress) {
    log.info(
        "<<< processing approval request {},--> {} >>>",
        approvalRequest.getId(),
        approvalRequest.getApprovalRequestType());
    SecurityContextHolder.setContext(securityContext);
    getApprovalResponseDTO(joinPoint, checker, makerChecker, approvalRequest);
    regulatoryLoggingService.log(
        ipAddress,
        getValueFromAccessToken("role"),
        getValueFromAccessToken("email"),
        getValueFromAccessToken("name"),
        "APPROVE",
        makerChecker.module(),
        approvalRequest);
    log.info(
        "<<< completed processing approval request {},--> {} >>>",
        approvalRequest.getId(),
        approvalRequest.getApprovalRequestType());
  }

  private ApprovalResponseDTO getApprovalResponseDTO(
      ProceedingJoinPoint joinPoint,
      String checker,
      MakerChecker makerChecker,
      ApprovalRequest approvalRequest) {
    Object[] args = getMethodActualArgs(joinPoint, makerChecker, approvalRequest);

    try {
      if (args.length > 0) joinPoint.proceed(args);
      else joinPoint.proceed();
    } catch (Throwable e) {

      if (e instanceof OmnexaException omnexaException) {
        // Already a OmnexaException, re-throw directly
        throw omnexaException;
      } else {
        log.error(
            "<<< error occurred while processing approval, see message : {} >>>", e.getMessage());
        throw new OmnexaException(HttpStatus.BAD_REQUEST, new ApiError(e.getMessage(), "MKT_003"));
      }
    }
    approvalRequest.setApproved(true);
    approvalRequest.setApprovalUsername(checker);
    approvalRequest.setApprovedDate(LocalDateTime.now());
    approvalRequest.setStatus(ApprovalRequestStatus.EXECUTED);

    approvalRequest.setApprovalName(getValueFromAccessToken("name"));

    approvalRequestRepository.save(approvalRequest);

    return getApprovalResponse();
  }

  private static Object[] getMethodActualArgs(
      ProceedingJoinPoint joinPoint, MakerChecker makerChecker, ApprovalRequest approvalRequest) {
    try {

      if (makerChecker.requestClassName() != null && !makerChecker.requestClassName().isBlank()) {
        Class<?> c = Class.forName(makerChecker.requestClassName());
        Object requestArg = null;

        if (approvalRequest.getDataToUpdate() != null
            && !approvalRequest.getDataToUpdate().isEmpty()) {
          requestArg = getObjectMapper().readValue(approvalRequest.getDataToUpdate(), c);
        }

        Object[] args = joinPoint.getArgs();

        args[1] = requestArg;
        return args;
      }
      return new Object[0];

    } catch (Exception e) {
      throw new OmnexaException(HttpStatus.BAD_REQUEST, new ApiError(e.getMessage(), "MKT_004"));
    }
  }

  private ApprovalResponseDTO getApprovalResponse() {
    return ApprovalResponseDTO.builder()
        .description("Request treated successfully")
        .requestStatus(ApprovalRequestStatus.TREATED)
        .build();
  }
}
