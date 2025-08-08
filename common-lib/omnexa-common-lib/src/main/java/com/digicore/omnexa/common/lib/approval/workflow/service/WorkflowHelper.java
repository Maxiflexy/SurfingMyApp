/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.service;

import static com.digicore.omnexa.common.lib.util.RequestUtil.getObjectMapper;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.data.repository.ApprovalRequestRepository;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-17(Mon)-2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowHelper {
  private final ApprovalRequestRepository approvalRequestRepository;

  public Object retrieveRequest(String className, long requestId) {
    ApprovalRequest approvalRequest = approvalRequestRepository.findById(requestId).orElseThrow();
    if (approvalRequest.getApprovedDate() != null) {
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError("requested already treated", "MKT_002"));
    }

    try {
      Class<?> c = Class.forName(className);
      if (approvalRequest.getDataToUpdate() != null
          && !approvalRequest.getDataToUpdate().isEmpty()) {
        return getObjectMapper().readValue(approvalRequest.getDataToUpdate(), c);
      }

    } catch (Exception e) {
      log.error("MKT_007 failed message : {}", e.getMessage());
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError("request retrieval failed", "MKT_007"));
    }
    throw new OmnexaException(
        HttpStatus.BAD_REQUEST, new ApiError("request retrieval failed", "MKT_008"));
  }
}
