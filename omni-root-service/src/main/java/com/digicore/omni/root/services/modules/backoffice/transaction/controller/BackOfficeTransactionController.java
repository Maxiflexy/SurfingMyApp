package com.digicore.omni.root.services.modules.backoffice.transaction.controller;



import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.dtos.SearchRequest;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.TransactionStatus;
import com.digicore.omni.data.lib.modules.merchant.model.Transaction;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantTransactionService;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.services.VirtualAccountService;
import com.digicore.omni.root.lib.modules.common.utils.TransactionPaginatedAndDownloadApiUtil;
import com.digicore.omni.root.services.modules.backoffice.transaction.service.BackOfficeTransactionService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.common.utils.DownloadHeadersUtils;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.socket.WebSocketSession;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService.extractUsername;

@RestController
@RequestMapping("/api/v1/backoffice/merchant-transactions/process/")
@RequiredArgsConstructor
public class BackOfficeTransactionController {

    private final BackOfficeTransactionService backOfficeTransactionService;

    private final TransactionPaginatedAndDownloadApiUtil transactionService;

    @Value("${digicore.file.upload.directory:/digicore}")
    private String fileUploadDirectory;

    @TokenValid
    @GetMapping("view-outlets")
    public ResponseEntity<Object> merchantTransactionOutlets()  {
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.getMerchantOutlets());
    }



    @TokenValid()
    @GetMapping("view-all")
    public ResponseEntity<Object> merchantTransactionHistory(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                                                     @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                                                     @RequestParam(value = "transactionMode") String transactionMode) {
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.getMerchantTransaction(pageNumber,pageSize,transactionMode));
    }

    @TokenValid()
    @GetMapping("view-all-static-account")
    public ResponseEntity<Object> merchantVirtualAccountTransactionHistory(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                             @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.getMerchantVirtualAccountTransaction(pageNumber,pageSize));
    }

