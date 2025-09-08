package com.digicore.omni.root.services.modules.backoffice.dispute.controller;

import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.data.lib.modules.common.enums.TransactionStatus;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.root.lib.modules.backoffice.service.proxy.BackOfficeProxyDisputeService;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.backoffice.dispute.service.BackOfficeDisputeServiceImpl;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.common.utils.DownloadHeadersUtils;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Monsuru
 * @since Nov-09(Wed)-2022
 */
@RestController
@RequestMapping("/api/v1/backoffice/merchant-dispute/process")
@RequiredArgsConstructor
public class BackOfficeDisputeController {

  private final BackOfficeDisputeServiceImpl backOfficeDisputeServiceImpl;

  private final BackOfficeProxyDisputeService backOfficeProxyDisputeService;

  @TokenValid()
  @PatchMapping("/log-{transactionRef}-dispute")
  public ResponseEntity<Object> logDispute(@PathVariable("transactionRef") String transactionRef) {
    return CommonUtils.buildSuccessResponse(
        backOfficeProxyDisputeService.logDispute(transactionRef));
  }

  @TokenValid()
  @GetMapping("/fetch-all-disputed-transactions")
  public ResponseEntity<Object> fetchAllDisputedTransactions(
      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
    return CommonUtils.buildSuccessResponse(
        backOfficeDisputeServiceImpl.fetchAllDisputedTransactions(page, pageSize));
  }

  @TokenValid()
  @GetMapping("/filter-transactions")
  public ResponseEntity<Object> fetchFilter(
      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @RequestParam(value = "status", required = false) TransactionStatus status,
      @RequestParam(value = "channel", required = false) PaymentChannel channel,
      @RequestParam(value = "token", required = false) PaymentToken token) {
    return CommonUtils.buildSuccessResponse(
        backOfficeDisputeServiceImpl.fetchAllDisputedTransactionsByFilter(
            page, pageSize, status, token, channel));
  }

  @TokenValid()
  @GetMapping("/fetch-all-disputed-transactions-by-payer-name/{name}")
  public ResponseEntity<Object> fetchAllDisputedTransactionsByPayerName(
      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
      @PathVariable String name) {
    return CommonUtils.buildSuccessResponse(
        backOfficeDisputeServiceImpl.fetchAllDisputedTransactionsByPayerName(page, pageSize, name));
  }

