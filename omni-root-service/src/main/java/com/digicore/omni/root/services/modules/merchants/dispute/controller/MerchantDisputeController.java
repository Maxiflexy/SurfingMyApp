package com.digicore.omni.root.services.modules.merchants.dispute.controller;


import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.dispute.DisputeDeclineDTO;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.common.utils.DownloadHeadersUtils;
import com.digicore.omni.root.services.modules.merchants.dispute.service.DisputeService;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Monsuru
 * @since Nov-12(Sat)-2022
 */
@RestController
@RequestMapping("/api/v1/merchant/dispute/process")
@RequiredArgsConstructor
public class MerchantDisputeController {

    private final DisputeService disputeService;

    @TokenValid
    @GetMapping("/fetch-disputes-by-filter")
    public ResponseEntity<Object> fetchDisputesByFilter(@RequestParam(value = "disputeId", required = false) String disputeId,
                                                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                        @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
                                                        @RequestParam(value = "paymentToken", required = false) String paymentToken,
                                                        @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
                                                        @RequestParam(value = "payerName", required = false) String payerName,
                                                        @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
                                                        @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        Principal principal){
        return CommonUtils.buildSuccessResponse(disputeService.fetchDisputesByFilter(disputeId, startDate, endDate, paymentChannel, paymentToken,
                disputeStatus, payerName, transactionStatus,pageNumber, pageSize, principal));
    }

    @TokenValid()
    @GetMapping("/get-{disputeId}-dispute")
    public ResponseEntity<Object> fetchDispute(@PathVariable("disputeId") String disputeId)  {
            return CommonUtils.buildSuccessResponse(disputeService.fetchDispute(disputeId));

    }

    @TokenValid()
    @GetMapping("/fetch-all-dispute")
    public ResponseEntity<Object> fetchDispute(Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return CommonUtils.buildSuccessResponse(disputeService.fetchAllMerchantDispute(principal, page, pageSize));
    }

    @TokenValid()
    @GetMapping("/fetch-all-pending-dispute")
    public ResponseEntity<Object> fetchAllPendingDispute(Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return CommonUtils.buildSuccessResponse(disputeService.fetchAllPendingMerchantDispute(principal, page, pageSize));
    }

    @TokenValid()
    @GetMapping("/fetch-all-past-dispute")
    public ResponseEntity<Object> fetchAllPastDispute(Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return CommonUtils.buildSuccessResponse(disputeService.fetchAllPastMerchantDispute(principal, page, pageSize));
    }

    @TokenValid()
    @GetMapping("/fetch-all-merchants-accepted-dispute")
    public ResponseEntity<Object> fetchAllMerchantAcceptedDispute(@RequestParam(value = "merchantId") String merchantId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return CommonUtils.buildSuccessResponse(disputeService.fetchAllAcceptedMerchantDispute(merchantId, page, pageSize));
    }

    @TokenValid()
    @GetMapping("/fetch-all-merchants-declined-dispute")
    public ResponseEntity<Object> fetchAllMerchantDeclinedDispute(@RequestParam(value = "merchantId") String merchantId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return CommonUtils.buildSuccessResponse(disputeService.fetchAllDeclinedMerchantDispute(merchantId, page, pageSize));
    }

    @TokenValid()
    @PatchMapping("/accept-{disputeId}-dispute")
    public ResponseEntity<Object> acceptDispute(@PathVariable("disputeId") String disputeId,
                                    @RequestParam(value = "isManualHandling", defaultValue = "false") boolean isManualHandling)
            {
            return CommonUtils.buildSuccessResponse(disputeService.acceptDispute(disputeId, isManualHandling));

    }

    @TokenValid()
    @PatchMapping("/decline-{disputeId}-dispute")
    public ResponseEntity<Object> declineDispute(@PathVariable("disputeId") String disputeId,
                                     @Valid @ModelAttribute DisputeDeclineDTO disputeDeclineDTO) {
            return CommonUtils.buildSuccessResponse(disputeService.declineDispute(disputeId, disputeDeclineDTO));

    }

    @GetMapping("/download-in-excel-for-merchant")
    public void downloadInExcelForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
                                           @RequestParam(value = "paymentToken", required = false) String paymentToken,
                                           @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
                                           @RequestParam(value = "payerName", required = false) String payerName,
                                           @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
                                           @RequestParam(value = "startDate",required = false) LocalDate startDate,
                                           @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                           HttpServletResponse response, Principal principal)  {
        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/octet-stream","xlsx", "merchant-dispute");
            disputeService.downloadAllMerchantDisputeInExcelForMerchant(res, paymentChannel, paymentToken, disputeStatus, payerName, transactionStatus, startDate, endDate, principal);

    }

    @GetMapping("/download-in-pdf-for-merchant")
    public void downloadInPdfForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
                                         @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
                                         @RequestParam(value = "payerName", required = false) String payerName,
                                         @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                         HttpServletResponse response, Principal principal)  {
        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/pdf","pdf", "merchant-dispute");

            disputeService.downloadAllMerchantDisputeInPdfForMerchant(res, paymentChannel, paymentToken, disputeStatus, payerName, transactionStatus, startDate, endDate, principal);

    }

//    @GetMapping("download-in-csv-for-merchant")
//    public void downloadInCSVForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                         @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
//                                         @RequestParam(value = "payerName", required = false) String payerName,
//                                         @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
//                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                         HttpServletResponse response, Principal principal)  {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "merchant-dispute");
//
//            disputeService.downloadAllMerchantDisputeInCsvForMerchant(res, paymentChannel, paymentToken, disputeStatus, payerName, transactionStatus, startDate, endDate, principal);
//
//    }

    @TokenValid()
    @GetMapping("/download-in-csv-for-merchant")
    public ResponseEntity<Object> downloadTransactions(

            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "paymentChannel", required = false,defaultValue = "null") String paymentChannel,
            @RequestParam(value = "paymentToken", required = false,defaultValue = "null") String paymentToken,
            @RequestParam(value = "disputeStatus", required = false,defaultValue = "null") String disputeStatus,
            @RequestParam(value = "payerName", required = false,defaultValue = "null") String payerName,
            @RequestParam(value = "transactionStatus", required = false,defaultValue = "null") String transactionStatus)throws ExecutionException, InterruptedException {



        CompletableFuture<ReportGeneratorService.ReportResponse> future = disputeService.downloadAllMerchantDisputeInCsvForMerchant(paymentChannel, paymentToken, disputeStatus, payerName, transactionStatus, startDate, endDate);

        return CommonUtils.buildSuccessResponse(future.get());
    }

    @TokenValid()
    @GetMapping("/search-merchant-dispute")
    public ResponseEntity<Object> searchMerchantBackOfficeDispute(
            @RequestParam(value = "searchKey") String searchKey,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        return CommonUtils.buildSuccessResponse(
                disputeService.searchMerchantDispute(searchKey, page, pageSize));
    }
}
