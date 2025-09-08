package com.digicore.omni.root.services.modules.merchants.invoice.controller;

import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.merchant.dto.PaymentPageDTO;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.invoice.service.MerchantPaymentPageInvoiceService;
import com.digicore.request.processor.annotations.TokenValid;
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

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Dec-11(Wed)-2024
 */

@RestController
@RequestMapping("/api/v1/merchant-invoice-page/process/")
@RequiredArgsConstructor
@Tag(
        name = "Merchant-Invoice-Page-Controller",
        description =
                "Under this controller contains all the endpoints used to request recurring payment from customer, view and download invoices")
public class MerchantInvoicePageController {

    private final MerchantPaymentPageInvoiceService invoiceService;

    @TokenValid
    @PostMapping("generate-payment-page")
    public ResponseEntity<Object> generatePaymentPage(
            @Valid @RequestBody PaymentPageDTO paymentPageDTO, Principal principal) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.generatePaymentPage(paymentPageDTO, principal));
    }

    @TokenValid
    @PatchMapping("update-invoice-amount")
    public ResponseEntity<Object> editPaymentPageAmount(
            @RequestParam(name = "paymentCode") String paymentCode,
            @RequestParam(name = "amount") String amountInMinor) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.editPaymentPageAmount(paymentCode, amountInMinor));
    }

    @TokenValid
    @PatchMapping("delete-invoice")
    public ResponseEntity<Object> deletePaymentPage(
            @RequestParam(name = "paymentCode") String paymentCode) {
        invoiceService.deletePaymentPage(paymentCode);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid
    @PatchMapping("deactivate-invoice")
    public ResponseEntity<Object> deactivatePaymentPage(
            @RequestParam(name = "paymentCode") String paymentCode) {
        invoiceService.deactivatePaymentPage(paymentCode);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid
    @PatchMapping("activate-invoice")
    public ResponseEntity<Object> activatePaymentPage(
            @RequestParam(name = "paymentCode") String paymentCode) {
        invoiceService.activatePaymentPage(paymentCode);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @GetMapping("fetch-all-payment-page")
    public ResponseEntity<Object> fetchAllPaymentPage(
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Principal principal) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.fetchAllPaymentPage(page, pageSize, principal));
    }

    @TokenValid
    @GetMapping("fetch-merchant-invoice")
    public ResponseEntity<Object> fetchMerchantPaymentPageInvoiceStatistics(
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
            @RequestParam(value = "paymentPageMode", required = false, defaultValue = "null")
            String paymentPageMode,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency) {

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(status);
        transactionFilterDTO.setPage(pageNumber);
        transactionFilterDTO.setSize(pageSize);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(paymentPageMode);
        return CommonUtils.buildSuccessResponse(
                invoiceService.getMerchantInvoiceFilter(startDate, endDate, transactionFilterDTO));
    }

    @TokenValid()
    @GetMapping("search")
    public ResponseEntity<Object> searchMerchantPaymentPage(
            @RequestParam(value = "searchKey") String searchKey,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.searchMerchantPaymentPage(searchKey, pageNumber, pageSize));
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
            @RequestParam(value = "paymentPageMode", required = false, defaultValue = "null")
            String paymentPageMode)
            throws ExecutionException, InterruptedException {
        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(status);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(paymentPageMode);

        CompletableFuture<ReportGeneratorService.ReportResponse> future =
                invoiceService.exportAllMerchantPaymentPageAsCSV(startDate, endDate, transactionFilterDTO);

        return CommonUtils.buildSuccessResponse(future.get());
    }

    @TokenValid()
    @GetMapping("fetch-all-payment-page-transactions")
    public ResponseEntity<Object> fetchAllPaymentPageTransactions(
            @RequestParam(value = "paymentPageReference") String paymentPageReference,
            @RequestParam(name = "pageNumber", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Principal principal) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.fetchAllPaymentPageTransactions(
                        paymentPageReference, page, pageSize, principal));
    }

    @TokenValid()
    @GetMapping("search-transaction")
    public ResponseEntity<Object> searchMerchantTransaction(
            @RequestParam(value = "searchKey") String searchKey,
            @RequestParam(value = "paymentPageReference") String paymentPageReference,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(
                invoiceService.searchMerchantPaymentPageTransaction(
                        searchKey, paymentPageReference, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("view-merchant-transaction-filter")
    public ResponseEntity<Object> merchantTransactionHistoryByMerchantFilterFrontOffice(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "channel", required = false, defaultValue = "null") String channel,
            @RequestParam(value = "token", required = false, defaultValue = "null") String token,
            @RequestParam(value = "transactionStatus", required = false, defaultValue = "null")
            String transactionStatus,
            @RequestParam(value = "paymentProcessor", required = false, defaultValue = "null")
            String paymentProcessor,
            @RequestParam(value = "paymentPageReference") String paymentPageReference,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency) {
        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setPage(pageNumber);
        transactionFilterDTO.setSize(pageSize);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setPaymentPageReference(paymentPageReference);
        transactionFilterDTO.setOutletTitle("null");
        transactionFilterDTO.setPaymentToken(token);
        return CommonUtils.buildSuccessResponse(
                invoiceService.getMerchantInvoiceFilterTransaction(
                        startDate, endDate, transactionFilterDTO));
    }

    @TokenValid()
    @GetMapping("download-transactions")
    public ResponseEntity<Object> downloadTransactions(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "channel", required = false, defaultValue = "null") String channel,
            @RequestParam(value = "token", required = false, defaultValue = "null") String token,
            @RequestParam(value = "transactionStatus", required = false, defaultValue = "null")
            String transactionStatus,
            @RequestParam(value = "paymentProcessor", required = false, defaultValue = "null")
            String paymentProcessor,
            @RequestParam(value = "paymentPageReference") String paymentPageReference,
            @RequestParam(value = "currency", required = false, defaultValue = "null") String currency)
            throws ExecutionException, InterruptedException {

        TransactionFilterDTO transactionFilterDTO = new TransactionFilterDTO();
        transactionFilterDTO.setTransactionStatus(transactionStatus);
        transactionFilterDTO.setCurrency(currency);
        transactionFilterDTO.setMerchantId("null");
        transactionFilterDTO.setPaymentChannel(channel);
        transactionFilterDTO.setPaymentProcessor(paymentProcessor);
        transactionFilterDTO.setPaymentPageReference(paymentPageReference);
        transactionFilterDTO.setOutletTitle("null");
        transactionFilterDTO.setPaymentToken(token);

        CompletableFuture<ReportGeneratorService.ReportResponse> future =
                invoiceService.exportAllMerchantPaymentPageTransactionAsCSV(
                        startDate, endDate, transactionFilterDTO);

        return CommonUtils.buildSuccessResponse(future.get());
    }
}
