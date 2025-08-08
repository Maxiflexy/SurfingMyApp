/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.dto;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-15(Sat)-2025
 */
@Getter
@Setter
public class ApprovalFlowDTO {
  public static final String APPROVAL_FLOW_DTO =
      "com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalFlowDTO";

  public ApprovalFlowDTO(String approvalUsername) {
    this.approvalUsername = approvalUsername;
  }

  private String approvalUsername;
  private String approvalName;
  private String approvalRole;
  private String organizationId;
  private String platform;
  private String nextApproval;
  private boolean roleBasedApproval;

  private ApprovalStatus status;

  private String reason;
}
