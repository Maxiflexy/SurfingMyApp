/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service.flowtype;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.CONFIGURATION_REQUIRED;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.INVALID;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getLoggedInUsername;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getValueFromAccessToken;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.approval.rule.data.repository.ApprovalFlowRepository;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleDTO;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalBasedType;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalFlowType;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */
@Service("multi")
@RequiredArgsConstructor
@Transactional
public class MultiApprovalService implements ApprovalFlowType {
  private final Map<String, ApprovalBasedType> approvalBasedTypes;
  private final ApprovalFlowRepository approvalFlowRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  @Override
  public String getNextApproval(ApprovalRuleDTO approvalRuleDTO) {
    ApprovalBasedType approvalBasedType =
        approvalBasedTypes.get(approvalRuleDTO.getApprovalBasedType().toString().toLowerCase());
    if (approvalBasedType != null) {
      return approvalBasedType.getNextApproval(approvalRuleDTO);
    }
    throw new OmnexaException(
        messagePropertyConfig.getApprovalMessage(CONFIGURATION_REQUIRED), HttpStatus.BAD_REQUEST);
  }

  @Override
  public boolean process(
      ApprovalRuleDTO approvalRuleDTO,
      ApprovalRequest approvalRequest,
      ApprovalDecisionDTO approvalDecisionDTO)
      throws InterruptedException {
    boolean result = false;
    ApprovalFlow approvalFlow = new ApprovalFlow();
    approvalFlow.setApprovalName(getValueFromAccessToken("name"));
    approvalFlow.setApprovalRole(getValueFromAccessToken("role"));
    approvalFlow.setApprovalUsername(getLoggedInUsername());
    approvalFlow.setApprovalRequest(approvalRequest);
    approvalFlow.setStatus(approvalDecisionDTO.getStatus());
    if (approvalDecisionDTO.getStatus().equals(ApprovalStatus.APPROVED)) {
      ApprovalBasedType approvalBasedType =
          approvalBasedTypes.get(approvalRuleDTO.getApprovalBasedType().toString().toLowerCase());
      if (approvalBasedType != null) {
        approvalBasedType.process(
            approvalRuleDTO,
            approvalRequest.getNextApprovalIndex(),
            approvalFlow,
            approvalFlowRepository.findByApprovalRequestAndStatus(
                approvalRequest.getId(), ApprovalStatus.APPROVED));
        approvalFlowRepository.save(approvalFlow);
        if (approvalFlowRepository.countByRequestIdAndStatus(
                approvalRequest.getId(), ApprovalStatus.APPROVED)
            < approvalRuleDTO.getMinApprovalsRequired()) {
          approvalRequest.setNextApprovalIndex(approvalRequest.getNextApprovalIndex() + 1);
          approvalFlow.setNextApproval(
              approvalBasedType.getNextApproval(
                  approvalRuleDTO, approvalRequest.getNextApprovalIndex() + 1));
        } else result = true;
      } else {
        throw new OmnexaException(
            messagePropertyConfig.getApprovalMessage(INVALID), HttpStatus.BAD_REQUEST);
      }
    } else if (approvalDecisionDTO.getStatus().equals(ApprovalStatus.DECLINED)) {
      approvalFlow.setReason(approvalDecisionDTO.getReason());
      result = true;
      approvalFlowRepository.save(approvalFlow);
    }
    return result;
  }
}
