/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.data.model;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import com.digicore.omnexa.common.lib.approval.rule.converter.ApprovalStatusConverter;
import com.digicore.omnexa.common.lib.model.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

@Entity
@Table(name = "approval_flow")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ApprovalFlow extends Auditable<String> implements Serializable {
  private String approvalUsername;
  private String approvalName;
  private String approvalRole;
  private String organizationId;
  private String platform;
  private String nextApproval;
  private boolean roleBasedApproval;

  @Convert(converter = ApprovalStatusConverter.class)
  private ApprovalStatus status;

  @Column(columnDefinition = "text")
  private String reason;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "approval_request_id")
  private ApprovalRequest approvalRequest;
}
