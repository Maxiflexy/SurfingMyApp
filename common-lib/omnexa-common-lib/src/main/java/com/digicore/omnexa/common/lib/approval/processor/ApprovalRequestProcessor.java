/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.processor;

import static com.digicore.omnexa.common.lib.util.RequestUtil.getLoggedInUsername;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getValueFromAccessToken;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.data.repository.ApprovalRequestRepository;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalRequestDTO;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalResponseDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.approval.interceptor.ApprovalRequestInterceptor;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestHandler;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestType;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import com.digicore.omnexa.common.lib.audit.service.OmnexaAuditLogService;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-29(Wed)-2025
 */

@RequiredArgsConstructor
@RequestHandler(type = RequestHandlerType.PROCESS_MAKER_REQUESTS)
public class ApprovalRequestProcessor {
  private final ApprovalRequestRepository approvalRequestRepository;

  private final OmnexaAuditLogService regulatoryLoggingService;

  private final ApprovalRequestInterceptor approvalRequestInterceptor;

  private static final String INVALID_REQUEST_ID_MESSAGE = "Invalid request id supplied";
  private static final String INVALID_REQUEST_ID_CODE = "MK_001";

  private static final String REQUEST_TREATED_MESSAGE = "Request already treated";
  private static final String REQUEST_TREATED_CODE = "MK_002";

  @RequestType(name = "approveRequest")
  public Object approveRequest(Object requestsDTO) {
    ApprovalDecisionDTO decisionDTO = (ApprovalDecisionDTO) requestsDTO;
    ApprovalRequest approvalRequest =
        approvalRequestRepository
            .findById(Long.valueOf(decisionDTO.getRequestId()))
            .orElseThrow(
                () ->
                    new OmnexaException(
                        HttpStatus.BAD_REQUEST,
                        new ApiError(INVALID_REQUEST_ID_MESSAGE, INVALID_REQUEST_ID_CODE)));
    if (!ApprovalRequestStatus.NOT_TREATED.equals(approvalRequest.getStatus())
        && !ApprovalRequestStatus.PENDING.equals(approvalRequest.getStatus()))
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError(REQUEST_TREATED_MESSAGE, REQUEST_TREATED_CODE));
    decisionDTO.setRequestType(approvalRequest.getApprovalRequestType());
    return approvalRequestInterceptor.process(decisionDTO);
  }

  @RequestType(name = "declineRequest")
  public Object declineRequest(Object requestsDTO) {
    ApprovalDecisionDTO decisionDTO = (ApprovalDecisionDTO) requestsDTO;
    ApprovalRequest approvalRequest =
        approvalRequestRepository
            .findById(Long.valueOf(decisionDTO.getRequestId()))
            .orElseThrow(
                () ->
                    new OmnexaException(
                        HttpStatus.BAD_REQUEST,
                        new ApiError(INVALID_REQUEST_ID_MESSAGE, INVALID_REQUEST_ID_CODE)));
    if (!ApprovalRequestStatus.NOT_TREATED.equals(approvalRequest.getStatus()))
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError("Request already approved", "MK_003"));
    approvalRequest.setApproved(false);
    approvalRequest.setStatus(ApprovalRequestStatus.DECLINED);
    approvalRequest.setApprovedDate(LocalDateTime.now());
    approvalRequest.setApprovalUsername(getLoggedInUsername());
    approvalRequestRepository.save(approvalRequest);
    regulatoryLoggingService.log(
        getValueFromAccessToken("role"),
        getValueFromAccessToken("email"),
        getValueFromAccessToken("name"),
        "DECLINE",
        null,
        approvalRequest);
    ApprovalRequestDTO approvalRequestDTO = new ApprovalRequestDTO();
    BeanUtilWrapper.copyNonNullProperties(approvalRequest, approvalRequestDTO);
    return ApprovalResponseDTO.builder()
        .description("Request submitted successfully")
        .requestStatus(ApprovalRequestStatus.DECLINED)
        .build();
  }
}
