/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.dto;

import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-06(Thu)-2025
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO {
  private String organizationId;
  private String platform;
  private String approvalRequestType;
  private ApprovalRuleDTO approvalRuleDTO;
}
