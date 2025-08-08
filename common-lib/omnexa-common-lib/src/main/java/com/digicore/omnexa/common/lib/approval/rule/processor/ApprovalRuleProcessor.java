/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.processor;

import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalRuleService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestHandler;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestType;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

@RequestHandler(type = RequestHandlerType.PROCESS_MAKER_REQUESTS)
@RequiredArgsConstructor
public class ApprovalRuleProcessor {
  private final ApprovalRuleService approvalRuleService;

  @RequestType(name = "createApprovalRule")
  public Object createApprovalRule(Object approvalDecisionDTO) {
    return approvalRuleService.createApprovalRule(null, approvalDecisionDTO);
  }

  public Object editApprovalRule(Object approvalDecisionDTO) {
    return approvalRuleService.editApprovalRule(null, approvalDecisionDTO);
  }
}
