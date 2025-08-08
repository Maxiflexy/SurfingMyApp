/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.service;

import static com.digicore.omnexa.common.lib.util.RequestUtil.*;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.approval.data.model.ApprovalRequest;
import com.digicore.omnexa.common.lib.approval.data.repository.ApprovalRequestRepository;
import com.digicore.omnexa.common.lib.approval.dto.ApprovalRequestDTO;
import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-29(Wed)-2025
 */

@EntityScan("com.digicore.omnexa.common.lib.approval.data.model")
@EnableJpaRepositories(basePackages = {"com.digicore.omnexa.common.lib.approval.data.repository"})
@Service
@RequiredArgsConstructor
public class ApprovalRequestService {
  private final ApprovalRequestRepository approvalRequestRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  private static final String INVALID_REQUEST_ID_MESSAGE = "Invalid request id supplied";
  private static final String INVALID_REQUEST_ID_CODE = "MK_001";

  public PaginatedResponse<ApprovalRequestDTO> getRequests(
      int page, int size, ApprovalRequestStatus status) {
    Page<ApprovalRequest> approvalRequestPage =
        approvalRequestRepository.findByStatusAndApprovalUsernameOrderByCreatedOnDesc(
            status, getLoggedInUsername(), getPageable(page, size));
    return getPaginatedResponse(approvalRequestPage);
  }

  private static PaginatedResponse<ApprovalRequestDTO> getPaginatedResponse(
      Page<ApprovalRequest> approvalRequestPage) {
    return PaginatedResponse.<ApprovalRequestDTO>builder()
        .content(
            approvalRequestPage.getContent().stream()
                .map(
                    approvalRequest -> {
                      ApprovalRequestDTO approvalRequestsDTO = new ApprovalRequestDTO();
                      BeanUtilWrapper.copyNonNullProperties(approvalRequest, approvalRequestsDTO);
                      approvalRequestsDTO.setTreatDate(approvalRequest.getApprovedDate());
                      approvalRequestsDTO.setApprovalRequestType(
                          camelCaseToSentence(approvalRequest.getApprovalRequestType()));
                      if (approvalRequest.getApprovalUsername() != null)
                        approvalRequestsDTO.setApprovalUsername(
                            approvalRequest.getApprovalUsername());
                      return approvalRequestsDTO;
                    })
                .toList())
        .currentPage(approvalRequestPage.getNumber() + 1)
        .isFirstPage(approvalRequestPage.isFirst())
        .isLastPage(approvalRequestPage.isLast())
        .totalItems(approvalRequestPage.getTotalElements())
        .totalPages(approvalRequestPage.getTotalPages())
        .build();
  }

