package com.digicore.omni.root.services.modules.merchants.outlets.controller;

import com.digicore.omni.data.lib.modules.merchant.enums.OutletStatus;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.CreateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.UpdateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.service.MerchantOutletService;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@RestController
@RequestMapping("/api/v1/merchant-outlet")
@RequiredArgsConstructor
public class MerchantOutletController {
    private final MerchantOutletService merchantOutletService;

    @PostMapping
    @TokenValid()
    public ResponseEntity<Object> create(@Valid @RequestBody CreateOutletRequest request){
        return CommonUtils.buildSuccessResponse(merchantOutletService.createOutlet(request));
    }

    @PutMapping
    @TokenValid()
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateOutletRequest request){
        return CommonUtils.buildSuccessResponse(merchantOutletService.updateOutlet(request));
    }

    @GetMapping("/{id}")
    @TokenValid()
    public ResponseEntity<Object> getById(@PathVariable Long id){
        return CommonUtils.buildSuccessResponse(merchantOutletService.getMerchantOutletById(id));
    }

    @GetMapping()
    @TokenValid()
    public ResponseEntity<Object> paginated(@RequestParam(required = false, name = "page", defaultValue = "0") int page, @RequestParam(required = false, name = "pageSize", defaultValue = "15") int pageSize, @RequestParam(required = false, name = "status", defaultValue = "ACTIVE") OutletStatus status){
        return CommonUtils.buildSuccessResponse(merchantOutletService.getPaginatedMerchantResponse(status, PageRequest.of(page, pageSize)));
    }
}
