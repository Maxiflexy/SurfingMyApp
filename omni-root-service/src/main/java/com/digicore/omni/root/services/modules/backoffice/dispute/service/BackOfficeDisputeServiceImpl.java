package com.digicore.omni.root.services.modules.backoffice.dispute.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.data.lib.modules.common.enums.TransactionStatus;
import com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.TransactionDTO;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeDisputeService;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantDisputeService;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.backoffice.dispute.email.BackOfficeDisputeEmailScheduler;
import com.digicore.otp.service.NotificationDispatcher;
import com.digicore.request.processor.annotations.MakerChecker;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

/**
 * @author Monsuru
 * @since Nov-09(Wed)-2022
 */
@Service
@RequiredArgsConstructor
public class BackOfficeDisputeServiceImpl implements BackOfficeDisputeService {

  private final NotificationDispatcher notificationDispatcher;

  private final BackOfficeService backOfficeService;

 // private final BackOfficeMerchantTransactionService transactionService;

  private final BackOfficeMerchantDisputeService backOfficeMerchantDisputeService;

  private final BackOfficeDisputeEmailScheduler backOfficeDisputeEmailScheduler;

  @MakerChecker(
      checkerPermission = "approve-accepted-or-declined-dispute",
      makerPermission = "accept-dispute",
      requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.TransactionDTO")
  public Object logTransactionDispute(Object transaction, Object... args) {
    MerchantTransaction merchantTransaction =
        backOfficeMerchantDisputeService.logTransactionDispute((TransactionDTO) transaction);
    backOfficeDisputeEmailScheduler.scheduleDisputeEmail(merchantTransaction);
    return merchantTransaction;
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllDisputedTransactions(
      int page, int pageSize) {
    return backOfficeMerchantDisputeService.fetchAllDisputedTransactions(page, pageSize);
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllDisputedTransactionsByFilter(
      int page, int size, TransactionStatus status, PaymentToken token, PaymentChannel channel) {
    if (status == null && channel == null && token != null) {
      return backOfficeMerchantDisputeService.fetchAllDisputedTransactionByTransactionType(
          page, size, token);
    } else if (status == null && channel != null && token == null) {
      return backOfficeMerchantDisputeService.fetchAllDisputedTransactionByTransactionChannel(
          page, size, channel);
    } else if (status != null && channel == null && token == null) {
      return backOfficeMerchantDisputeService.fetchAllDisputedTransactionByTransactionStatus(
          page, size, status);
    }
    return null;
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllDisputedTransactionsByPayerName(
      int page, int size, String payerName) {
    return backOfficeMerchantDisputeService.fetchAllDisputedTransactionByPayerName(
        page, size, payerName);
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllAcceptedDisputedTransactions(
      int page, int pageSize) {
    return backOfficeMerchantDisputeService.fetchAllAcceptedDisputedTransactions(page, pageSize);
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllDeclinedDisputedTransactions(
      int page, int pageSize) {
    return backOfficeMerchantDisputeService.fetchAllDeclinedDisputedTransactions(page, pageSize);
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchAllProcessingDisputedTransactions(
      int page, int pageSize) {
    return backOfficeMerchantDisputeService.fetchAllProcessingDisputedTransactions(page, pageSize);
  }

  @MakerChecker(
      checkerPermission = "approve-accepted-or-declined-dispute",
      makerPermission = "accept-dispute",
      requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO")
  public Object approveDisputeRejection(Object dispute, Object... args) {

    return backOfficeMerchantDisputeService.approveDisputeRejection((DisputeDTO) dispute);
  }

  @MakerChecker(
      checkerPermission = "approve-accepted-or-declined-dispute",
      makerPermission = "decline-dispute",
      requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO")
  public Object declineDisputeRejection(Object dispute, Object... args) {

    return backOfficeMerchantDisputeService.declineDisputeRejection((DisputeDTO) dispute);
  }

  @MakerChecker(
      checkerPermission = "approve-accepted-or-declined-dispute",
      makerPermission = "accept-dispute",
      requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO")
  public Object approveDisputeManualHandling(Object dispute, Object... args) {

    return backOfficeMerchantDisputeService.approveDisputeManualHandling((DisputeDTO) dispute);
  }

  @MakerChecker(
      checkerPermission = "approve-accepted-or-declined-dispute",
      makerPermission = "decline-dispute",
      requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO")
  public Object declineDisputeManualHandling(Object dispute, Object... args) {

    return backOfficeMerchantDisputeService.declineDisputeManualHandling((DisputeDTO) dispute);
  }

  public DisputeDTO getDispute(String disputeId) {

    return backOfficeMerchantDisputeService.getDispute(disputeId);
  }

  public String getPathToUploadedFile(String disputeId) {

    return backOfficeMerchantDisputeService.getPathToUploadedFile(disputeId);
  }

  public PaginatedResponseApiModel<DisputeDTO> fetchDisputesByFilter(
      String disputeId,
      LocalDate startDate,
      LocalDate endDate,
      String paymentChannel,
      String paymentToken,
      String disputeStatus,
      String payerName,
      String transactionStatus,
      String merchantBusinessName,
      String merchantId,
      int page,
      int pageSize) {
    return backOfficeMerchantDisputeService.fetchDisputeByFilter(
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
        page,
        pageSize);
  }

  public void downloadDisputeInExcelFormat(
      HttpServletResponse res,
      String paymentChannel,
      String paymentToken,
      String disputeStatus,
      String payerName,
      String transactionStatus,
      LocalDate startDate,
      LocalDate endDate,
      String merchantBusinessName,
      String merchantId) {
    backOfficeMerchantDisputeService.exportDisputeInExcelFormat(
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

  public void downloadDisputeInPdfFormat(
      HttpServletResponse res,
      String paymentChannel,
      String paymentToken,
      String disputeStatus,
      String payerName,
      String transactionStatus,
      LocalDate startDate,
      LocalDate endDate,
      String merchantBusinessName,
      String merchantId) {
    backOfficeMerchantDisputeService.exportDisputeInPdfFormat(
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

  public void downloadDisputeInCsvFormat(
      HttpServletResponse res,
      String paymentChannel,
      String paymentToken,
      String disputeStatus,
      String payerName,
      String transactionStatus,
      LocalDate startDate,
      LocalDate endDate,
      String merchantBusinessName,
      String merchantId) {

    try {
      backOfficeMerchantDisputeService.exportDisputeInCsvFormat(
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
    } catch (Exception e) {
      throw new ZeusRuntimeException(e.getMessage());
    }
  }

  public CompletableFuture<ReportGeneratorService.ReportResponse> downloadDisputeInCsvFormat(
      String paymentChannel,
      String paymentToken,
      String disputeStatus,
      String payerName,
      String transactionStatus,
      String merchantId,
      String transactionMode,
      String startDate,
      String endDate) {

   return   backOfficeMerchantDisputeService.exportAllDisputeAsCSV(
        paymentChannel,
        paymentToken,
        disputeStatus,
        payerName,
        transactionStatus,
        transactionMode,
        merchantId,
        startDate,
        endDate,
        ClientUtil.getLoggedInUsername());
  }

  public PaginatedResponseApiModel<DisputeDTO> searchBackOfficeDispute(String searchKey, int page, int size) {
    return backOfficeMerchantDisputeService.searchDisputeByGenericFilter(searchKey, page, size);
  }
}
