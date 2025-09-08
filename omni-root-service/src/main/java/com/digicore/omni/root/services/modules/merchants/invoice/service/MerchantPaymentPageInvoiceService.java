package com.digicore.omni.root.services.modules.merchants.invoice.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentPageDTO;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;
import com.digicore.omni.root.lib.modules.merchant.response.InvoiceResponse;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantPaymentPageService;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import java.security.Principal;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Dec-11(Wed)-2024
 */

@Service
@RequiredArgsConstructor
public class MerchantPaymentPageInvoiceService {

  private final MerchantPaymentPageService merchantPaymentPageService;
  //  private final NotificationDispatcher notificationDispatcher;
  private final AuditLogProcessor auditLogProcessor;
  private final AuthInformationUtils authInformationUtils;
  //  @Value("${omni.root.mail.payment-reminder.subject:Payment Reminder}")
  //  private String paymentReminderSubject;
  //
  //  @Value("${omni.root.mail.payment-required.subject:Payment Required}")
  //  private String paymentRequiredSubject;

  public InvoiceResponse fetchMerchantInvoiceStatistics(
      Currency currency, String startDate, String endDate) {
    return merchantPaymentPageService.getMerchantInvoiceStatistics(currency, startDate, endDate);
  }

  public PaginatedResponseApiModel<PaymentPageDTO> searchMerchantPaymentPage(
      String searchKey, int page, int size) {
    return merchantPaymentPageService.searchMerchantPaymentPage(searchKey, page, size);
  }

  public PaginatedResponseApiModel<PaymentPageDTO> getMerchantInvoiceFilter(
      String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
    return merchantPaymentPageService.fetchByPaymentPageFilter(
        startDate, endDate, transactionFilterDTO);
  }

  public CompletableFuture<ReportGeneratorService.ReportResponse> exportAllMerchantPaymentPageAsCSV(
      String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {

    return merchantPaymentPageService.exportAllMerchantPaymentPageAsCSV(
        startDate, endDate, transactionFilterDTO, ClientUtil.getLoggedInUsername());
  }

  public PaginatedResponseApiModel<MerchantTransaction> searchMerchantPaymentPageTransaction(
      String searchKey, String paymentPageRef, int page, int size) {
    return merchantPaymentPageService.searchMerchantTransactionByGenericFilter(
        searchKey, paymentPageRef, page, size);
  }

  public PaginatedResponseApiModel<MerchantTransaction> getMerchantInvoiceFilterTransaction(
      String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
    return merchantPaymentPageService.fetchByTransactionFilter(
        startDate, endDate, transactionFilterDTO);
  }

  public CompletableFuture<ReportGeneratorService.ReportResponse>
      exportAllMerchantPaymentPageTransactionAsCSV(
          String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {

    return merchantPaymentPageService.exportAllMerchantTransactionsAsCSVForMerchant(
        startDate, endDate, transactionFilterDTO, ClientUtil.getLoggedInUsername());
  }

  public PaymentPageDTO generatePaymentPage(PaymentPageDTO paymentPageDTO, Principal principal) {
    //    MerchantProfile merchantProfile =
    //        merchantPaymentPageService.retrieveMerchantProfile(ClientUtil.getLoggedInUsername());
    PaymentPageDTO paymentPageDTO1 =
        merchantPaymentPageService.generatePaymentPage(paymentPageDTO, principal);
    //    notificationDispatcher.dispatchEmail(
    //        NotificationServiceRequest.builder()
    //            .notificationSubject(paymentRequiredSubject)
    //            .recipients(List.of(paymentPageDTO.getCustomerEmail()))
    //            .currency(paymentPageDTO.getCurrency().toString())
    //            .transactionNotificationRequest(
    //                TransactionNotificationRequest.builder()
    //                    .paymentLink(paymentPageDTO1.getPaymentPageUrl())
    //                    .amountPaid(
    //                        MoneyUtil.formatWithCommas(
    //                            MoneyUtil.getAmountFromAmountInMinor(
    //                                paymentPageDTO1.getAmountInMinor())))
    //                    .description(paymentPageDTO1.getDetails())
    //                    .transactionRef(paymentPageDTO1.getPaymentPageReference())
    //
    // .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
    //                    .merchantSupportEmail(merchantProfile.getEmail())
    //                    .build())
    //            .dateTime(paymentPageDTO1.getCreatedOn())
    //            .notificationRequestType(NotificationRequestType.SEND_PAYMENT_REQUIRED_EMAIL)
    //            .build());

    AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
    auditLogProcessor.saveAuditWithDescription(
        auditLog.getRole(),
        auditLog.getUser(),
        auditLog.getEmail(),
        LogActivityType.CREATE_ACTIVITY,
        "Generate payment Page");

    return paymentPageDTO1;
  }

  public PaymentPageDTO editPaymentPageAmount(String paymentCode, String amountInMinor) {
    return merchantPaymentPageService.editPaymentPageAmount(paymentCode, amountInMinor);
  }

  //  public void sendReminder(String paymentCode) {
  //    MerchantProfile merchantProfile =
  //        merchantPaymentPageService.retrieveMerchantProfile(ClientUtil.getLoggedInUsername());
  //    PaymentPageDTO paymentPageDTO = merchantPaymentPageService.sendReminder(paymentCode);
  //    notificationDispatcher.dispatchEmail(
  //        NotificationServiceRequest.builder()
  //            .notificationSubject(paymentReminderSubject)
  //            .recipients(List.of(paymentPageDTO.getCustomerEmail()))
  //            .currency(paymentPageDTO.getCurrency().toString())
  //            .transactionNotificationRequest(
  //                TransactionNotificationRequest.builder()
  //                    .paymentPage(paymentPageDTO.getPaymentPageUrl())
  //                    .amountPaid(
  //                        MoneyUtil.formatWithCommas(
  //                            MoneyUtil.getAmountFromAmountInMinor(
  //                                paymentPageDTO.getAmountInMinor())))
  //                    .description(paymentPageDTO.getDetails())
  //                    .transactionRef(paymentPageDTO.getPaymentPageReference())
  //
  // .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
  //                    .merchantSupportEmail(merchantProfile.getEmail())
  //                    .build())
  //            .dateTime(paymentPageDTO.getCreatedOn())
  //
  // .notificationRequestType(NotificationRequestType.SEND_INVOICE_PAYMENT_REMINDER_EMAIL)
  //            .build());
  //  }

  public void deletePaymentPage(String paymentCode) {
    merchantPaymentPageService.deletePaymentPage(paymentCode);
  }

  public void deactivatePaymentPage(String paymentCode) {
    merchantPaymentPageService.deactivatePaymentPage(paymentCode);
  }

  public void activatePaymentPage(String paymentCode) {
    merchantPaymentPageService.activatePaymentPage(paymentCode);
  }

  public PaginatedResponseApiModel<PaymentPageDTO> fetchAllPaymentPage(
      int page, int pageSize, Principal principal) {
    return merchantPaymentPageService.fetchAllMerchantPaymentPage(page, pageSize, principal);
  }

  public PaginatedResponseApiModel<MerchantTransaction> fetchAllPaymentPageTransactions(
      String paymentPageRef, int page, int pageSize, Principal principal) {
    return merchantPaymentPageService.fetchAllMerchantPaymentPageTransactions(
        paymentPageRef, page, pageSize, principal);
  }
}
