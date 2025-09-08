package com.digicore.omni.root.services.modules.backoffice.approvals.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.ApprovalDTO;

import com.digicore.omni.root.lib.modules.common.services.ApprovalMappingService;
import com.digicore.omni.root.lib.modules.common.services.UserAccountService;
import com.digicore.registhentication.common.dto.response.PaginatedResponseDTO;
import com.digicore.request.processor.dto.ApprovalRequestsDTO;
import com.digicore.request.processor.processors.ApprovalRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.digicore.request.processor.enums.ApprovalRequestStatus.DECLINED;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-21(Mon)-2022
 */

@Service
@RequiredArgsConstructor
public class BackOfficeApprovalService {


    public static final String NO_MAKER_CHECKER="no maker checker available";
    @Autowired(required = false)
    private  ApprovalRequestService approvalRequestService;

    private final UserAccountService userAccountService;

    private final ApprovalMappingService mappingService;


    public Object getRequest(@PathVariable Long requestId) throws ZeusRuntimeException{
        return approvalRequestService.getRequest(requestId);

    }

    public PaginatedResponseApiModel<ApprovalRequestsDTO> getAllApprovalRequestsDTOS(int page, int size)  {
        if (approvalRequestService != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Map<String,Object> approvalRequests = approvalRequestService.getApprovalRequests(auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),page,size);

            List<ApprovalRequestsDTO> approvalRequestsDTOS = (List<ApprovalRequestsDTO>) approvalRequests.get("content");
            PaginatedResponseApiModel<ApprovalRequestsDTO> approvalRequests1 = getRequestsDTOPaginatedUserApiModel(approvalRequests, approvalRequestsDTOS);
             return approvalRequests1 !=null ? approvalRequests1 : new PaginatedResponseApiModel<>();
        }
        else
            throw new ZeusRuntimeException(NO_MAKER_CHECKER);

    }

    private PaginatedResponseApiModel<ApprovalRequestsDTO> getRequestsDTOPaginatedUserApiModel(Map<String, Object> approvalRequests, List<ApprovalRequestsDTO> approvalRequestsDTOS) {
        if (approvalRequestsDTOS !=null && !approvalRequestsDTOS.isEmpty()) {
            String requesterUserName = approvalRequestsDTOS.get(0).getRequesterUsername();
           Optional<ApprovalDTO> loggedInUserApprovalMapping = mappingService.getLoggedInUserApprovalMapping(ClientUtil.getLoggedInUsername());
           if (loggedInUserApprovalMapping.isPresent()){
               loggedInUserApprovalMapping.get().getInitiators().forEach(initiator->{

                       if (initiator.equalsIgnoreCase(requesterUserName)){
                           UserAccountApiModel accountApiModel = userAccountService.fetchUserByUsername(initiator);
                           String name = accountApiModel.getFirstName().concat(" ").concat(accountApiModel.getLastName());
                           approvalRequestsDTOS.forEach(approvalRequestsDTO -> approvalRequestsDTO.setRequesterName(name));
                           approvalRequests.put("content", approvalRequestsDTOS);
                       }
               });
               return getApprovalRequestsDTOPaginatedUserApiModel(approvalRequests, approvalRequestsDTOS);
           }

        }
        return null;
    }


    public PaginatedResponseApiModel<ApprovalRequestsDTO> getPendingApprovalRequestsDTOS(int page,int size)  {
        if (approvalRequestService != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Map<String,Object> approvalRequests = approvalRequestService.getPendingApprovalRequests(
                    auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList()
                    ,page,size
            );

            List<ApprovalRequestsDTO> approvalRequestsDTOS = (List<ApprovalRequestsDTO>) approvalRequests.get("content");
            PaginatedResponseApiModel<ApprovalRequestsDTO> approvalRequests1 = getRequestsDTOPaginatedUserApiModel(approvalRequests, approvalRequestsDTOS);
            return approvalRequests1 !=null ? approvalRequests1 : new PaginatedResponseApiModel<>();
        }
        else
            throw new ZeusRuntimeException(NO_MAKER_CHECKER);

    }

    public PaginatedResponseApiModel<ApprovalRequestsDTO> getDeclinedApprovalRequestsDTOS(int page, int size)  {
        if (approvalRequestService != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Map<String,Object> approvalRequests = approvalRequestService.getRequests(
                    auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()
                    ,page, size
                    , DECLINED
            );

            List<ApprovalRequestsDTO> approvalRequestsDTOS = (List<ApprovalRequestsDTO>) approvalRequests.get("content");
            PaginatedResponseApiModel<ApprovalRequestsDTO> approvalRequests1 = getRequestsDTOPaginatedUserApiModel(approvalRequests, approvalRequestsDTOS);
            return approvalRequests1 !=null ? approvalRequests1 : new PaginatedResponseApiModel<>();
        }
        else
            throw new ZeusRuntimeException(NO_MAKER_CHECKER);

    }


    private static PaginatedResponseApiModel<ApprovalRequestsDTO> getApprovalRequestsDTOPaginatedUserApiModel(Map<String, Object> approvalRequests, List<ApprovalRequestsDTO> approvalRequestsDTOS) {
        PaginatedResponseApiModel<ApprovalRequestsDTO> approvalRequestsDTOPaginatedUserApiModel = new PaginatedResponseApiModel<>();
        approvalRequestsDTOPaginatedUserApiModel.setContent(approvalRequestsDTOS);
        approvalRequestsDTOPaginatedUserApiModel.setLastPage((Boolean) approvalRequests.get("isLastPage"));
        approvalRequestsDTOPaginatedUserApiModel.setFirstPage((Boolean) approvalRequests.get("isFirstPage"));
        approvalRequestsDTOPaginatedUserApiModel.setTotalItems((Long) approvalRequests.get("totalItems"));
        approvalRequestsDTOPaginatedUserApiModel.setCurrentPage((Integer) approvalRequests.get("currentPage"));
        approvalRequestsDTOPaginatedUserApiModel.setTotalPages((Integer) approvalRequests.get("totalPages"));
        return approvalRequestsDTOPaginatedUserApiModel;
    }

    public PaginatedResponseDTO<ApprovalRequestsDTO> searchApprovalRequestByTableHeader(String searchKey, int page, int size) {
        return approvalRequestService.searchApprovalRequestByGenericFilter(searchKey, page, size);
    }
}
