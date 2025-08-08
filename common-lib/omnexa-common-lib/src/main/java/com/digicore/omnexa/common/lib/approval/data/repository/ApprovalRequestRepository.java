/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.data.repository;

import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
  Page<ApprovalRequest> findByPermissionInAndStatusInOrderByCreatedOnDesc(
      List<String> permissions, List<ApprovalRequestStatus> statuses, Pageable pageable);

  Page<ApprovalRequest> findByPermissionInAndStatusOrderByCreatedOnDesc(
      List<String> permissions, ApprovalRequestStatus status, Pageable pageable);

  Page<ApprovalRequest> findByStatusAndApprovalUsernameOrderByCreatedOnDesc(
      ApprovalRequestStatus status, String username, Pageable pageable);

  Page<ApprovalRequest>
      findByPermissionInAndCreatedOnBetweenOrApprovedDateBetweenOrderByCreatedOnDesc(
          List<String> permissions,
          LocalDateTime createdOnStartDate,
          LocalDateTime createdOnEndDate,
          LocalDateTime approvedDateStartDate,
          LocalDateTime approvedDateEndDate,
          Pageable pageable);

  Page<ApprovalRequest>
      findByPermissionInAndStatusAndCreatedOnBetweenOrApprovedDateBetweenOrderByCreatedOnDesc(
          List<String> permissions,
          ApprovalRequestStatus status,
          LocalDateTime createdOnStartDate,
          LocalDateTime createdOnEndDate,
          LocalDateTime approvedDateStartDate,
          LocalDateTime approvedDateEndDate,
          Pageable pageable);

  Page<ApprovalRequest>
      findByPermissionInAndStatusOrApprovalRequestTypeContainingIgnoreCaseOrderByCreatedOnDesc(
          List<String> permissions,
          ApprovalRequestStatus status,
          String approvalRequestType,
          Pageable pageable);

  Page<ApprovalRequest>
      findByPermissionInAndStatusAndApprovalRequestTypeContainingIgnoreCaseOrderByCreatedOnDesc(
          List<String> permissions,
          ApprovalRequestStatus status,
          String approvalRequestType,
          Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          "SELECT * FROM approval_requests  WHERE permission IN (:permissions) and treated_date is not null")
  Page<ApprovalRequest> findByPermissionAndRequestIsTreated(
      @Param("permissions") List<String> permissions, Pageable pageable);
}
