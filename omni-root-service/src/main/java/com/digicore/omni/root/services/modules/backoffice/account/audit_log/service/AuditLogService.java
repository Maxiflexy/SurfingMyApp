package com.digicore.omni.root.services.modules.backoffice.account.audit_log.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeAuditService;

import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.utils.DateUtils;
import com.digicore.omni.root.lib.modules.merchant.response.AuditResponse;
import com.digicore.request.processor.dto.AuditLogDTO;
import com.digicore.request.processor.enums.LogActivityType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class AuditLogService {

    private final BackOfficeAuditService backOfficeAuditService;

    public AuditLogService(BackOfficeAuditService backOfficeAuditService) {
       this.backOfficeAuditService = backOfficeAuditService;
       }

    public AuditResponse getLogDetails(long logId){
        return backOfficeAuditService.getALog(logId);
    }

    public PaginatedResponseApiModel<AuditLogDTO> fetchAllAuditLogByMerchantName(String merchantName, int pageNumber, int pageSize){
        if(merchantName == null){
            return backOfficeAuditService.getAllAuditLog(pageNumber,pageSize);
        }
        return backOfficeAuditService.getAuditsByMerchant(merchantName,pageNumber,pageSize);

    }


    public PaginatedResponseApiModel<AuditLogDTO> fetchAllAuditLog(int pageNumber, int pageSize){
        return backOfficeAuditService.getAllAuditLog(pageNumber,pageSize);

    }


    public PaginatedResponseApiModel<AuditLogDTO> fetchByFilter(LogActivityType activity , int pageNumber, int pageSize, String startDate, String endDate){
        if(activity != null && startDate !=null && endDate != null){
            LocalDateTime start = DateUtils.toStartOfDay(startDate);
            LocalDateTime end = DateUtils.toEndOfDay(endDate);
            return backOfficeAuditService.getAuditByDateRangeBackOffice(activity,start,end,pageNumber,pageSize);
        } else if(activity == null && startDate != null && endDate != null){
            LocalDateTime start = DateUtils.toStartOfDay(startDate);
            LocalDateTime end = DateUtils.toEndOfDay(endDate);
            return backOfficeAuditService.getAuditByDateRangeOnly(start,end,pageNumber,pageSize);
        } else if (activity != null && startDate == null && endDate == null) {
            return backOfficeAuditService.getAuditByActivityBackOffice(activity,pageNumber,pageSize);
        } else if (activity == null && startDate == null && endDate == null) {
            return backOfficeAuditService.getAllAuditLog(pageNumber,pageSize);
        }
        return new PaginatedResponseApiModel<>();
    }

    public CompletableFuture<ReportGeneratorService.ReportResponse> downloadAllBackOfficeAuditLogInCsv(String activity, String startDate, String endDate)  {
        return backOfficeAuditService.exportAllBackOfficeAuditLogs(activity, startDate, endDate);
    }


}


