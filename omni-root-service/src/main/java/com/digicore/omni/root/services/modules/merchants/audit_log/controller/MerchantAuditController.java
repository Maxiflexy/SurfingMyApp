package com.digicore.omni.root.services.modules.merchants.audit_log.controller;

import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.audit_log.service.MerchantAuditService;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/merchant/audit-log/process/")
public class MerchantAuditController {

    private final MerchantAuditService merchantAuditService;

    public MerchantAuditController(MerchantAuditService merchantAuditService) {
        this.merchantAuditService = merchantAuditService;
    }

    @TokenValid()
    @GetMapping("get-audit-log-detail-merchant/{logId}")
    public ResponseEntity<Object> getAuditLogDetailForMerchant(@PathVariable int logId){
        return CommonUtils.buildSuccessResponse(merchantAuditService.getLogDetailsForMerchant(logId));
    }

    @TokenValid()
    @GetMapping("get-all-audit-logs-for-merchant")
    public ResponseEntity<Object> getAllLogs(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize){
        return CommonUtils.buildSuccessResponse(merchantAuditService.fetchAllAuditLogForAMerchant(pageNumber,pageSize));
    }


    @TokenValid()
    @GetMapping("get-audit-logs-by-merchant-filter")
    public ResponseEntity<Object> getAuditLogByMerchantFilter(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                                                      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                                                      @RequestParam(value = "startDate",required = false) String startDate,
                                                                                      @RequestParam(value = "endDate", required = false) String endDate,
                                                                                      @RequestParam(value = "activity", required = false) LogActivityType activity){
        return CommonUtils.buildSuccessResponse(merchantAuditService.fetchMerchantAuditLogByFilter(activity,pageNumber,pageSize,startDate,endDate));
    }

    @TokenValid()
    @GetMapping("get-all-log-activity-type")
    public ResponseEntity<Object> getAllLogActivity(){
        return CommonUtils.buildSuccessResponse(LogActivityType.getAllLogActivityTypeDTO());
    }

    @TokenValid()
    @GetMapping("/download-in-csv")
    public ResponseEntity<Object> downloadTransactions(@RequestParam(value = "startDate") String startDate,
                                                       @RequestParam(value = "endDate") String endDate,
                                                       @RequestParam(value = "activity") String activity)
            throws ExecutionException, InterruptedException {

        CompletableFuture<ReportGeneratorService.ReportResponse> future = merchantAuditService.downloadAllMerchantAuditLogInCsv(activity, startDate, endDate);

        return CommonUtils.buildSuccessResponse(future.get());
    }
}
