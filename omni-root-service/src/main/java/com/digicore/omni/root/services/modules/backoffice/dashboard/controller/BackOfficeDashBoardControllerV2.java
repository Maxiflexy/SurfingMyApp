package com.digicore.omni.root.services.modules.backoffice.dashboard.controller;

import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.root.services.modules.backoffice.dashboard.service.BackOfficeDashBoardService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*
 * @author Ikechi Ucheagwu
 * @createdOn Dec-08(Fri)-2023
 */

@RestController
@RequestMapping("/api/v2/backoffice-dashboard/process/")
@RequiredArgsConstructor
@Tag(name = "BackOffice-Dashboard-Controller-V2",
        description = "Under this controller contains all the endpoints used to retrieve dashboard data")
public class BackOfficeDashBoardControllerV2 {

    private final BackOfficeDashBoardService backOfficeDashBoardService;

    @TokenValid()
    @GetMapping("get-transaction-summary")
    @Operation(
            summary = "Get Total Collections",
            description = "This returns the total collections value and count")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "currency", defaultValue = "NGN") Currency currency,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate,
                                                                  @RequestParam(value = "apiKeyMode", defaultValue = "LIVE") String apiKeyMode) {
        return CommonUtils.buildSuccessResponse(
            backOfficeDashBoardService.getSuccessfulTransactionSummary(currency, channel, startDate, endDate, apiKeyMode));

    }

    @TokenValid()
    @GetMapping("get-transaction-summary-by-payment-token")
    @Operation(
            summary = "Get Total Collections Per Payment Token",
            description = "This returns the total collections per token supplied value and count, TRANSFER payment token has it's own endpoint and doesn't make use of this endpoint. The remaining payment tokens currency is NGN except card which could be NGN or USD.")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "paymentToken", defaultValue = "CARD") PaymentToken paymentToken,
                                                                  @RequestParam(value = "currency", defaultValue = "NGN") Currency currency,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate,
                                                                  @RequestParam(value = "apiKeyMode", defaultValue = "LIVE") String apiKeyMode) {
        return CommonUtils.buildSuccessResponse(
            backOfficeDashBoardService.getSuccessfulTransactionSummary(paymentToken, currency, channel, startDate, endDate, apiKeyMode));

    }

    @TokenValid()
    @GetMapping("get-virtual-transaction-summary")
    @Operation(
            summary = "Get Total Collections For Transfer",
            description = "This returns the total collections value and count, where transaction type could be STATIC or DYNAMIC")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "transactionType", defaultValue = "STATIC") String transactionType,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate,
                                                                  @RequestParam(value = "apiKeyMode", defaultValue = "LIVE") String apiKeyMode) {
        return CommonUtils.buildSuccessResponse(
            backOfficeDashBoardService.getSuccessfulTransactionSummary(transactionType, channel, startDate, endDate, apiKeyMode));

    }

    @TokenValid()
    @GetMapping("get-dispute-summary")
    @Operation(
            summary = "Get Total Collections For Dispute",
            description = "This returns the total collections value and count")
    public ResponseEntity<Object> getSuccessfulDisputeSummary(@RequestParam(value = "startDate") String startDate,
                                                              @RequestParam(value = "endDate") String endDate,
                                                              @RequestParam(value = "apiKeyMode", defaultValue = "LIVE") String apiKeyMode) {
        return CommonUtils.buildSuccessResponse(
            backOfficeDashBoardService.getSuccessfulDisputeSummary(startDate, endDate, apiKeyMode));

    }


}
