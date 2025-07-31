/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.service;

import com.digicore.omnexa.common.lib.audit.contract.AuditLogService;
import com.digicore.omnexa.common.lib.audit.data.model.AuditLog;
import com.digicore.omnexa.common.lib.audit.data.repository.AuditLogRepository;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
    auditLog.setIpAddress(RequestUtil.getIpAddress(this.request));
    auditLog.setEmail(email);
    auditLog.setActivitySuccessfullyDone(true);
    auditLog.setActivityDescription(description);
    auditLog.setLogEndDate(LocalDateTime.now());
    this.auditLogRepository.save(auditLog);
  }
}
