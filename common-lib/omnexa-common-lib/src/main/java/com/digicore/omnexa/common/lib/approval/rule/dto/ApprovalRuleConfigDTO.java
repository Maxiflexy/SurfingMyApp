/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.dto;

import java.util.List;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-06(Thu)-2025
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRuleConfigDTO {
  public static final String APPROVAL_RULE_CONFIG_DTO =
      "com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalRuleConfigDTO";
  private boolean global;
  private boolean supportThresholdConfiguration = false;
  private String activity;
  private String module;
  private List<ApprovalRuleDTO> approvalRules;
}
