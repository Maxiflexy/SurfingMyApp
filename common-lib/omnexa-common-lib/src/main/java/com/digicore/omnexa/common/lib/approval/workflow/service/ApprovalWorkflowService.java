/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.service;

import static com.digicore.omnexa.common.lib.util.RequestUtil.*;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.data.repository.ApprovalRequestRepository;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalResponseDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalRuleService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.audit.service.OmnexaAuditLogService;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

@ConditionalOnProperty(name = "omnexa.maker_checker.enabled", havingValue = "true")
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApprovalWorkflowService {
  private final ApprovalRequestRepository approvalRequestRepository;
  private final ApprovalRuleService approvalRuleService;
  private final ApprovalVerifierService approvalVerifierService;
  private final OmnexaAuditLogService regulatoryLoggingService;
  private final CustodianService custodianService;
  private final HttpServletRequest httpServletRequest;

  private static MakerChecker getValuesPassedToMakerCheckerAnnotation(
      ProceedingJoinPoint joinPoint) {
    Method method = getMethod(joinPoint);
    return method.getAnnotation(MakerChecker.class);
  }

  private static Method getMethod(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    return signature.getMethod();
  }

  private static String getRequestType(ProceedingJoinPoint joinPoint) {
    Method method = getMethod(joinPoint);
    log.trace("got the request type : {}", method.getName());
    return method.getName();
  }

  @Around("@annotation(com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker)")
  public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
    MakerChecker makerChecker = getValuesPassedToMakerCheckerAnnotation(joinPoint);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean bypassFlow = Boolean.parseBoolean(getValueFromAccessToken("required"));
    if (bypassFlow) {
      return custodianService.process(joinPoint, makerChecker);
    }
    if (auth.getAuthorities().stream()
        .anyMatch(x -> x.getAuthority().contains(makerChecker.checkerPermission())))
      return checkerOperationResponse(joinPoint, auth.getName(), makerChecker);
    else if (auth.getAuthorities().stream()
        .anyMatch(x -> x.getAuthority().contains(makerChecker.makerPermission())))
      return makerOperationResponse(joinPoint, auth.getName(), makerChecker);
    else
      throw new OmnexaException(
          HttpStatus.UNAUTHORIZED, new ApiError("You are not authorized", "MKT_001"));
  }

  private Object checkerOperationResponse(
      ProceedingJoinPoint joinPoint, String checker, MakerChecker makerChecker)
      throws InterruptedException {
    ApprovalDecisionDTO approvalDecisionDTO = (ApprovalDecisionDTO) joinPoint.getArgs()[1];
    ApprovalRequest approvalRequest =
        approvalRequestRepository
            .findById(Long.valueOf(approvalDecisionDTO.getRequestId()))
            .orElseThrow();
    if (approvalRequest.getApprovedDate() != null) {
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError("requested already treated", "MKT_002"));
    }

    boolean approved;
    approved = workflowCompleted(makerChecker, approvalRequest, approvalDecisionDTO);

    if (approved) {
      return triggerMethod(joinPoint, checker, makerChecker, approvalRequest);
    }
    approvalRequest.setStatus(ApprovalRequestStatus.PENDING);
    approvalRequestRepository.save(approvalRequest);
    return getApprovalResponse("Request pending", ApprovalRequestStatus.PENDING);
  }

  private ApprovalResponseDTO triggerMethod(
      ProceedingJoinPoint joinPoint,
      String checker,
      MakerChecker makerChecker,
      ApprovalRequest approvalRequest) {
    if (makerChecker.runApproveStateInBackground()) {
      approvalVerifierService.triggerActualMethodCallSilently(
          joinPoint,
          checker,
          makerChecker,
          approvalRequest,
          SecurityContextHolder.getContext(),
          getIpAddress(httpServletRequest));
      return getApprovalResponse("Request processing", ApprovalRequestStatus.PROCESSING);
    }
    return approvalVerifierService.triggerActualMethodCall(
        joinPoint, checker, makerChecker, approvalRequest);
  }

  private boolean workflowCompleted(
      MakerChecker makerChecker,
      ApprovalRequest approvalRequest,
      ApprovalDecisionDTO approvalDecisionDTO)
      throws InterruptedException {
    if (approvalRequest.isRequiresWorkFlow()) {
      boolean approved =
          approvalRuleService.validateApprovalRule(
              makerChecker.activity().toUpperCase(),
              makerChecker.module(),
              approvalRequest,
              approvalDecisionDTO);
      approvalRequestRepository.save(approvalRequest);
      return approved;
    }
    return true;
  }

  private Object makerOperationResponse(
      ProceedingJoinPoint joinPoint, String maker, MakerChecker makerChecker) {

    String requestData = "";
    String initialData = "";
    if (makerChecker.requestClassName() != null && !makerChecker.requestClassName().isBlank()) {
      try {
        initialData = getObjectMapper().writeValueAsString(joinPoint.getArgs()[0]);
        requestData = getObjectMapper().writeValueAsString(joinPoint.getArgs()[1]);
      } catch (Exception e) {
        log.error("maker-checker data deserialization error", e);
      }
    }
    ApprovalRequest approvalRequest =
        prepareApprovalRequest(joinPoint, maker, makerChecker, requestData, initialData);
    approvalRuleService.validateInitiateRule(
        makerChecker.activity().toUpperCase(), makerChecker.module(), approvalRequest, requestData);
    approvalRequestRepository.save(approvalRequest);

    regulatoryLoggingService.log(
        getValueFromAccessToken("role"),
        getValueFromAccessToken("email"),
        getValueFromAccessToken("name"),
        makerChecker.activity().toUpperCase(),
        makerChecker.module(),
        approvalRequest);

    return getApprovalResponse("Request submitted successfully", ApprovalRequestStatus.SUBMITTED);
  }

  private ApprovalRequest prepareApprovalRequest(
      ProceedingJoinPoint joinPoint,
      String maker,
      MakerChecker makerChecker,
      String requestData,
      String initialData) {
    ApprovalRequest approvalRequest = new ApprovalRequest();
    approvalRequest.setStatus(ApprovalRequestStatus.NOT_TREATED);
    approvalRequest.setCreatedOn(LocalDateTime.now());
    approvalRequest.setApproved(false);
    approvalRequest.setDataToUpdate(requestData);
    approvalRequest.setInitialData(initialData);
    approvalRequest.setPermission(makerChecker.checkerPermission());
    approvalRequest.setRequesterUsername(maker);
    approvalRequest.setApprovalRequestType(getRequestType(joinPoint));
    approvalRequest.setCreatedOn(LocalDateTime.now());
    String description = camelCaseToSentence(getRequestType(joinPoint));

    approvalRequest.setDescription(description);
    approvalRequest.setRequesterName(getValueFromAccessToken("name"));
    approvalRequestRepository.save(approvalRequest);
    return approvalRequest;
  }

  private ApprovalResponseDTO getApprovalResponse(
      String description, ApprovalRequestStatus approvalRequestStatus) {
    return ApprovalResponseDTO.builder()
        .description(description)
        .requestStatus(approvalRequestStatus)
        .build();
  }
}
