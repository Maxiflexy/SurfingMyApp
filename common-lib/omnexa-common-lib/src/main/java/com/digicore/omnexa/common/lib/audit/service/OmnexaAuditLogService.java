/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.*;
import static com.digicore.omnexa.common.lib.util.RequestUtil.*;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.audit.annotation.LogActivity;
import com.digicore.omnexa.common.lib.audit.contract.AuditLogService;
import com.digicore.omnexa.common.lib.audit.data.model.AuditLog;
import com.digicore.omnexa.common.lib.audit.data.repository.AuditLogRepository;
import com.digicore.omnexa.common.lib.audit.dto.AuditLogDTO;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@EntityScan("com.digicore.omnexa.common.lib.audit.data.model")
@EnableJpaRepositories(basePackages = {"com.digicore.omnexa.common.lib.audit.data.repository"})
@Service
@RequiredArgsConstructor
public class OmnexaAuditLogService implements AuditLogService {
  private final AuditLogRepository auditLogRepository;
  private final MessagePropertyConfig messagePropertyConfig;
  private final HttpServletRequest request;

  public void log(
      String role,
      String email,
      String name,
      String logActivityType,
      String module,
      String description) {
    AuditLog auditLog = new AuditLog();
    auditLog.setLogStartDate(LocalDateTime.now());
    auditLog.setRole(role);
    auditLog.setName(name);
    auditLog.setActivity(logActivityType);
    auditLog.setModule(module);
    auditLog.setIpAddress(getIpAddress(this.request));
    auditLog.setEmail(email);
    auditLog.setActivitySuccessfullyDone(true);
    auditLog.setActivityDescription(description);
    auditLog.setLogEndDate(LocalDateTime.now());
    this.auditLogRepository.save(auditLog);
  }

  public void log(
      String role,
      String email,
      String name,
      String logActivityType,
      String module,
      ApprovalRequest approvalRequest) {
    AuditLog auditLog = new AuditLog();
    auditLog.setLogStartDate(LocalDateTime.now());
    auditLog.setRole(role);
    auditLog.setName(name);
    auditLog.setActivity(logActivityType);
    auditLog.setModule(module);
    auditLog.setIpAddress(getIpAddress(this.request));
    auditLog.setEmail(email);
    auditLog.setActivitySuccessfullyDone(true);
    auditLog.setActivityDescription(
        getDescription(module, logActivityType, name, approvalRequest.getId(), auditLog));
    auditLog.setLogEndDate(LocalDateTime.now());
    auditLog.setApprovalRequest(approvalRequest);
    this.auditLogRepository.save(auditLog);
  }

  public void log(String logActivityType, String module, String description, String requestData) {
    AuditLog auditLog = new AuditLog();
    auditLog.setLogStartDate(LocalDateTime.now());
    auditLog.setRole(getValueFromAccessToken("role"));
    auditLog.setName(getValueFromAccessToken("name"));
    auditLog.setActivity(logActivityType);
    auditLog.setModule(module);
    auditLog.setIpAddress(getIpAddress(this.request));
    auditLog.setEmail(getValueFromAccessToken("email"));
    auditLog.setActivitySuccessfullyDone(true);
    auditLog.setActivityDescription(description);
    auditLog.setLogEndDate(LocalDateTime.now());
    auditLog.setRequestData(requestData);
    this.auditLogRepository.save(auditLog);
  }

  public void log(
      String ipAddress,
      String role,
      String email,
      String name,
      String logActivityType,
      String module,
      ApprovalRequest approvalRequest) {
    AuditLog auditLog = new AuditLog();
    auditLog.setLogStartDate(LocalDateTime.now());
    auditLog.setRole(role);
    auditLog.setName(name);
    auditLog.setActivity(logActivityType);
    auditLog.setModule(module);
    auditLog.setIpAddress(ipAddress);
    auditLog.setEmail(email);
    auditLog.setActivitySuccessfullyDone(true);
    auditLog.setActivityDescription(
        getDescription(module, logActivityType, name, approvalRequest.getId(), auditLog));
    auditLog.setLogEndDate(LocalDateTime.now());
    auditLog.setApprovalRequest(approvalRequest);
    this.auditLogRepository.save(auditLog);
  }

  private AuditLogDTO getDescription(Long approvalRequestId) {
    return auditLogRepository.getModuleByApprovalRequestId(approvalRequestId);
  }

  private static LogActivity getLogActivityPassedValue(ProceedingJoinPoint joinPoint) {
    Method method = getMethod(joinPoint);
    return method.getAnnotation(LogActivity.class);
  }

  private static Method getMethod(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    return signature.getMethod();
  }

  public void retrieveNecessaryInfoFromLoggedInUserAccessToken(AuditLog auditLog) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
      Map<String, Object> claims = jwtAuthenticationToken.getTokenAttributes();
      auditLog.setRole(claims.get("role").toString());
      auditLog.setName(claims.get("name").toString());
      auditLog.setEmail(claims.get("email").toString());
    }
  }

  private String getDescription(
      String module, String activity, String user, Long approvalRequestId, AuditLog auditLog) {
    String approveRequestActivity = activity;
    if (activity.equalsIgnoreCase("DECLINE") || activity.equalsIgnoreCase("APPROVE")) {
      AuditLogDTO auditLogDTO = getDescription(approvalRequestId);
      approveRequestActivity = auditLogDTO.getActivity();
      module = auditLogDTO.getModule();
      auditLog.setModule(module);
    }
    return switch (activity.toLowerCase()) {
      case CREATE ->
          messagePropertyConfig
              .getAuditMessage(CREATE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case EDIT ->
          messagePropertyConfig
              .getAuditMessage(EDIT)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case DELETE ->
          messagePropertyConfig
              .getAuditMessage(DELETE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case ENABLE ->
          messagePropertyConfig
              .getAuditMessage(ENABLE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case DISABLE ->
          messagePropertyConfig
              .getAuditMessage(DISABLE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case ONBOARD ->
          messagePropertyConfig
              .getAuditMessage(ONBOARD)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case APPROVE ->
          messagePropertyConfig
              .getAuditMessage(APPROVE)
              .replace(ACTION, approveRequestActivity.toLowerCase())
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case DECLINE ->
          messagePropertyConfig
              .getAuditMessage(DECLINE)
              .replace(ACTION, approveRequestActivity.toLowerCase())
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      default -> "N/A";
    };
  }

  private String getDescription(String module, String activity, String user) {
    return switch (activity.toLowerCase()) {
      case CREATE ->
          messagePropertyConfig
              .getAuditMessage(CREATE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case EDIT ->
          messagePropertyConfig
              .getAuditMessage(EDIT)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case DELETE ->
          messagePropertyConfig
              .getAuditMessage(DELETE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case ENABLE ->
          messagePropertyConfig
              .getAuditMessage(ENABLE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case DISABLE ->
          messagePropertyConfig
              .getAuditMessage(DISABLE)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      case ONBOARD ->
          messagePropertyConfig
              .getAuditMessage(ONBOARD)
              .replace(MODULE, module.toLowerCase())
              .replace(USER, user.toLowerCase());
      //      case PASSWORD_UPDATED ->
      //              messagePropertyConfig.getAuditMessage(PASSWORD_UPDATED).replace(USER,
      // user.toLowerCase());
      default -> "N/A";
    };
  }
}