//    @TokenValid()
//    @GetMapping("view-merchant-transaction-by-customer-name")
//    public ResponseEntity<Object> merchantTransactionHistoryByCustomerName(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
//                                                                                                                   @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
//                                                                                                                   @RequestParam(value = "customerName",required = false) String customerName,
//                                                                                                                   @RequestParam(value = "transactionMode") String transactionMode){
//        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.getMerchantTransactionCustomerName(pageNumber,pageSize,customerName,transactionMode));
//    }

    @TokenValid()
    @GetMapping("view-merchant-transaction-for-merchant-filter")
    public ResponseEntity<Object> merchantTransactionHistoryForMerchantFilter(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                      @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                      @RequestParam(value = "startDate") String startDate,
                                                                      @RequestParam(value = "endDate") String endDate,
                                                                      @RequestParam(value = "channel", required = false,defaultValue = "null") String channel,
                                                                      @RequestParam(value = "token", required = false,defaultValue = "null") String token,
                                                                      @RequestParam(value = "transactionStatus", required = false,defaultValue = "null") String transactionStatus,
                                                                      @RequestParam(value = "transactionMode", required = false,defaultValue = "null") String transactionMode,
                                                                      @RequestParam(value = "paymentProcessor", required = false,defaultValue = "null") String paymentProcessor,
                                                                      @RequestParam(value = "outletTitle", required = false,defaultValue = "null") String outletTitle,
                                                                      @RequestParam(value = "currency", required = false,defaultValue = "null") String currency){
        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setPage(pageNumber);
        transactionFilterDTO.setSize(pageSize);
        transactionFilterDTO.setMerchantId("null");
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setOutletTitle(outletTitle);
        transactionFilterDTO.setPaymentToken(token);
        transactionFilterDTO.setTransactionMode(transactionMode);
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.fetchTransactionsByFilter(startDate, endDate, transactionFilterDTO));
    }

    @TokenValid()
    @GetMapping("search-transaction-by-table-header")
    public ResponseEntity<Object> searchMerchantTransactionByTableHeader(@RequestParam(value = "searchKey") String searchKey,
                                                                         @RequestParam(value = "transactionMode") String transactionMode,
                                                                         @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.searchMerchantTransactionByTableHeader(searchKey, transactionMode, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("search-static-transaction")
    public ResponseEntity<Object> searchMerchantStaticTransaction(@RequestParam(value = "searchKey") String searchKey,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.searchStaticTransactionByTableHeader(searchKey, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("filter-static-transaction")
    public ResponseEntity<Object> filterMerchantStaticTransaction(@RequestParam(value = "transactionType", required = false) String transactionType,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                  @RequestParam(value = "startDate",required = false) String startDate,
                                                                  @RequestParam(value = "endDate", required = false) String endDate,
                                                                  @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTransactionType(transactionType);
        searchRequest.setTransactionStatus(transactionStatus);
        searchRequest.setStartDate(startDate);
        searchRequest.setEndDate(endDate);
        searchRequest.setPage(pageNumber);
        searchRequest.setSize(pageSize);
        return CommonUtils.buildSuccessResponse(backOfficeTransactionService.filterMerchantTransactionByTableHeader(searchRequest));
    }

    @TokenValid()
    @GetMapping("export-static-transaction-in-csv")
    public void downloadStaticTransactionsInCSV(@RequestParam(value = "transactionType", required = false) String transactionType,
                                                @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                @RequestParam(value = "startDate",required = false) String startDate,
                                                @RequestParam(value = "endDate", required = false) String endDate,
                                                @RequestParam(value = "transactionStatus", required = false) TransactionStatus transactionStatus, HttpServletResponse response) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setTransactionType(transactionType);
        searchRequest.setTransactionStatus(transactionStatus);
        searchRequest.setStartDate(startDate);
        searchRequest.setEndDate(endDate);
        searchRequest.setPage(pageNumber);
        searchRequest.setSize(pageSize);
        backOfficeTransactionService.downloadVirtualAccountTransactionsInCSV(response,searchRequest);
    }

//    @TokenValid()
//    @GetMapping("download")
//    public void downloadInCSV(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
//                              @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
//                              @RequestParam(value = "startDate") String startDate,
//                              @RequestParam(value = "endDate") String endDate,
//                              @RequestParam(value = "channel", required = false,defaultValue = "null") String channel,
//                              @RequestParam(value = "token", required = false,defaultValue = "null") String token,
//                              @RequestParam(value = "transactionStatus", required = false,defaultValue = "null") String transactionStatus,
//                              @RequestParam(value = "transactionMode", required = false,defaultValue = "null") String transactionMode,
//                              @RequestParam(value = "paymentProcessor", required = false,defaultValue = "null") String paymentProcessor,
//                              @RequestParam(value = "currency", required = false,defaultValue = "null") String currency,
//                              HttpServletResponse response) throws IOException {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "merchant-transaction");
//        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
//        transactionFilterDTO.setTransactionStatus(transactionStatus);
//        transactionFilterDTO.setPage(pageNumber);
//        transactionFilterDTO.setSize(pageSize);
//        transactionFilterDTO.setCurrency(currency);
//        transactionFilterDTO.setMerchantId("null");
//        transactionFilterDTO.setPaymentChannel(channel);
//        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
//        transactionFilterDTO.setPaymentToken(token);
//        transactionFilterDTO.setTransactionMode(transactionMode);
//        backOfficeTransactionService.downloadAllMerchantTransactionInCSV(res,startDate,endDate,transactionFilterDTO);
//    }
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public ReportGeneratorService.ReportResponse fuck(WebSocketSession session, @RequestBody ReportGeneratorService.TransactionFilter transactionFilter){
//        Specification<Transaction> transactionSpecification = this.backOfficeMerchantTransactionService.exportAllMerchantTransactionsAsPDFCSV(transactionFilter);
//        return reportGeneratorService.processAndSendData(session, transactionSpecification, "backoffice-transaction", ClientUtil.getLoggedInUsername());
//    }
//
    @TokenValid()
    @GetMapping("download-in-csv")
    public ResponseEntity<Object> downloadTransactions(

            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "channel", required = false,defaultValue = "null") String channel,
            @RequestParam(value = "token", required = false,defaultValue = "null") String token,
            @RequestParam(value = "transactionStatus", required = false,defaultValue = "null") String transactionStatus,
            @RequestParam(value = "transactionMode", required = false,defaultValue = "null") String transactionMode,
            @RequestParam(value = "paymentProcessor", required = false,defaultValue = "null") String paymentProcessor,
            @RequestParam(value = "outletTitle", required = false,defaultValue = "null") String outletTitle,
            @RequestParam(value = "currency", required = false,defaultValue = "null") String currency)throws  ExecutionException, InterruptedException {

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setMerchantId("null");
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setOutletTitle(outletTitle);
        transactionFilterDTO.setPaymentToken(token);
        transactionFilterDTO.setTransactionMode(transactionMode);

        CompletableFuture<ReportGeneratorService.ReportResponse> future =  backOfficeTransactionService.downloadAllMerchantTransactionInCSV(startDate, endDate, transactionFilterDTO);

//        ReportGeneratorService.ReportResponse response = future.get();
//        response.setReportDownloadLink("{BaseUrl}".concat("/api/v1/report/download/").concat(response.getReportDownloadLink()));
       // return ResponseEntity.ok();
        return CommonUtils.buildSuccessResponse(future.get());
    }

//        return ResponseEntity.ok()
//                .headers(headers)
//                //   .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
//                .body(outputStream -> {
//                    List<Transaction> transactions = null;
//                    try {
//                        transactions = future.get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        throw new RuntimeException(e);
//                    }
//                    List<MerchantTransaction> transformedTransactions = transactionService.prepareTransactionsForDownload(transactions);
//
//                    // Use CSV writer to write data to the output stream
//                    ICsvBeanWriter csvWriter = new CsvBeanWriter(new OutputStreamWriter(outputStream), CsvPreference.STANDARD_PREFERENCE);
//                    String[] csvHeader = {"Transaction ID","Payer Name", "Payer Email", "Masked Card Number", "Payment Type", "Channel", "Currency", "Amount", "Transaction Fee", "Total Amount Charged", "Date and Time", "Transaction Status", "Merchant Name", "Merchant Id", "Merchant Account Number", "Retrieval Reference Number", "Payment Processor", "Order Id", "Transaction Reference","Transaction Unique Identifier", "Virtual Account Number", "Transaction Fee Charged By", "STAN", "Transaction Reference from Coral Pay And Afrigo", "Session ID", "OrderSN", "Account Name", "Bank Name", "Bank Code", "Meta Data", "Source"};
//                    String[] nameMapping = {"transactionId", "customerName","customerEmail", "maskedCardNumber", "paymentToken", "paymentChannel","currency", "amountInMinor", "transactionFee", "totalAmountCharged", "createdOn", "transactionStatus", "merchantName", "merchantId", "settlementAccountNumber", "retrievalReferenceNumber", "paymentProcessor", "orderId", "transactionReference", "transactionUniqueId", "virtualAccountNumber", "transactionFeeChargedBy", "stan", "gatewayTransactionId", "sessionId", "orderSn", "accountName", "bankName", "bankCode", "metadata", "transactionSource"};
//                    csvWriter.writeHeader(csvHeader);
//                    for (MerchantTransaction transaction : transformedTransactions) {
//                        csvWriter.write(transaction, nameMapping);
//                    }
//                    csvWriter.close();
//                });
}
