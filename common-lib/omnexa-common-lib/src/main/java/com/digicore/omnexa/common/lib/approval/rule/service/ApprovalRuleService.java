/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service;

import static com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus.INITIATED;
import static com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleConfigDTO.APPROVAL_RULE_CONFIG_DTO;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.module.ModuleConstant.APPROVAL_RULE;
import static com.digicore.omnexa.common.lib.util.RequestUtil.*;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalRule;
import com.digicore.omnexa.common.lib.approval.rule.data.repository.ApprovalFlowRepository;
import com.digicore.omnexa.common.lib.approval.rule.data.repository.ApprovalRuleRepository;
import com.digicore.omnexa.common.lib.approval.rule.dto.ActivityTypeDTO;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleConfigDTO;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleDTO;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.FeaturePropertyConfig;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.MoneyUtil;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */
@EntityScan("com.digicore.omnexa.common.lib.approval.rule.data.model")
@EnableJpaRepositories(
    basePackages = {"com.digicore.omnexa.common.lib.approval.rule.data.repository"})
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApprovalRuleService {
  private final ApprovalRuleRepository approvalRuleRepository;
  private final Map<String, ApprovalFlowType> approvalFlowTypes;
  private final MessagePropertyConfig messagePropertyConfig;
  private final ApprovalFlowRepository approvalFlowRepository;
  private final FeaturePropertyConfig featurePropertyConfig;

  //  private final ApprovalRuleNotificationService<NotificationRequestDTO>
  //      approvalRuleNotificationService;

  public void validateInitiateRule(
      String activity, String module, ApprovalRequest approvalRequest, String requestData) {
    List<ApprovalRule> approvalRules =
        approvalRuleRepository.findAllByIsDeletedFalseAndActivityAndModule(activity, module);
    if (!nullOrEmpty(approvalRules)) {
      ApprovalRuleDTO approvalRuleDTO = getApprovalRuleDTO(approvalRules, requestData);

      if (userRoleIsNotConfigured(approvalRuleDTO.getInitiators().getRoles())) {
        throw new OmnexaException(
            messagePropertyConfig.getApprovalMessage(DENIED), HttpStatus.BAD_REQUEST);
      }
      approvalRequest.setRequiresWorkFlow(true);
      ApprovalFlow approvalFlow = new ApprovalFlow();
      approvalFlow.setApprovalName(getValueFromAccessToken("name"));
      approvalFlow.setApprovalRole(getValueFromAccessToken("role"));
      approvalFlow.setApprovalUsername(getLoggedInUsername());
      approvalFlow.setApprovalRequest(approvalRequest);
      approvalFlow.setRoleBasedApproval(
          "role".equalsIgnoreCase(approvalRuleDTO.getApprovalBasedType().toString()));
      approvalFlow.setStatus(INITIATED);
      approvalFlow.setNextApproval(
          getApprovalFlowType(approvalRuleDTO).getNextApproval(approvalRuleDTO));
      approvalFlowRepository.save(approvalFlow);
      if (approvalRuleDTO.getMinApprovalsRequired() > 1) approvalRequest.setNextApprovalIndex(0);
      //        approvalRuleNotificationService.sendNotification(NotificationRequestDTO.builder()
      //                        .approvalRequestType(approvalRequest.getApprovalRequestType())
      //                        .organizationId(RequestContextHolder.get().getOrganizationId())
      //                        .platform(RequestContextHolder.get().getPlatform())
      //                        .approvalRuleDTO(ruleConfig)
      //                .build());
    }
  }

  private ApprovalRuleDTO getApprovalRuleDTO(List<ApprovalRule> approvalRules, String requestData) {
    ApprovalRule approvalRule;
    if (approvalRules.size() > 1) {
      Optional<ApprovalRule> approvalRuleOptional =
          approvalRules.stream().filter(approvalRule1 -> !approvalRule1.isGlobal()).findFirst();

      approvalRule = approvalRuleOptional.orElse(approvalRules.getFirst());
    } else {
      approvalRule = approvalRules.getFirst();
    }

    if (approvalRule == null) {
      throw new OmnexaException(
          messagePropertyConfig.getApprovalMessage(CONFIGURATION_REQUIRED), HttpStatus.BAD_REQUEST);
    }

    ApprovalRuleConfigDTO ruleConfig =
        RequestUtil.getGsonMapper()
            .fromJson(approvalRule.getRuleConfig(), ApprovalRuleConfigDTO.class);
    List<ApprovalRuleDTO> approvalRuleDTOS = ruleConfig.getApprovalRules();
    ApprovalRuleDTO approvalRuleDTO;

    if (ruleConfig.isSupportThresholdConfiguration()) {
      approvalRuleDTO = getApprovalRuleDTO(requestData, approvalRuleDTOS);
    } else {
      if (approvalRuleDTOS.isEmpty()) {
        throw new OmnexaException(
            messagePropertyConfig.getApprovalMessage(CONFIGURATION_REQUIRED),
            HttpStatus.BAD_REQUEST);
      }
      approvalRuleDTO = approvalRuleDTOS.getFirst();
    }
    return approvalRuleDTO;
  }

  private ApprovalRuleDTO getApprovalRuleDTO(
      String requestData, List<ApprovalRuleDTO> approvalRuleDTOS) {
    ApprovalRuleDTO approvalRuleDTO;
    long requestAmount = getTotalAmountInMinor(requestData);
    BigDecimal amountToCheck = MoneyUtil.convertMinorToMajor(requestAmount);

    approvalRuleDTO =
        approvalRuleDTOS.stream()
            .filter(
                existingRule -> {
                  BigDecimal lowerBound =
                      MoneyUtil.getAmountFromAmountInMinor(existingRule.getLowerBoundInMinor());
                  BigDecimal upperBound =
                      MoneyUtil.getAmountFromAmountInMinor(existingRule.getUpperBoundInMinor());
                  return amountToCheck.compareTo(lowerBound) >= 0
                      && amountToCheck.compareTo(upperBound) <= 0;
                })
            .findFirst()
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig.getApprovalMessage(CONFIGURATION_REQUIRED),
                        HttpStatus.BAD_REQUEST));
    return approvalRuleDTO;
  }

  public boolean validateApprovalRule(
      String activity,
      String module,
      ApprovalRequest approvalRequest,
      ApprovalDecisionDTO approvalDecisionDTO)
      throws InterruptedException {
    List<ApprovalRule> approvalRules =
        approvalRuleRepository.findAllByIsDeletedFalseAndActivityAndModule(activity, module);

    if (nullOrEmpty(approvalRules)) {
      // by default approval rule is not required
      return true;
    }

    ApprovalRuleDTO approvalRuleDTO =
        getApprovalRuleDTO(approvalRules, approvalRequest.getDataToUpdate());

    ApprovalFlowType approvalFlowType = getApprovalFlowType(approvalRuleDTO);

    return approvalFlowType.process(approvalRuleDTO, approvalRequest, approvalDecisionDTO);
  }

  private ApprovalFlowType getApprovalFlowType(ApprovalRuleDTO approvalRuleDTO) {
    ApprovalFlowType approvalFlowType =
        approvalFlowTypes.getOrDefault(
            approvalRuleDTO.getApprovalFlowType().toString().toLowerCase(), null);

    if (approvalFlowType == null) {
      throw new OmnexaException(
          messagePropertyConfig.getApprovalMessage(CONFIGURATION_REQUIRED), HttpStatus.BAD_REQUEST);
    }
    return approvalFlowType;
  }

  @MakerChecker(
      checkerPermission = "approve-create-approval-rule",
      makerPermission = "create-approval-rule",
      requestClassName = APPROVAL_RULE_CONFIG_DTO,
      activity = CREATE,
      module = APPROVAL_RULE)
  public Object createApprovalRule(Object initialData, Object requestDTO, Object... args) {
    ApprovalRuleConfigDTO approvalRuleDTO = (ApprovalRuleConfigDTO) requestDTO;
    ApprovalRule approvalRule = new ApprovalRule();
    BeanUtilWrapper.copyNonNullProperties(requestDTO, approvalRule);
    approvalRule.setRuleConfig(RequestUtil.getGsonMapper().toJson(approvalRuleDTO));
    approvalRuleRepository.save(approvalRule);
    return Optional.empty();
  }

  @Async
  public void createApprovalRule(ApprovalRuleConfigDTO approvalRuleConfigDTO) {
    ApprovalRule approvalRule = new ApprovalRule();
    BeanUtilWrapper.copyNonNullProperties(approvalRuleConfigDTO, approvalRule);
    approvalRule.setRuleConfig(RequestUtil.getGsonMapper().toJson(approvalRuleConfigDTO));
    approvalRuleRepository.save(approvalRule);
  }

  @Async
  public void createApprovalRule(
      List<ApprovalRuleConfigDTO> approvalRuleConfigDTO, String organizationId, String platform) {
    List<ApprovalRule> approvalRules = new ArrayList<>();
    approvalRuleConfigDTO.forEach(
        approvalRuleConfigDTO1 -> {
          ApprovalRule approvalRule = new ApprovalRule();
          BeanUtilWrapper.copyNonNullProperties(approvalRuleConfigDTO1, approvalRule);
          approvalRule.setRuleConfig(RequestUtil.getGsonMapper().toJson(approvalRuleConfigDTO1));
          approvalRules.add(approvalRule);
        });
    approvalRuleRepository.saveAll(approvalRules);
  }

  public void deleteCustomerApprovalRules() {
    List<ApprovalRule> approvalRules =
        approvalRuleRepository.findAllByIsDeletedFalse().stream()
            .peek(approvalRule -> approvalRule.setDeleted(true))
            .toList();
    approvalRuleRepository.saveAll(approvalRules);
  }

  @MakerChecker(
      checkerPermission = "approve-edit-approval-rule",
      makerPermission = "edit-approval-rule",
      requestClassName = APPROVAL_RULE_CONFIG_DTO,
      activity = EDIT,
      module = APPROVAL_RULE)
  public Object editApprovalRule(Object initialData, Object requestDTO, Object... args) {
    ApprovalRuleConfigDTO approvalRuleDTO = (ApprovalRuleConfigDTO) requestDTO;
    ApprovalRule approvalRule =
        approvalRuleRepository
            .findFirstByIsDeletedFalseAndActivityAndModuleAndGlobal(
                approvalRuleDTO.getActivity(),
                approvalRuleDTO.getModule(),
                approvalRuleDTO.isGlobal())
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig.getApprovalMessage(NOT_FOUND),
                        HttpStatus.BAD_REQUEST));
    approvalRule.setRuleConfig(
        RequestUtil.getGsonMapper().toJson(approvalRuleDTO.getApprovalRules()));
    approvalRuleRepository.save(approvalRule);
    return Optional.empty();
  }

  public List<ApprovalRuleConfigDTO> getAllApprovalRule() {
    return approvalRuleRepository.findAllByIsDeletedFalse().stream()
        .map(
            approvalRule ->
                RequestUtil.getGsonMapper()
                    .fromJson(approvalRule.getRuleConfig(), ApprovalRuleConfigDTO.class))
        .toList();
  }

  public List<ApprovalRuleConfigDTO> getAllApprovalRule(String module) {
    return approvalRuleRepository.findAllByIsDeletedFalseAndModule(module).stream()
        .map(
            approvalRule ->
                RequestUtil.getGsonMapper()
                    .fromJson(approvalRule.getRuleConfig(), ApprovalRuleConfigDTO.class))
        .toList();
  }

  public ApprovalRuleConfigDTO getApprovalRule(String activity, String module, boolean global) {
    ApprovalRule approvalRule =
        approvalRuleRepository
            .findFirstByIsDeletedFalseAndActivityAndModuleAndGlobal(activity, module, global)
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig.getApprovalMessage(NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

    if (nullOrEmpty(approvalRule.getRuleConfig())) return new ApprovalRuleConfigDTO();

    return RequestUtil.getGsonMapper()
        .fromJson(approvalRule.getRuleConfig(), ApprovalRuleConfigDTO.class);
  }

  public ActivityTypeDTO getActivityType() {
    return ActivityTypeDTO.builder().modules(featurePropertyConfig.getModules()).build();
  }

  private boolean userRoleIsNotConfigured(List<String> allowedRoles) {
    String loggedInUserRole = getValueFromAccessToken("role");
    return loggedInUserRole == null || !allowedRoles.contains(loggedInUserRole);
  }
}
