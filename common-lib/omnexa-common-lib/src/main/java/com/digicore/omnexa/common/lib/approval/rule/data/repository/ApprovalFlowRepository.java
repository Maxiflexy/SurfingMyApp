/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.data.repository;

import static com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalFlowDTO.APPROVAL_FLOW_DTO;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalFlow;
import com.digicore.omnexa.common.lib.approval.rule.dto.ApprovalFlowDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

public interface ApprovalFlowRepository extends JpaRepository<ApprovalFlow, Long> {
  @Query(
      "SELECT COUNT(a) FROM ApprovalFlow a WHERE a.approvalRequest.id = :approvalRequestId AND a.status = :status")
  long countByRequestIdAndStatus(
      @Param("approvalRequestId") Long approvalRequestId, @Param("status") ApprovalStatus status);

  @Query(
      "SELECT new "
          + APPROVAL_FLOW_DTO
          + "(a.approvalUsername) FROM ApprovalFlow a WHERE a.approvalRequest.id = :approvalRequestId AND a.status = :status")
  List<ApprovalFlowDTO> findByApprovalRequestAndStatus(
      @Param("approvalRequestId") Long approvalRequestId, @Param("status") ApprovalStatus status);
}
