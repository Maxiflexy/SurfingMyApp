package com.digicore.omni.root.services.modules.merchants.audit_log.service;

import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.utils.DateUtils;
import com.digicore.omni.root.lib.modules.merchant.response.AuditResponse;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantAudit;
import com.digicore.request.processor.dto.AuditLogDTO;
import com.digicore.request.processor.enums.LogActivityType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@Service
public class MerchantAuditService {

    private final MerchantAudit merchantAudit;

    public MerchantAuditService(MerchantAudit merchantAudit) {
        this.merchantAudit = merchantAudit;
    }

   public AuditResponse getLogDetailsForMerchant(long logId){
        return merchantAudit.getALogForUser(logId);
    }



    public PaginatedResponseApiModel<AuditLogDTO> fetchAllAuditLogForAMerchant(int pageNumber, int pageSize){
       return merchantAudit.getAllAuditLogForMerchant(pageNumber,pageSize);


    }



    public PaginatedResponseApiModel<AuditLogDTO> fetchMerchantAuditLogByFilter(LogActivityType activity , int pageNumber, int pageSize, String startDate, String endDate){
        if(activity != null && startDate !=null && endDate != null){
            LocalDateTime start = DateUtils.toStartOfDay(startDate);
            LocalDateTime end = DateUtils.toEndOfDay(endDate);
            return merchantAudit.getAuditByDateRangeForMerchant(activity,start,end, pageNumber,pageSize);
        }else if(activity == null && startDate != null && endDate != null){
            LocalDateTime start = DateUtils.toStartOfDay(startDate);
            LocalDateTime end = DateUtils.toEndOfDay(endDate);
            return merchantAudit.getAuditByDateRangeOnlyForMerchant(start,end,pageNumber,pageSize);
        } else if (activity != null && startDate == null && endDate == null) {
            return merchantAudit.getAuditByActivityOnlyForMerchant(activity,pageNumber,pageSize);
        } else if (activity == null && startDate == null && endDate == null) {
            return merchantAudit.getAllAuditLogForMerchant(pageNumber,pageSize);
        }
        return new PaginatedResponseApiModel<>();
    }

    public CompletableFuture<ReportGeneratorService.ReportResponse> downloadAllMerchantAuditLogInCsv(String activity, String startDate, String endDate)  {
        return merchantAudit.exportAllMerchantAuditLogs(activity, startDate, endDate);
    }
}
