package com.digicore.omni.root.services.modules.merchants.invoice.controller;

import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentLinkDTO;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.invoice.service.MerchantPaymentLinkInvoiceService;
import com.digicore.request.processor.annotations.TokenValid;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/merchant-invoice/process/")
@RequiredArgsConstructor
@Tag(
        name = "Merchant-Invoice-Controller",
        description =
                "Under this controller contains all the endpoints used to request payment from customer, view and download invoices",
        externalDocs =
        @ExternalDocumentation(
                description = "New Update !!!",
                url =
                        "https://docs.google.com/document/d/143Pe1kOt-YsEFAZP6z8OIf0p46nONQ_fao2ixnK5mU0"))
public class MerchantInvoiceController {

    private final MerchantPaymentLinkInvoiceService invoiceService;

    @TokenValid
    @PostMapping("generate-payment-link")
    public ResponseEntity<Object> generatePaymentLink(
            @Valid @org.springframework.web.bind.annotation.RequestBody PaymentLinkDTO paymentLinkDTO,
            Principal principal) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.generatePaymentLink(paymentLinkDTO, principal));
    }

    @TokenValid()
    @PostMapping("send-invoice-reminder")
    public ResponseEntity<Object> sendPaymentLinkReminder(
            @RequestParam(name = "paymentCode") String paymentCode) {
        invoiceService.sendReminder(paymentCode);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid
    @PatchMapping("update-invoice-amount")
    public ResponseEntity<Object> editPaymentLinkAmount(
            @RequestParam(name = "paymentCode") String paymentCode,
            @RequestParam(name = "amount") String amountInMinor) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.editPaymentLinkAmount(paymentCode, amountInMinor));
    }

    @TokenValid
    @PatchMapping("update-invoice-status")
    public ResponseEntity<Object> editPaymentLinkStatus(
            @RequestParam(name = "paymentCode") String paymentCode) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.changePaymentLinkStatusToPaid(paymentCode));
    }

    @TokenValid
    @PatchMapping("delete-invoice")
    public ResponseEntity<Object> deletePaymentLink(
            @RequestParam(name = "paymentCode") String paymentCode) {
        invoiceService.deletePaymentLink(paymentCode);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @GetMapping("fetch-all-payment-link")
    public ResponseEntity<Object> fetchAllPaymentLink(
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Principal principal) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.fetchAllPaymentLink(page, pageSize, principal));
    }

    @TokenValid
    @GetMapping("fetch-merchant-invoice")
    public ResponseEntity<Object> fetchMerchantPaymentLinkInvoiceStatistics(
            @RequestParam(name = "currency", required = false, defaultValue = "NGN") Currency currency,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.fetchMerchantInvoiceStatistics(currency, startDate, endDate));
    }

    @TokenValid
    @GetMapping("view-merchant-invoice-filter")
    public ResponseEntity<Object> merchantInvoiceByMerchantFilter(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "status", required = false, defaultValue = "null") String status,
            @RequestParam(value = "startDate", required = false, defaultValue = "null") String startDate,
            @RequestParam(value = "endDate", required = false, defaultValue = "null") String endDate,
            @RequestParam(value = "paymentLinkMode", required = false, defaultValue = "null") String paymentLinkMode,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency) {

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(status);
        transactionFilterDTO.setPage(pageNumber);
        transactionFilterDTO.setSize(pageSize);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(paymentLinkMode);
        return CommonUtils.buildSuccessResponse(
                invoiceService.getMerchantInvoiceFilter(startDate, endDate, transactionFilterDTO));
    }

    @TokenValid()
    @GetMapping("search")
    public ResponseEntity<Object> searchMerchantPaymentLink(@RequestParam(value = "searchKey") String searchKey,
                                                            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(invoiceService.searchMerchantPaymentLink(searchKey, pageNumber, pageSize));
    }


    @TokenValid()
    @GetMapping("download-invoice")
    @Operation(
            summary = "This endpoint is new, check it out",
            description =
                    "This endpoints is used to export invoice transactions. it only supports just one export type which is CSV.")
    @ApiResponse(
            responseCode = "200",
            description = "This means your request was successfully treated",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseJson.class))
            })
    public ResponseEntity<Object> downloadTransactions(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "status", required = false, defaultValue = "null") String status,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency,
            @RequestParam(value = "paymentLinkMode", required = false, defaultValue = "null") String paymentLinkMode)
            throws ExecutionException, InterruptedException {
        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(status);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(paymentLinkMode);

        CompletableFuture<ReportGeneratorService.ReportResponse> future = invoiceService.exportAllMerchantPaymentLinkAsCSV(startDate, endDate, transactionFilterDTO);

        return CommonUtils.buildSuccessResponse(future.get());
    }
}
