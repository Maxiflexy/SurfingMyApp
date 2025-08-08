/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service.basedtype;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_TREATED_ERROR;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getLoggedInUsername;

import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalFlowDTO;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleDTO;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalBasedType;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

@Service("user")
@RequiredArgsConstructor
@Transactional
public class UserBasedApprovalService implements ApprovalBasedType {
  private final MessagePropertyConfig messagePropertyConfig;
  private static final ApprovalRuleDTO.ApprovalBasedType APPROVAL_BASED_TYPE =
      ApprovalRuleDTO.ApprovalBasedType.USER;

  @Override
  public String getNextApproval(ApprovalRuleDTO approvalRuleDTO) {
    return approvalRuleDTO.getApprovals().getUsernames().getFirst();
  }

  @Override
  public String getNextApproval(ApprovalRuleDTO approvalRuleDTO, int index) {
    if (index >= approvalRuleDTO.getApprovals().getRoles().size())
      return approvalRuleDTO.getApprovals().getRoles().getFirst();
    return approvalRuleDTO.getApprovals().getUsernames().get(index);
  }

  @Override
  public void process(
      ApprovalRuleDTO approvalRuleDTO,
      int index,
      ApprovalFlow approvalFlow,
      List<ApprovalFlowDTO> approvalFlows) {
    ensureNoDuplicateApproval(approvalFlow, approvalFlows);
    if (approvalRuleDTO.isSequentialApproval()) {
      String currentApproval;
      if (index >= approvalRuleDTO.getApprovals().getRoles().size())
        currentApproval = approvalRuleDTO.getApprovals().getRoles().getFirst();
      else currentApproval = approvalRuleDTO.getApprovals().getRoles().get(index);
      String loggedInUserRole = getLoggedInUsername();
      if (!currentApproval.equals(loggedInUserRole))
        throw new OmnexaException(
            messagePropertyConfig.getApprovalMessage(DENIED), HttpStatus.FORBIDDEN);
      approvalFlow.setRoleBasedApproval(false);
    } else validate(approvalRuleDTO.getApprovals().getUsernames());
  }

  @Override
  public ApprovalRuleDTO.ApprovalBasedType getType() {
    return APPROVAL_BASED_TYPE;
  }

  @Override
  public void process(
      ApprovalRuleDTO approvalRuleDTO,
      ApprovalFlow approvalFlow,
      List<ApprovalFlowDTO> approvalFlows) {
    ensureNoDuplicateApproval(approvalFlow, approvalFlows);
    validate(approvalRuleDTO.getApprovals().getUsernames());
    approvalFlow.setRoleBasedApproval(false);
  }

  private void ensureNoDuplicateApproval(
      ApprovalFlow approvalFlow, List<ApprovalFlowDTO> approvalFlows) {
    String currentApprovalUser =
        approvalFlow.getApprovalUsername(); // Get the user attempting to approve
    // Check if the user has already approved in previous flows
    boolean alreadyApproved =
        approvalFlows.stream()
            .anyMatch(flow -> flow.getApprovalUsername().equals(currentApprovalUser));

    if (alreadyApproved) {
      throw new OmnexaException(
          messagePropertyConfig.getApprovalMessage(SYSTEM_DEFAULT_TREATED_ERROR),
          HttpStatus.FORBIDDEN);
    }
  }

  private void validate(List<String> base) {
    String loggedInUserUserName = getLoggedInUsername();
    if (loggedInUserUserName.equalsIgnoreCase("SYSTEM") || !base.contains(loggedInUserUserName))
      throw new OmnexaException(
          messagePropertyConfig.getApprovalMessage(DENIED), HttpStatus.FORBIDDEN);
  }
}
