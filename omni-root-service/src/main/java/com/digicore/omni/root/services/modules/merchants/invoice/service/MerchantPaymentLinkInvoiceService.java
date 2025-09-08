package com.digicore.omni.root.services.modules.merchants.invoice.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.common.util.MoneyUtil;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.notification.lib.request.TransactionNotificationRequest;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentLinkDTO;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;
import com.digicore.omni.root.lib.modules.merchant.response.InvoiceResponse;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantPaymentLinkService;
import com.digicore.otp.service.NotificationDispatcher;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantPaymentLinkInvoiceService {

  private final MerchantPaymentLinkService merchantPaymentLinkService;
  private final NotificationDispatcher notificationDispatcher;
  private final AuditLogProcessor auditLogProcessor;
  private final AuthInformationUtils authInformationUtils;

  @Value("${omni.root.mail.payment-reminder.subject:Payment Reminder}")
  private String paymentReminderSubject;

  @Value("${omni.root.mail.payment-required.subject:Payment Required}")
  private String paymentRequiredSubject;

  public InvoiceResponse fetchMerchantInvoiceStatistics(
      Currency currency, String startDate, String endDate) {
    return merchantPaymentLinkService.getMerchantInvoiceStatistics(currency, startDate, endDate);
  }

  public PaginatedResponseApiModel<PaymentLinkDTO> searchMerchantPaymentLink(
      String searchKey, int page, int size) {
    return merchantPaymentLinkService.searchMerchantPaymentLink(searchKey, page, size);
  }

  public PaginatedResponseApiModel<PaymentLinkDTO> getMerchantInvoiceFilter(
      String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
    return merchantPaymentLinkService.fetchByPaymentLinkFilter(
        startDate, endDate, transactionFilterDTO);
  }

  public CompletableFuture<ReportGeneratorService.ReportResponse> exportAllMerchantPaymentLinkAsCSV(
      String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {

    return merchantPaymentLinkService.exportAllMerchantPaymentLinkAsCSV(
        startDate, endDate, transactionFilterDTO, ClientUtil.getLoggedInUsername());
  }

  public PaymentLinkDTO generatePaymentLink(PaymentLinkDTO paymentLinkDTO, Principal principal) {
    MerchantProfile merchantProfile =
        merchantPaymentLinkService.retrieveMerchantProfile(ClientUtil.getLoggedInUsername());
    PaymentLinkDTO paymentLinkDTO1 =
        merchantPaymentLinkService.generatePaymentLink(paymentLinkDTO, principal);
    notificationDispatcher.dispatchEmail(
        NotificationServiceRequest.builder()
            .notificationSubject(paymentRequiredSubject)
            .recipients(List.of(paymentLinkDTO.getCustomerEmail()))
            .currency(paymentLinkDTO.getCurrency().toString())
            .transactionNotificationRequest(
                TransactionNotificationRequest.builder()
                    .paymentLink(paymentLinkDTO1.getPaymentLinkUrl())
                    .amountPaid(
                        MoneyUtil.formatWithCommas(
                            MoneyUtil.getAmountFromAmountInMinor(
                                paymentLinkDTO1.getAmountInMinor())))
                    .description(paymentLinkDTO1.getDetails())
                    .transactionRef(paymentLinkDTO1.getPaymentLinkReference())
                    .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                    .merchantSupportEmail(merchantProfile.getEmail())
                    .build())
            .dateTime(paymentLinkDTO1.getCreatedOn())
            .notificationRequestType(NotificationRequestType.SEND_PAYMENT_REQUIRED_EMAIL)
            .build());

    AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
    auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(),
            LogActivityType.CREATE_ACTIVITY,"Generate payment link");

    return paymentLinkDTO1;
  }

  public PaymentLinkDTO editPaymentLinkAmount(String paymentCode, String amountInMinor) {
    return merchantPaymentLinkService.editPaymentLinkAmount(paymentCode, amountInMinor);
  }

  public void sendReminder(String paymentCode) {
    MerchantProfile merchantProfile =
        merchantPaymentLinkService.retrieveMerchantProfile(ClientUtil.getLoggedInUsername());
    PaymentLinkDTO paymentLinkDTO = merchantPaymentLinkService.sendReminder(paymentCode);
    notificationDispatcher.dispatchEmail(
        NotificationServiceRequest.builder()
            .notificationSubject(paymentReminderSubject)
            .recipients(List.of(paymentLinkDTO.getCustomerEmail()))
            .currency(paymentLinkDTO.getCurrency().toString())
            .transactionNotificationRequest(
                TransactionNotificationRequest.builder()
                    .paymentLink(paymentLinkDTO.getPaymentLinkUrl())
                    .amountPaid(
                        MoneyUtil.formatWithCommas(
                            MoneyUtil.getAmountFromAmountInMinor(
                                paymentLinkDTO.getAmountInMinor())))
                    .description(paymentLinkDTO.getDetails())
                    .transactionRef(paymentLinkDTO.getPaymentLinkReference())
                    .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                    .merchantSupportEmail(merchantProfile.getEmail())
                    .build())
            .dateTime(paymentLinkDTO.getCreatedOn())
            .notificationRequestType(NotificationRequestType.SEND_INVOICE_PAYMENT_REMINDER_EMAIL)
            .build());
  }

  public PaymentLinkDTO changePaymentLinkStatusToPaid(String paymentCode) {
    return merchantPaymentLinkService.changePaymentLinkStatusToPaid(paymentCode);
  }

  public void deletePaymentLink(String paymentCode) {
    merchantPaymentLinkService.deletePaymentLink(paymentCode);
  }

  public PaginatedResponseApiModel<PaymentLinkDTO> fetchAllPaymentLink(
      int page, int pageSize, Principal principal) {
    return merchantPaymentLinkService.fetchAllMerchantPaymentLink(page, pageSize, principal);
  }
}
