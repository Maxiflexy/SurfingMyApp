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
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

@Service("single")
@RequiredArgsConstructor
@Transactional
public class SingleApprovalService implements ApprovalFlowType {
  private final Map<String, ApprovalBasedType> approvalBasedTypes;
  private final RedissonClient redissonClient;
  private final ApprovalFlowRepository approvalFlowRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  @Override
  public boolean process(
      ApprovalRuleDTO approvalRuleDTO,
      ApprovalRequest approvalRequest,
      ApprovalDecisionDTO approvalDecisionDTO)
      throws InterruptedException {
    RLock lock =
        redissonClient.getLock("single-approval-".concat(String.valueOf(approvalRequest.getId())));
    boolean result = false;
    try {
      if (lock.tryLock(5, 30, TimeUnit.SECONDS)) {
        ApprovalBasedType approvalBasedType =
            approvalBasedTypes.get(approvalRuleDTO.getApprovalBasedType().toString().toLowerCase());
        if (approvalBasedType != null) {
          ApprovalFlow approvalFlow = new ApprovalFlow();
          approvalFlow.setApprovalName(getValueFromAccessToken("name"));
          approvalFlow.setApprovalRole(getValueFromAccessToken("role"));
          approvalFlow.setApprovalUsername(getLoggedInUsername());
          approvalFlow.setApprovalRequest(approvalRequest);
          approvalFlow.setStatus(approvalDecisionDTO.getStatus());
          approvalFlow.setNextApproval(null);
          approvalBasedType.process(
              approvalRuleDTO,
              approvalFlow,
              approvalFlowRepository.findByApprovalRequestAndStatus(
                  approvalRequest.getId(), ApprovalStatus.APPROVED));
          approvalFlowRepository.save(approvalFlow);
          result = true;
        } else {
          throw new OmnexaException(
              messagePropertyConfig.getApprovalMessage(INVALID), HttpStatus.BAD_REQUEST);
        }
      }
    } finally {
      lock.unlock();
    }
    return result;
  }

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
}
