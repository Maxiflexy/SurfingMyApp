package com.digicore.omni.root.services.modules.merchants.settlement.service;


import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.enums.SettlementStatus;
import com.digicore.omni.data.lib.modules.merchant.dto.SettlementDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.SettlementTransactionsDTO;

import com.digicore.omni.root.lib.modules.merchant.service.MerchantSettlementService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Monsuru
 * @since Dec-28(Wed)-2022
 */
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MerchantSettlementService merchantSettlementService;

    public PaginatedResponseApiModel<SettlementDTO> fetchAllBySettlementStatus(SettlementStatus settlementStatus, int page, int pageSize){
        return merchantSettlementService.fetchAllBySettlementStatus(page, pageSize,settlementStatus);
    }

    public SettlementDTO fetchSettlementDetails(String settlementTransactionId){
        return merchantSettlementService.fetchSettlementDetails(settlementTransactionId);
    }

    public PaginatedResponseApiModel<SettlementTransactionsDTO> fetchSettlementTransactions(int page, int pageSize, String settlementTransactionId){
        return merchantSettlementService.fetchSettlementTransactions(page, pageSize, settlementTransactionId);
    }
    public PaginatedResponseApiModel<SettlementDTO> fetchByMerchantSettlementFilter(String settlementTransactionId,
                                                                                    LocalDate startDate,
                                                                                    LocalDate endDate,
                                                                                    String paymentChannel,
                                                                                    String paymentToken,
                                                                                    String settlementStatus,
                                                                                    int page, int pageSize,
                                                                                    Principal principal)  {
        return merchantSettlementService.fetchByMerchantSettlementFilter(settlementTransactionId, startDate, endDate,
                paymentChannel, paymentToken, settlementStatus, page, pageSize, principal);
    }
//
//    public void downloadAllMerchantSettlementInExcelForMerchant(HttpServletResponse res,
//                                                                String paymentChannel,
//                                                                String paymentToken,
//                                                                String settlementStatus,
//                                                                LocalDate startDate,
//                                                                LocalDate endDate,
//                                                                Principal principal)  {
//        merchantSettlementService.exportAllMerchantSettlementsAsExcelFileForMerchant(res, paymentChannel, paymentToken,
//                settlementStatus, startDate, endDate, principal);
//    }
//
//    public void downloadAllMerchantSettlementInPdfForMerchant(HttpServletResponse res,
//                                                              String paymentChannel,
//                                                              String paymentToken,
//                                                              String settlementStatus,
//                                                              LocalDate startDate,
//                                                              LocalDate endDate,
//                                                              Principal principal)  {
//        merchantSettlementService.exportAllMerchantSettlementsAsPdfFileForMerchant(res, paymentChannel, paymentToken,
//                settlementStatus, startDate, endDate, principal);
//    }
//
//    public void downloadAllMerchantSettlementInCsvForMerchant(HttpServletResponse res,
//                                                              String paymentChannel,
//                                                              String paymentToken,
//                                                              String settlementStatus,
//                                                              LocalDate startDate,
//                                                              LocalDate endDate,
//                                                              Principal principal)  {
//        merchantSettlementService.exportAllMerchantSettlementsAsCsvFileForMerchant(res, paymentChannel, paymentToken,
//                settlementStatus, startDate, endDate, principal);
//    }
}
