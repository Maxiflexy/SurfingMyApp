/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service;

import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalFlowDTO;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleDTO;
import java.util.List;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

public interface ApprovalBasedType {
  void process(
      ApprovalRuleDTO approvalRuleDTO,
      int index,
      ApprovalFlow approvalFlow,
      List<ApprovalFlowDTO> approvalFlows);

  void process(
      ApprovalRuleDTO approvalRuleDTO,
      ApprovalFlow approvalFlow,
      List<ApprovalFlowDTO> approvalFlows);

  String getNextApproval(ApprovalRuleDTO approvalRuleDTO);

  String getNextApproval(ApprovalRuleDTO approvalRuleDTO, int index);

  ApprovalRuleDTO.ApprovalBasedType getType();
}
