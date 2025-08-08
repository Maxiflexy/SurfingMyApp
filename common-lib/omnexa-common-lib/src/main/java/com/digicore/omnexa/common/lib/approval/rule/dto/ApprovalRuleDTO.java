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
 * @createdOn Feb-09(Sun)-2025
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRuleDTO {
  private ApprovalFlowType approvalFlowType;
  private ApprovalBasedType approvalBasedType;
  private Initiators initiators;
  private Approvals approvals;
  private int minApprovalsRequired;
  private boolean sequentialApproval;
  private String lowerBoundInMinor;
  private String upperBoundInMinor;

  @Getter
  @Setter
  public static class Initiators {
    private List<String> roles;
  }

  @Getter
  @Setter
  public static class Approvals {
    private List<String> roles;
    private List<String> usernames;
  }

  public enum ApprovalFlowType {
    SINGLE,
    MULTI
    // ALL
  }

  public enum ApprovalBasedType {
    ROLE,
    USER
  }
}
