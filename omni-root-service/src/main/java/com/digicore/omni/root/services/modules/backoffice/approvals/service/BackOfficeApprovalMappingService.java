package com.digicore.omni.root.services.modules.backoffice.approvals.service;

import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.ApprovalDTO;
import com.digicore.omni.data.lib.modules.common.dtos.ApprovalMappingRequest;

import com.digicore.omni.root.lib.modules.common.services.ApprovalMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-18(Fri)-2022
 */

@Service
@RequiredArgsConstructor
public class BackOfficeApprovalMappingService {

    private final ApprovalMappingService mappingService;

    public List<ApprovalDTO> findInitiator(String name){
       return mappingService.findInitiator(name);
    }

    public List<ApprovalDTO> findApproval(String name){
        return mappingService.findApproval(name);
    }

    public void updateApprovalMapping(ApprovalMappingRequest request){
        mappingService.updateMapping(request);
    }

    public PaginatedResponseApiModel<ApprovalDTO> findAllMapping(int page, int size){
       return mappingService.getAllApprovalMapping(page,size);
    }

    public ApprovalDTO findApproval(long id){
        return mappingService.getApprovalMapping(id);
    }
}

