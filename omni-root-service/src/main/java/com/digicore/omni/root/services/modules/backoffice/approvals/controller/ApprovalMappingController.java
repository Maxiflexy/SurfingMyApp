package com.digicore.omni.root.services.modules.backoffice.approvals.controller;

import com.digicore.omni.data.lib.modules.common.dtos.ApprovalMappingRequest;
import com.digicore.omni.root.services.modules.backoffice.approvals.service.BackOfficeApprovalMappingService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-18(Fri)-2022
 */

@RestController
@RequiredArgsConstructor
public class ApprovalMappingController {

    private final BackOfficeApprovalMappingService mappingService;

    @TokenValid()
    @GetMapping("get-initiators")
    public ResponseEntity<Object> getInitiators(@RequestParam(name = "name") String name) {
        return CommonUtils.buildSuccessResponse(mappingService.findInitiator(name), null);

    }

    @TokenValid()
    @GetMapping("get-approval")
    public ResponseEntity<Object> getApproval(@RequestParam(name = "name") String name) {
        return CommonUtils.buildSuccessResponse(mappingService.findApproval(name), null);

    }

    @TokenValid()
    @PostMapping("add-approval-mapping")
    public ResponseEntity<Object> updateApprovalMapping(@RequestBody ApprovalMappingRequest request) {
        mappingService.updateApprovalMapping(request);
        return CommonUtils.buildSuccessResponse(null, null);
    }

    @TokenValid()
    @GetMapping("get-all-approval-mapping")
    public ResponseEntity<Object> getApproval(@RequestParam(name = "page", defaultValue = "0") int pageNumber, @RequestParam(name = "size", defaultValue = "15") int pageSize) {

        return CommonUtils.buildSuccessResponse(mappingService.findAllMapping(pageNumber, pageSize), null);

    }

    @TokenValid()
    @GetMapping("get-{id}-approval-mapping")
    public ResponseEntity<Object> getApproval(@RequestParam(name = "id") long id) {
        return CommonUtils.buildSuccessResponse(mappingService.findApproval(id), null);
    }
}
