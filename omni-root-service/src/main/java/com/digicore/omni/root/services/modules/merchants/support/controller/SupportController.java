package com.digicore.omni.root.services.modules.merchants.support.controller;


import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.support.service.SupportService;
import com.digicore.omni.root.services.util.SupportServiceRequest;
import com.digicore.request.processor.annotations.TokenValid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant-support/process/")
public class SupportController {

    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }


    @TokenValid()
    @GetMapping("fetch-all-corresponding-email")
    public ResponseEntity<Object> fetchAllCorrespondingEmail() {
        return CommonUtils.buildSuccessResponse(supportService.getSupportEmail());
    }

    @TokenValid()
    @PostMapping("send-support-email")
    public ResponseEntity<Object> sendEmailToSupportTeam(@RequestBody SupportServiceRequest request) throws MessagingException {
        supportService.sendSupportMail(request);
        return CommonUtils.buildSuccessResponse();
    }
}