  public PaginatedResponse<ApprovalRequestDTO> getRequests(int page, int size) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return getRequests(
        auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
        page,
        size,
        ApprovalRequestStatus.NOT_TREATED,
        ApprovalRequestStatus.PENDING);
  }

  public Object getRequest(Long id) {
    ApprovalRequest approvalRequests =
        approvalRequestRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new OmnexaException(
                        HttpStatus.BAD_REQUEST,
                        new ApiError(INVALID_REQUEST_ID_MESSAGE, INVALID_REQUEST_ID_CODE)));
    ApprovalRequestDTO approvalRequestDTO = new ApprovalRequestDTO();
    BeanUtilWrapper.copyNonNullProperties(approvalRequests, approvalRequestDTO);
    return approvalRequestDTO;
  }

  public PaginatedResponse<ApprovalRequestDTO> getRequests(
      List<String> loggedInUserPermissions,
      int page,
      int size,
      ApprovalRequestStatus status,
      ApprovalRequestStatus pendingStatus) {
    Page<ApprovalRequest> approvalRequestPage =
        approvalRequestRepository.findByPermissionInAndStatusInOrderByCreatedOnDesc(
            loggedInUserPermissions, List.of(status, pendingStatus), getPageable(page, size));
    List<ApprovalRequest> approvalRequests = approvalRequestPage.getContent();
    if (!nullOrEmpty(approvalRequests)) {
      String loggedInUserRole = getValueFromAccessToken("role");
      String loggedInUsername = getLoggedInUsername();

      List<ApprovalRequest> filteredRequests =
          approvalRequests.stream()
              .filter(
                  approvalRequest ->
                      !approvalRequest.isRequiresWorkFlow()
                          || // Include requests that don’t require workflow
                          (approvalRequest.getApprovalFlows() != null
                              && approvalRequest.getApprovalFlows().stream()
                                  .anyMatch(
                                      approvalFlow ->
                                          (approvalFlow.isRoleBasedApproval()
                                                  && loggedInUserRole.equals(
                                                      approvalFlow
                                                          .getNextApproval()) // User's role matches
                                                  && approvalRequest
                                                      .getApprovalFlows()
                                                      .stream() // Ensure user hasn't approved
                                                      // before
                                                      .noneMatch(
                                                          flow ->
                                                              flow.getApprovalUsername() != null
                                                                  && flow.getApprovalUsername()
                                                                      .equals(loggedInUsername)))
                                              || (!approvalFlow.isRoleBasedApproval()
                                                  && loggedInUsername.equals(
                                                      approvalFlow.getNextApproval())))))
              .toList();

      return getApprovalRequestDTOPaginatedResponseDTO(filteredRequests, approvalRequestPage);
    }
    return getApprovalRequestDTOPaginatedResponseDTO(approvalRequests, approvalRequestPage);
  }

  public PaginatedResponse<ApprovalRequestDTO> getRequests(
      List<String> loggedInUserPermissions, int page, int size, ApprovalRequestStatus status) {
    Page<ApprovalRequest> approvalRequestPage =
        approvalRequestRepository.findByPermissionInAndStatusOrderByCreatedOnDesc(
            loggedInUserPermissions, status, getPageable(page, size));
    List<ApprovalRequest> approvalRequests = approvalRequestPage.getContent();
    if (!nullOrEmpty(approvalRequests)) {
      String loggedInUserRole = getValueFromAccessToken("role");
      String loggedInUsername = getLoggedInUsername();

      List<ApprovalRequest> filteredRequests =
          approvalRequests.stream()
              .filter(
                  approvalRequest ->
                      !approvalRequest.isRequiresWorkFlow()
                          || // Include requests that don’t require workflow
                          (approvalRequest.getApprovalFlows() != null
                              && approvalRequest.getApprovalFlows().stream()
                                  .anyMatch(
                                      approvalFlow ->
                                          (approvalFlow.isRoleBasedApproval()
                                                  && loggedInUserRole.equals(
                                                      approvalFlow
                                                          .getNextApproval()) // User's role matches
                                                  && approvalRequest
                                                      .getApprovalFlows()
                                                      .stream() // Ensure user hasn't approved
                                                      // before
                                                      .noneMatch(
                                                          flow ->
                                                              flow.getApprovalUsername() != null
                                                                  && flow.getApprovalUsername()
                                                                      .equals(loggedInUsername)))
                                              || (!approvalFlow.isRoleBasedApproval()
                                                  && loggedInUsername.equals(
                                                      approvalFlow.getNextApproval())))))
              .toList();
      return getApprovalRequestDTOPaginatedResponseDTO(filteredRequests, approvalRequestPage);
    }
    return getApprovalRequestDTOPaginatedResponseDTO(approvalRequests, approvalRequestPage);
  }

  private static PaginatedResponse<ApprovalRequestDTO> getApprovalRequestDTOPaginatedResponseDTO(
      List<ApprovalRequest> approvalRequests, Page<ApprovalRequest> approvalRequestPage) {
    return PaginatedResponse.<ApprovalRequestDTO>builder()
        .content(
            approvalRequests.stream()
                .map(
                    approvalRequest -> {
                      ApprovalRequestDTO approvalRequestsDTO = new ApprovalRequestDTO();
                      BeanUtilWrapper.copyNonNullProperties(approvalRequest, approvalRequestsDTO);
                      approvalRequestsDTO.setTreatDate(approvalRequest.getApprovedDate());
                      approvalRequestsDTO.setApprovalRequestType(
                          approvalRequestsDTO.getApprovalRequestType());
                      if (approvalRequest.getApprovalUsername() != null)
                        approvalRequestsDTO.setApprovalUsername(
                            approvalRequest.getApprovalUsername());
                      return approvalRequestsDTO;
                    })
                .toList())
        .currentPage(approvalRequestPage.getNumber() + 1)
        .isFirstPage(approvalRequestPage.isFirst())
        .isLastPage(approvalRequestPage.isLast())
        .totalItems(approvalRequestPage.getTotalElements())
        .totalPages(approvalRequestPage.getTotalPages())
        .build();
  }

  public static Pageable getPageable(int page, int size) {
    return PageRequest.of(Math.max(page - 1, 0), size);
  }

  private ApprovalRequestStatus getApprovalStatus(String stringStatus) {
    ApprovalRequestStatus status = null;
    if (stringStatus != null) {
      status =
          switch (stringStatus) {
            case "DECLINED" -> ApprovalRequestStatus.DECLINED;
            case "EXECUTED" -> ApprovalRequestStatus.EXECUTED;
            case "NOT_TREATED" -> ApprovalRequestStatus.NOT_TREATED;
            default ->
                throw new OmnexaException(
                    HttpStatus.BAD_REQUEST,
                    List.of(new ApiError("Search status are: DECLINED, EXECUTED, NOT_TREATED")));
          };
    }
    return status;
  }
}
