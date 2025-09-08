package com.digicore.omni.root.services.modules.merchants.agency.vas.controller;

import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.agency.vas.service.AgencyVasTransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-21(Thu)-2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agency/vas/process/")
public class AgencyVasTransactionHistoryController {
    private final AgencyVasTransactionHistoryService agencyVasTransactionHistoryService;

    @GetMapping("transaction-history")
    public ResponseEntity<Object> getTransactionHistory(@RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int pageSize,
                                                        @RequestParam(required = false) String searchTerm) {
        return CommonUtils.buildSuccessResponse(agencyVasTransactionHistoryService.getVasTransactionHistory(searchTerm, pageNumber, pageSize));
    }
}