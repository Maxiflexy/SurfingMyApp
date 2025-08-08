/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.dto;

import com.digicore.omnexa.common.lib.approval.dto.ApprovalRequestDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AuditLogDTO {
  public static final String AUDIT_LOG_DTO = "com.digicore.omnexa.common.lib.audit.dto.AuditLogDTO";

  public AuditLogDTO(String module, String activity) {
    this.module = module;
    this.activity = activity;
  }

  private Long auditTrailId;

  private String name;

  private String email;

  private String role;

  private String activity;

  private String module;

  private String activityDescription;

  private boolean activitySuccessfullyDone;

  private LocalDateTime logStartDate;

  private LocalDateTime logEndDate;

  private String ipAddress;

  private String auditType;
  private ApprovalRequestDTO approvalRequests;
}
