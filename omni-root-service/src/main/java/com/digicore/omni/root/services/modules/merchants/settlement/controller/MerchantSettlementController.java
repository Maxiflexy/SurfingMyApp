package com.digicore.omni.root.services.modules.merchants.settlement.controller;


import com.digicore.omni.data.lib.modules.common.enums.SettlementStatus;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.settlement.service.SettlementService;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Monsuru
 * @since Dec-28(Wed)-2022
 */
@RestController
@RequestMapping("/api/v1/merchant/settlement/process/")
@RequiredArgsConstructor
public class MerchantSettlementController {

    private final SettlementService settlementService;

    @TokenValid
    @GetMapping("get-all")
    public ResponseEntity<Object> fetchAllSettlements(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "status") SettlementStatus settlementStatus) {
        return CommonUtils.buildSuccessResponse(settlementService.fetchAllBySettlementStatus(settlementStatus, page, pageSize));
    }

    @TokenValid
    @GetMapping("get-details")
    public ResponseEntity<Object> getSettlementDetails(@RequestParam("settlementTransactionId") String settlementTransactionId) {
        return CommonUtils.buildSuccessResponse(settlementService.fetchSettlementDetails(settlementTransactionId));
    }

    @TokenValid
    @GetMapping("get-transactions")
    public ResponseEntity<Object> getSettlementTransactions(@RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam("settlementTransactionId") String settlementTransactionId) {
        return CommonUtils.buildSuccessResponse(settlementService.fetchSettlementTransactions(page, pageSize, settlementTransactionId));
    }

    @TokenValid()
    @GetMapping("fetch-by-merchant-settlement-filter")
    public ResponseEntity<Object> fetchByMerchantSettlementFilter(@RequestParam(value = "settlementTransactionId", required = false) String settlementTransactionId,
                                                                  @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                  @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                                  @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
                                                                  @RequestParam(value = "paymentToken", required = false) String paymentToken,
                                                                  @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                                  Principal principal) {
        return CommonUtils.buildSuccessResponse(settlementService.fetchByMerchantSettlementFilter(settlementTransactionId, startDate, endDate, paymentChannel,
                paymentToken, settlementStatus, pageNumber, pageSize, principal));
    }
//
//    @GetMapping("download-in-excel-for-merchant")
//    public void downloadInExcelForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                            @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                            @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                            @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                            @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                            HttpServletResponse response, Principal principal)  {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/octet-stream","xlsx", "merchant-settlements");
//            settlementService.downloadAllMerchantSettlementInExcelForMerchant(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, principal);
//
//    }
//
//    @GetMapping("download-in-pdf-for-merchant")
//    public void downloadInPdfForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                         @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                         HttpServletResponse response, Principal principal)   {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/pdf","pdf", "merchant-settlements");
//            settlementService.downloadAllMerchantSettlementInPdfForMerchant(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, principal);
//
//    }
//
//    @GetMapping("download-in-csv-for-merchant")
//    public void downloadInCSVForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                         @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                         HttpServletResponse response, Principal principal)  {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "merchant-settlements");
//            settlementService.downloadAllMerchantSettlementInCsvForMerchant(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, principal);
//
//    }
}
