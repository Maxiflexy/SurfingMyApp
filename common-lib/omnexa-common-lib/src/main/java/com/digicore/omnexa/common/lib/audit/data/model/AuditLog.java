/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.data.model;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.model.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Entity
@Table(name = "audit_log")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AuditLog extends Auditable<String> implements Serializable {
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

  @Column(columnDefinition = "text")
  private String requestData;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "approval_request_id")
  private ApprovalRequest approvalRequest;
}
