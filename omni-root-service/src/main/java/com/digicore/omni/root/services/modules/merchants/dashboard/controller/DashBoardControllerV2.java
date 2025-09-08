package com.digicore.omni.root.services.modules.merchants.dashboard.controller;

import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantDashBoardService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-17(Sun)-2023
 */
@RestController
@RequestMapping("/api/v2/merchant-dashboard/process/")
@RequiredArgsConstructor
@Tag(name = "Merchant-Dashboard-Controller-V2",
        description = "Under this controller contains all the endpoints used to retrieve dashboard data")
public class DashBoardControllerV2 {
    private final MerchantDashBoardService merchantDashBoardService;

    @TokenValid()
    @GetMapping("get-transaction-summary")
    @Operation(
            summary = "Get Total Collections",
            description = "This returns the total collections value and count")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "currency", defaultValue = "NGN") Currency currency,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(merchantDashBoardService.getSuccessfulTransactionSummary(currency, channel, startDate, endDate));

    }

    @TokenValid()
    @GetMapping("get-transaction-summary-by-payment-token")
    @Operation(
            summary = "Get Total Collections Per Payment Token",
            description = "This returns the total collections per token supplied value and count, TRANSFER payment token has it's own endpoint and doesn't make use of this endpoint. The remaining payment tokens currency is NGN except card which could be NGN or USD.")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "paymentToken") PaymentToken paymentToken,
                                                                  @RequestParam(value = "currency", defaultValue = "NGN") Currency currency,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(merchantDashBoardService.getSuccessfulTransactionSummary(paymentToken, currency, channel, startDate, endDate));

    }

    @TokenValid()
    @GetMapping("get-virtual-transaction-summary")
    @Operation(
            summary = "Get Total Collections For Transfer",
            description = "This returns the total collections value and count, where transaction type could be STATIC or DYNAMIC")
    public ResponseEntity<Object> getSuccessfulTransactionSummary(@RequestParam(value = "transactionType") String transactionType,
                                                                  @RequestParam(value = "channel", required = false) PaymentChannel channel,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(merchantDashBoardService.getSuccessfulTransactionSummary(transactionType, channel, startDate, endDate));

    }

    @TokenValid()
    @GetMapping("get-dispute-summary")
    @Operation(
            summary = "Get Total Collections For Dispute",
            description = "This returns the total collections value and count")
    public ResponseEntity<Object> getSuccessfulDisputeSummary(@RequestParam(value = "startDate") String startDate,
                                                              @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(merchantDashBoardService.getSuccessfulDisputeSummary(startDate, endDate));

    }


}
