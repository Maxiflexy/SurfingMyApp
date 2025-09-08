package com.digicore.omni.root.services.modules.backoffice.dashboard.controller;


import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.root.services.modules.backoffice.dashboard.service.BackOfficeDashBoardService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/backoffice/merchant-dashboard/process/")
@RequiredArgsConstructor
public class BackOfficeDashBoardController {

    private final BackOfficeDashBoardService backOfficeDashBoardService;

    @TokenValid()
    @GetMapping("fetch-all-successful-backoffice-transaction-count")
    public ResponseEntity<Object> getAllSuccessfulTransactionsAndMerchantInfo(@RequestParam(value = "channel", required = false) PaymentChannel channel, @RequestParam(value = "transactionMode") String transactionMode,
                                                                              @RequestParam(value = "startDate") String startDate,
                                                                              @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.getAllSuccessfulTransactionsAndMerchantInfo(channel, transactionMode, startDate, endDate), null);
    }

    @TokenValid()
    @GetMapping("successful-backoffice-dashboard-chart/{year}")
    public ResponseEntity<Object> getBackOfficeTransactionChart(@PathVariable String year,
                                                                @RequestParam(value = "channel", required = false) PaymentChannel channel, @RequestParam(value = "transactionMode") String transactionMode) {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.getBackOfficeTransactionChart(year, channel, transactionMode));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-backoffice-transaction-count-by-type")
    public ResponseEntity<Object> fetchAllSuccessfulBackOfficeTransactionCountByPaymentType(@RequestParam(value = "channel", required = false) PaymentChannel channel, @RequestParam(value = "transactionMode") String transactionMode,
                                                                                            @RequestParam(value = "startDate") String startDate,
                                                                                            @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.fetchAllSuccessfulBackOfficeTransactionCountByPaymentType(channel, transactionMode, startDate, endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-backoffice-transaction-percentage-by-type")
    public ResponseEntity<Object> fetchAllSuccessfulBackOfficeTransactionPercentageByPaymentType(@RequestParam(value = "channel", required = false) PaymentChannel channel, @RequestParam(value = "transactionMode") String transactionMode, @RequestParam(value = "startDate") String startDate,
                                                                                                 @RequestParam(value = "endDate") String endDate) {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.fetchAllSuccessfulBackOfficeTransactionPercentageByPaymentType(channel, transactionMode, startDate, endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-transaction-per-year")
    public ResponseEntity<Object> fetchAllDistinctBackOfficeTransactionYears() {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.fetchAllDistinctBackOfficeTransactionYears());
    }

    @TokenValid()
    @GetMapping("successful-backoffice-dashboard-chart-value/{year}")
    public ResponseEntity<Object> getBackOfficeTransactionValueChart(@PathVariable String year,
                                                                     @RequestParam(value = "channel", required = false) PaymentChannel channel, @RequestParam(value = "transactionMode") String transactionMode) {
        return CommonUtils.buildSuccessResponse(backOfficeDashBoardService.getMerchantTransactionValueChart(year, channel, transactionMode));
    }
}
