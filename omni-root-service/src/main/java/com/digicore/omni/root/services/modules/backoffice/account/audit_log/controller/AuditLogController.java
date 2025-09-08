package com.digicore.omni.root.services.modules.backoffice.account.audit_log.controller;

import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.backoffice.account.audit_log.service.AuditLogService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/api/v1/backoffice/audit-log/process/")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @TokenValid()
    @GetMapping("get-audit-log-details/{logId}")
    public ResponseEntity<Object> getAuditLogDetails(@PathVariable int logId) {
        return CommonUtils.buildSuccessResponse(auditLogService.getLogDetails(logId), null);
    }

    @TokenValid()
    @GetMapping("get-all-audit-logs")
    public ResponseEntity<Object> getAllLogs(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        return CommonUtils.buildSuccessResponse(auditLogService.fetchAllAuditLog(pageNumber, pageSize), null);
    }

    @TokenValid()
    @GetMapping("get-audit-logs-by-name")
    public ResponseEntity<Object> getAuditLogByName(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                    @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                    @RequestParam(value = "merchantName", required = false) String merchantName) {

        return CommonUtils.buildSuccessResponse(auditLogService.fetchAllAuditLogByMerchantName(merchantName, pageNumber, pageSize), null);
    }

    @TokenValid()
    @GetMapping("get-audit-logs-by-filter")
    public ResponseEntity<Object> getAuditLogByFilter(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
                                                      @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                                      @RequestParam(value = "startDate", required = false) String startDate,
                                                      @RequestParam(value = "endDate", required = false) String endDate,
                                                      @RequestParam(value = "activity", required = false) LogActivityType activity) {
        return CommonUtils.buildSuccessResponse(auditLogService.fetchByFilter(activity, pageNumber, pageSize, startDate, endDate), null);
    }

    @TokenValid()
    @GetMapping("get-all-log-activity-type")
    public ResponseEntity<Object> getAllLogActivity() {
        return CommonUtils.buildSuccessResponse(LogActivityType.getAllLogActivityTypeDTO(), null);
    }

    @TokenValid()
    @GetMapping("/download-in-csv")
    public ResponseEntity<Object> downloadTransactions(@RequestParam(value = "startDate") String startDate,
                                                       @RequestParam(value = "endDate") String endDate,
                                                       @RequestParam(value = "activity") String activity)
            throws ExecutionException, InterruptedException {

        CompletableFuture<ReportGeneratorService.ReportResponse> future = auditLogService.downloadAllBackOfficeAuditLogInCsv(activity, startDate, endDate);

        return CommonUtils.buildSuccessResponse(future.get());
    }


}

