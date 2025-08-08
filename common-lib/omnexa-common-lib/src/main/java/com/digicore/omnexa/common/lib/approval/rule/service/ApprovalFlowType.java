/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleDTO;

public interface ApprovalFlowType {
  default void process(ApprovalRuleDTO approvalRuleDTO, String requestId)
      throws InterruptedException {}

  default boolean process(
      ApprovalRuleDTO approvalRuleDTO,
      ApprovalRequest approvalRequest,
      ApprovalDecisionDTO approvalDecisionDTO)
      throws InterruptedException {
    return false;
  }

  default String getNextApproval(ApprovalRuleDTO approvalRuleDTO) {
    return null;
  }
}