  @TokenValid()
  @GetMapping("/fetch-disputes-by-filter")
  public ResponseEntity<Object> fetchDisputesByFilter(
      @RequestParam(value = "disputeId", required = false) String disputeId,
      @RequestParam(value = "startDate", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(value = "endDate", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate,
      @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
      @RequestParam(value = "paymentToken", required = false) String paymentToken,
      @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
      @RequestParam(value = "payerName", required = false) String payerName,
      @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
      @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
      @RequestParam(value = "merchantId", required = false) String merchantId,
      @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    return CommonUtils.buildSuccessResponse(
        backOfficeDisputeServiceImpl.fetchDisputesByFilter(
            disputeId,
            startDate,
            endDate,
            paymentChannel,
            paymentToken,
            disputeStatus,
            payerName,
            transactionStatus,
            merchantBusinessName,
            merchantId,
            pageNumber,
            pageSize));
  }

  @TokenValid()
  @PatchMapping("/approve-dispute-{disputeId}-rejection")
  public ResponseEntity<Object> approveDisputeRejection(
      @PathVariable("disputeId") String disputeId) {

    return CommonUtils.buildSuccessResponse(
        backOfficeProxyDisputeService.approveDisputeRejection(disputeId));
  }

  @TokenValid()
  @PatchMapping("/decline-dispute-{disputeId}-rejection")
  public ResponseEntity<Object> declineDisputeRejection(
      @PathVariable("disputeId") String disputeId) {

    return CommonUtils.buildSuccessResponse(
        backOfficeProxyDisputeService.declineDisputeRejection(disputeId));
  }

  @TokenValid()
  @PatchMapping("/approve-dispute-{disputeId}-manual-handling")
  public ResponseEntity<Object> approveDisputeManualHandling(
      @PathVariable("disputeId") String disputeId) {
    return CommonUtils.buildSuccessResponse(
        backOfficeProxyDisputeService.approveDisputeManualHandling(disputeId));
  }

  @TokenValid()
  @PatchMapping("/decline-dispute-{disputeId}-manual-handling")
  public ResponseEntity<Object> declineDisputeManualHandling(
      @PathVariable("disputeId") String disputeId) {
    return CommonUtils.buildSuccessResponse(
        backOfficeProxyDisputeService.declineDisputeManualHandling(disputeId));
  }

  @GetMapping("/download-dispute-{disputeId}-decline-evidence")
  public void downloadDeclineEvidence(
      @PathVariable("disputeId") String disputeId, HttpServletResponse response) {
    String pathToFile = backOfficeDisputeServiceImpl.getPathToUploadedFile(disputeId);
    try (FileInputStream in = new FileInputStream(pathToFile)) {
      String[] fileName = pathToFile.split("\\.");
      response.addHeader(
          "Content-Disposition",
          String.format(
              "attachment; filename=\"%s\"", "dispute_decline_evidence.".concat(fileName[1])));
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      IOUtils.copy(in, response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw CommonExceptionProcessor.genError(e.getMessage());
    }
  }

  @GetMapping("/download-in-excel-for-backoffice")
  public void downloadInExcelForBackoffice(
      @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
      @RequestParam(value = "paymentToken", required = false) String paymentToken,
      @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
      @RequestParam(value = "payerName", required = false) String payerName,
      @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
      @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
      @RequestParam(value = "merchantId", required = false) String merchantId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate,
      HttpServletResponse response) {
    HttpServletResponse res =
        DownloadHeadersUtils.prepareDownload(
            response, "application/octet-stream", "xlsx", "backoffice-dispute");
    {
      backOfficeDisputeServiceImpl.downloadDisputeInExcelFormat(
          res,
          paymentChannel,
          paymentToken,
          disputeStatus,
          payerName,
          transactionStatus,
          startDate,
          endDate,
          merchantBusinessName,
          merchantId);
    }
  }

  @GetMapping("/download-in-pdf-for-backoffice")
  public void downloadInPdfForBackoffice(
      @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
      @RequestParam(value = "paymentToken", required = false) String paymentToken,
      @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
      @RequestParam(value = "payerName", required = false) String payerName,
      @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
      @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
      @RequestParam(value = "merchantId", required = false) String merchantId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate,
      HttpServletResponse response) {
    HttpServletResponse res =
        DownloadHeadersUtils.prepareDownload(
            response, "application/pdf", "pdf", "backoffice-dispute");

    backOfficeDisputeServiceImpl.downloadDisputeInPdfFormat(
        res,
        paymentChannel,
        paymentToken,
        disputeStatus,
        payerName,
        transactionStatus,
        startDate,
        endDate,
        merchantBusinessName,
        merchantId);
  }

//  @GetMapping("download-in-csv-for-backoffice")
//  public void downloadInCSVForBackoffice(
//      @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//      @RequestParam(value = "paymentToken", required = false) String paymentToken,
//      @RequestParam(value = "disputeStatus", required = false) String disputeStatus,
//      @RequestParam(value = "payerName", required = false) String payerName,
//      @RequestParam(value = "transactionStatus", required = false) String transactionStatus,
//      @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
//      @RequestParam(value = "merchantId", required = false) String merchantId,
//      @RequestParam(value = "startDate", required = false) LocalDate startDate,
//      @RequestParam(value = "endDate", required = false) LocalDate endDate,
//      HttpServletResponse response) {
//    HttpServletResponse res =
//        DownloadHeadersUtils.prepareDownload(response, "text/csv", "csv", "backoffice-dispute");
//    backOfficeDisputeServiceImpl.downloadDisputeInCsvFormat(
//        res,
//        paymentChannel,
//        paymentToken,
//        disputeStatus,
//        payerName,
//        transactionStatus,
//        startDate,
//        endDate,
//        merchantBusinessName,
//        merchantId);
//  }

  @TokenValid()
  @GetMapping("/download-in-csv-for-backoffice")
  public ResponseEntity<Object> downloadTransactions(
      @RequestParam(value = "startDate") String startDate,
      @RequestParam(value = "endDate") String endDate,
      @RequestParam(value = "paymentChannel", required = false, defaultValue = "null")
          String paymentChannel,
      @RequestParam(value = "paymentToken", required = false, defaultValue = "null")
          String paymentToken,
      @RequestParam(value = "disputeStatus", required = false, defaultValue = "null")
          String disputeStatus,
      @RequestParam(value = "payerName", required = false, defaultValue = "null") String payerName,
      @RequestParam(value = "merchantId", required = false, defaultValue = "null")
          String merchantId,
      @RequestParam(value = "transactionStatus", required = false, defaultValue = "null")
          String transactionStatus,
      @RequestParam(value = "transactionMode", required = false, defaultValue = "null")
      String transactionMode)
      throws ExecutionException, InterruptedException {

    CompletableFuture<ReportGeneratorService.ReportResponse> future =
        backOfficeDisputeServiceImpl.downloadDisputeInCsvFormat(
                paymentChannel, paymentToken,
                disputeStatus, payerName, transactionStatus, transactionMode, merchantId,startDate, endDate);


    return CommonUtils.buildSuccessResponse(future.get());
  }

  @TokenValid()
  @GetMapping("/search-dispute")
  public ResponseEntity<Object> searchMerchantBackOfficeDispute(
          @RequestParam(value = "searchKey") String searchKey,
          @RequestParam(value = "page", defaultValue = "0", required = false) int page,
          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

    return CommonUtils.buildSuccessResponse(
            backOfficeDisputeServiceImpl.searchBackOfficeDispute(searchKey, page, pageSize));
  }

}
