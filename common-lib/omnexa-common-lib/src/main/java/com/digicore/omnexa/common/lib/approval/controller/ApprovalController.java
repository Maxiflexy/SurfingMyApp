/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.PAGE_SIZE_DESCRIPTION;
import static com.digicore.omnexa.common.lib.swagger.constant.approval.ApprovalSwaggerDocConstant.*;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalDecisionDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import com.digicore.omnexa.common.lib.approval.interceptor.ApprovalRequestInterceptor;
import com.digicore.omnexa.common.lib.approval.service.ApprovalRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-29(Wed)-2025
 */

@RestController
@RequestMapping(API_V1 + APPROVAL_API)
@RequiredArgsConstructor
@Tag(name = APPROVAL_CONTROLLER_TITLE, description = APPROVAL_CONTROLLER_DESCRIPTION)
public class ApprovalController {
  private final ApprovalRequestInterceptor approvalRequestInterceptor;
  private final ApprovalRequestService approvalRequestService;

  @PostMapping("treat-request")
  @PreAuthorize("hasAuthority('treat-requests')")
  @Operation(
      summary = APPROVAL_CONTROLLER_APPROVE_REQUEST_TITLE,
      description = APPROVAL_CONTROLLER_APPROVE_REQUEST_DESCRIPTION)
  public ResponseEntity<Object> approveRequest(
      @RequestBody ApprovalDecisionDTO approvalDecisionDTO) {
    approvalDecisionDTO.setRequestType(
        approvalDecisionDTO.getStatus().equals(ApprovalStatus.APPROVED)
            ? "approveRequest"
            : "declineRequest");
    return ControllerResponse.buildSuccessResponse(
        approvalRequestInterceptor.process(approvalDecisionDTO), null);
  }

  @GetMapping("get-request-{requestId}")
  @PreAuthorize("hasAuthority('treat-requests')")
  @Operation(
      summary = APPROVAL_CONTROLLER_GET_REQUEST_TITLE,
      description = APPROVAL_CONTROLLER_GET_REQUEST_DESCRIPTION)
  public ResponseEntity<Object> getRequest(@PathVariable Long requestId) {
    return ControllerResponse.buildSuccessResponse(
        approvalRequestService.getRequest(requestId), null);
  }

  @GetMapping("get-treated-request")
  @PreAuthorize("hasAuthority('treat-requests')")
  @Operation(
      summary = APPROVAL_CONTROLLER_GET_TREATED_REQUEST_TITLE,
      description = APPROVAL_CONTROLLER_GET_TREATED_REQUEST_DESCRIPTION)
  public ResponseEntity<Object> getTreatedRequest(
      @RequestParam(name = PAGE_NUMBER, defaultValue = PAGE_NUMBER) int pageNumber,
      @RequestParam(name = PAGE_NUMBER, defaultValue = PAGE_NUMBER) int pageSize) {
    return ControllerResponse.buildSuccessResponse(
        approvalRequestService.getRequests(pageNumber, pageSize, ApprovalRequestStatus.EXECUTED),
        null);
  }

  @GetMapping("get-declined-request")
  @PreAuthorize("hasAuthority('treat-requests')")
  @Operation(
      summary = APPROVAL_CONTROLLER_GET_TREATED_REQUEST_TITLE,
      description = APPROVAL_CONTROLLER_GET_TREATED_REQUEST_DESCRIPTION)
  public ResponseEntity<Object> getDeclinedRequest(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {
    return ControllerResponse.buildSuccessResponse(
        approvalRequestService.getRequests(pageNumber, pageSize, ApprovalRequestStatus.DECLINED),
        null);
  }

  @GetMapping("get-pending-request")
  @PreAuthorize("hasAuthority('treat-requests')")
  @Operation(
      summary = APPROVAL_CONTROLLER_GET_PENDING_REQUEST_TITLE,
      description = APPROVAL_CONTROLLER_GET_PENDING_REQUEST_DESCRIPTION)
  public ResponseEntity<Object> getPendingRequest(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {
    return ControllerResponse.buildSuccessResponse(
        approvalRequestService.getRequests(pageNumber, pageSize), null);
  }
}
