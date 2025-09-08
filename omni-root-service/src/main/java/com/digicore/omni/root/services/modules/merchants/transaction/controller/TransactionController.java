package com.digicore.omni.root.services.modules.merchants.transaction.controller;


import com.digicore.omni.data.lib.modules.common.dtos.SearchRequest;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.TransactionStatus;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.transaction.service.TransactionService;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/merchant/merchant-transactions/process/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @TokenValid
    @GetMapping("view-outlets")
    public ResponseEntity<Object> merchantTransactionOutlets() {
        return CommonUtils.buildSuccessResponse(transactionService.getMerchantOutlets());
    }

    @TokenValid
    @GetMapping("view-all")
    public ResponseEntity<Object> merchantTransactionHistory(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(transactionService.getMerchantTransaction(pageNumber, pageSize));
    }

    @TokenValid
    @GetMapping("view-all-static-account")
    public ResponseEntity<Object> merchantVirtualAccountTransactionHistory(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(transactionService.getMerchantVirtualAccountTransaction(pageNumber, pageSize));
    }


    @TokenValid()
    @GetMapping("search-transaction-by-table-header")
    public ResponseEntity<Object> searchMerchantTransactionByTableHeader(@RequestParam(value = "searchKey") String searchKey,
                                                                         @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(transactionService.searchMerchantTransactionByTableHeader(searchKey, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("search-static-transaction")
    public ResponseEntity<Object> searchMerchantStaticTransaction(@RequestParam(value = "searchKey") String searchKey,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(transactionService.searchStaticTransactionByTableHeader(searchKey, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("filter-static-transaction")
    public ResponseEntity<Object> filterMerchantStaticTransaction(@RequestParam(value = "transactionType", required = false) String transactionType,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                  @RequestParam(value = "startDate", required = false) String startDate,
                                                                  @RequestParam(value = "endDate", required = false) String endDate,
                                                                  @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTransactionType(transactionType);
        searchRequest.setTransactionStatus(transactionStatus);
        searchRequest.setStartDate(startDate);
        searchRequest.setEndDate(endDate);
        searchRequest.setPage(pageNumber);
        searchRequest.setSize(pageSize);
        return CommonUtils.buildSuccessResponse(transactionService.filterMerchantTransactionByTableHeader(searchRequest));
    }

    @TokenValid()
    @GetMapping("export-static-transaction-in-csv")
    public void downloadStaticTransactionsInCSV(@RequestParam(value = "transactionType", required = false) String transactionType,
                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                @RequestParam(value = "startDate", required = false) String startDate,
                                                @RequestParam(value = "endDate", required = false) String endDate,
                                                @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus, HttpServletResponse response) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTransactionType(transactionType);
        searchRequest.setTransactionStatus(transactionStatus);
        searchRequest.setStartDate(startDate);
        searchRequest.setEndDate(endDate);
        searchRequest.setPage(pageNumber);
        searchRequest.setSize(pageSize);
        transactionService.downloadVirtualAccountTransactionsInCSV(response, searchRequest);
    }


    @TokenValid()
    @GetMapping("view-merchant-transaction-filter")
    public ResponseEntity<Object> merchantTransactionHistoryByMerchantFilterFrontOffice(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                                        @RequestParam(value = "startDate") String startDate,
                                                                                        @RequestParam(value = "endDate") String endDate,
                                                                                        @RequestParam(value = "channel", required = false, defaultValue = "null") String channel,
                                                                                        @RequestParam(value = "token", required = false, defaultValue = "null") String token,
                                                                                        @RequestParam(value = "transactionStatus", required = false, defaultValue = "null") String transactionStatus,
                                                                                        @RequestParam(value = "paymentProcessor", required = false, defaultValue = "null") String paymentProcessor,
                                                                                        @RequestParam(value = "outletTitle", required = false, defaultValue = "null") String outletTitle,
                                                                                        @RequestParam(value = "currency", required = false, defaultValue = "null") String currency) {
        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setPage(pageNumber);
        transactionFilterDTO.setSize(pageSize);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setOutletTitle(outletTitle);
        transactionFilterDTO.setPaymentToken(token);
        return CommonUtils.buildSuccessResponse(transactionService.getTransactionByFilter(startDate, endDate, transactionFilterDTO));
    }


    @TokenValid()
    @GetMapping("download-in-csv-for-merchant")
    public ResponseEntity<Object> downloadTransactions(

            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "channel", required = false, defaultValue = "null") String channel,
            @RequestParam(value = "token", required = false, defaultValue = "null") String token,
            @RequestParam(value = "transactionStatus", required = false, defaultValue = "null") String transactionStatus,
            @RequestParam(value = "paymentProcessor", required = false, defaultValue = "null") String paymentProcessor,
            @RequestParam(value = "outletTitle", required = false, defaultValue = "null") String outletTitle,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency) throws ExecutionException, InterruptedException {

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setMerchantId("null");
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setOutletTitle(outletTitle);
        transactionFilterDTO.setPaymentToken(token);


        CompletableFuture<ReportGeneratorService.ReportResponse> future = transactionService.downloadAllMerchantTransactionInCSVForMerchant(startDate, endDate, transactionFilterDTO);

        return CommonUtils.buildSuccessResponse(future.get());
    }

}
