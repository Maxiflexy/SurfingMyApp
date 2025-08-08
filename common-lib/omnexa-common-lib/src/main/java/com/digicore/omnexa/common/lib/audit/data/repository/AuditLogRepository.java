/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.data.repository;

import static com.digicore.omnexa.common.lib.audit.dto.AuditLogDTO.AUDIT_LOG_DTO;

import com.digicore.omnexa.common.lib.audit.data.model.AuditLog;
import com.digicore.omnexa.common.lib.audit.dto.AuditLogDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
  @Query(
      "SELECT new "
          + AUDIT_LOG_DTO
          + "(a.module, a.activity) FROM AuditLog a WHERE a.approvalRequest.id = :id")
  AuditLogDTO getModuleByApprovalRequestId(Long id);
}
