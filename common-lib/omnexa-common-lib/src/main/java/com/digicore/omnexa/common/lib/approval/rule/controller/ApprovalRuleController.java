/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.approval.ApprovalRuleSwaggerDocConstant.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleConfigDTO;
import com.digicore.omnexa.common.lib.approval.rule.service.ApprovalRuleService;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */
@RestController
@RequestMapping(API_V1 + APPROVAL_RULE_API)
@RequiredArgsConstructor
@Tag(name = APPROVAL_RULE_CONTROLLER_TITLE, description = APPROVAL_RULE_CONTROLLER_DESCRIPTION)
public class ApprovalRuleController {
  private final ApprovalRuleService approvalRuleService;

  @PostMapping(APPROVAL_RULE_CREATE_API)
  @PreAuthorize("hasAuthority('create-approval-rule')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_CREATE_RULE_TITLE,
      description = APPROVAL_RULE_CONTROLLER_CREATE_RULE_DESCRIPTION)
  public ResponseEntity<Object> createApprovalRule(
      @RequestBody ApprovalRuleConfigDTO approvalRuleDTO) throws OmnexaException {
    approvalRuleService.createApprovalRule(null, approvalRuleDTO);
    return ControllerResponse.buildSuccessResponse();
  }

  @PostMapping(APPROVAL_RULE_EDIT_API)
  @PreAuthorize("hasAuthority('edit-approval-rule')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_EDIT_RULE_TITLE,
      description = APPROVAL_RULE_CONTROLLER_EDIT_RULE_DESCRIPTION)
  public ResponseEntity<Object> editApprovalRule(@RequestBody ApprovalRuleConfigDTO approvalRuleDTO)
      throws OmnexaException {
    approvalRuleService.editApprovalRule(null, approvalRuleDTO);
    return ControllerResponse.buildSuccessResponse();
  }

  @GetMapping(APPROVAL_RULE_RETRIEVE_ALL_ACTIVITY_TYPES_API)
  @PreAuthorize("hasAuthority('view-activity-types')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_GET_ACTIVITY_TITLE,
      description = APPROVAL_RULE_CONTROLLER_GET_ACTIVITY_DESCRIPTION)
  public ResponseEntity<Object> getActivityType() throws OmnexaException {
    return ControllerResponse.buildSuccessResponse(approvalRuleService.getActivityType());
  }

  @GetMapping("retrieve-all")
  @PreAuthorize("hasAuthority('view-approval-rule')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_TITLE,
      description = APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_DESCRIPTION)
  public ResponseEntity<Object> viewAllApprovalRule() throws OmnexaException {
    return ControllerResponse.buildSuccessResponse(approvalRuleService.getAllApprovalRule());
  }

  @GetMapping("fetch-all")
  @PreAuthorize("hasAuthority('view-approval-rule')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_TITLE,
      description = APPROVAL_RULE_CONTROLLER_ALL_GET_RULES_DESCRIPTION)
  public ResponseEntity<Object> viewAllApprovalRule(@RequestParam String module)
      throws OmnexaException {
    return ControllerResponse.buildSuccessResponse(approvalRuleService.getAllApprovalRule(module));
  }

  @GetMapping("retrieve")
  @PreAuthorize("hasAuthority('view-approval-rule')")
  @Operation(
      summary = APPROVAL_RULE_CONTROLLER_GET_RULES_TITLE,
      description = APPROVAL_RULE_CONTROLLER_GET_RULES_DESCRIPTION)
  public ResponseEntity<Object> viewApprovalRule(
      @RequestParam String activity, @RequestParam String module, @RequestParam boolean global)
      throws OmnexaException {
    return ControllerResponse.buildSuccessResponse(
        approvalRuleService.getApprovalRule(activity, module, global));
  }
}
