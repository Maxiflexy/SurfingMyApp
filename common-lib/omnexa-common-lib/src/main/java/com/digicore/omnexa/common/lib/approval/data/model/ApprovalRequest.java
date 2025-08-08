/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.data.model;

import com.digicore.omnexa.common.lib.approval.converter.ApprovalRequestStatusConverter;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.audit.data.model.AuditLog;
import com.digicore.omnexa.common.lib.model.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
@Entity
@Table(name = "approval_request")
@Getter
@Setter
public class ApprovalRequest extends Auditable<String> implements Serializable {
  private String requesterName;
  private String approvalName;
  private String description;

  @Column(columnDefinition = "text")
  private String dataToUpdate;

  @Column(columnDefinition = "text")
  private String initialData;

  @NotNull @NotEmpty private String requesterUsername;
  private String approvalUsername;
  private int nextApprovalIndex;
  private boolean approved;

  @Convert(converter = ApprovalRequestStatusConverter.class)
  private ApprovalRequestStatus status;

  private String approvalRequestType;
  private String permission;
  private LocalDateTime createdOn;
  private LocalDateTime approvedDate;
  private String errorTrace;

  @OneToMany(mappedBy = "approvalRequest", cascade = CascadeType.ALL)
  private List<AuditLog> auditLogs = new ArrayList<>();

  @OneToMany(mappedBy = "approvalRequest", cascade = CascadeType.ALL)
  private List<ApprovalFlow> approvalFlows = new ArrayList<>();

  private boolean requiresWorkFlow = false;
}
