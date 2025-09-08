package com.digicore.omni.root.services.modules.merchants.dashboard.controller;


import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.dashboard.service.DashBoardService;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/merchant-dashboard/process/")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;



    @TokenValid()
    @GetMapping("successful-merchant-dashboard")
    public ResponseEntity<Object> getAllMerchantDataDashboard(Principal principal,
                                                              @RequestParam(value = "channel" , required = false) PaymentChannel channel,
                                                              @RequestParam(value = "transactionMode") String transactionMode,
                                                              @RequestParam(value = "startDate")String startDate,
                                                              @RequestParam(value = "endDate")String endDate){
        return CommonUtils.buildSuccessResponse(dashBoardService.getAllMerchantDataDashboard(principal,channel,transactionMode,startDate,endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-merchant-transaction-count-by-type")
    public ResponseEntity<Object> fetchAllSuccessfulMerchantTransactionCountByPaymentType(@RequestParam(value = "channel" , required = false)PaymentChannel channel,
                                                                                          @RequestParam(value = "startDate")String startDate,
                                                                                          @RequestParam(value = "endDate")String endDate) {
        return CommonUtils.buildSuccessResponse(dashBoardService.fetchAllSuccessfulMerchantTransactionCountByPaymentType(channel,startDate,endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-merchant-transaction-percentage-by-type")
    public ResponseEntity<Object> fetchAllSuccessfulMerchantTransactionPercentageByPaymentType(Principal principal,
                                                                                               @RequestParam(value = "channel", required = false)PaymentChannel channel,
                                                                                               @RequestParam(value = "transactionMode") String transactionMode,
                                                                                               @RequestParam(value = "startDate")String startDate,
                                                                                               @RequestParam(value = "endDate")String endDate) {
        return CommonUtils.buildSuccessResponse(dashBoardService.fetchAllSuccessfulMerchantTransactionPercentageByPaymentType(principal,channel,transactionMode,startDate,endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-merchant-{year}-monthly-transactions")
    public ResponseEntity<Object> fetchAllSuccessfulMerchantMonthlyTransactions(@PathVariable String year,
                                                                                @RequestParam(value = "channel", required = false)PaymentChannel channel){
        return CommonUtils.buildSuccessResponse(dashBoardService.getMerchantTransactionChart(year,channel));
    }

    @TokenValid()
    @GetMapping("fetch-all-merchant-dashboard-dispute-data")
    public ResponseEntity<Object> fetchAllMerchantDashboardDisputeData(Principal principal,
                                                                       @RequestParam(value = "channel", required = false)PaymentChannel channel,
                                                                       @RequestParam(value = "transactionMode") String transactionMode,
                                                                       @RequestParam(value = "startDate")String startDate,
                                                                       @RequestParam(value = "endDate")String endDate) {
        return CommonUtils.buildSuccessResponse(dashBoardService.fetchAllMerchantDashboardDisputeData(principal, channel, transactionMode, startDate, endDate));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-merchant-transactions-per-year")
    public ResponseEntity<Object> merchantDashboardUniqueTransactionYears(Principal principal) {
        return CommonUtils.buildSuccessResponse(dashBoardService.fetchAllDistinctMerchantTransactionYears(principal));
    }

    @TokenValid()
    @GetMapping("fetch-all-successful-merchant-{year}-monthly-transactions-value")
    public ResponseEntity<Object> getMerchantTransactionValueChart(Principal principal,
                                                                   @PathVariable String year,
                                                                   @RequestParam(value = "channel", required = false)PaymentChannel channel,
                                                                   @RequestParam(value = "transactionMode") String transactionMode){
        return CommonUtils.buildSuccessResponse(dashBoardService.getMerchantTransactionValueChart(year,channel));
    }

    @TokenValid()
    @GetMapping("fetch-static-virtual-account")
    public ResponseEntity<Object> fetchStaticVirtualAccount(){
     return CommonUtils.buildSuccessResponse(dashBoardService.generateStaticVirtualAccount());
    }

    @GetMapping("get-profile-picture")
    public void getMerchantProfilePicture(@RequestParam String merchantId, HttpServletResponse response) {
        String pathToFile = dashBoardService.getMerchantPathToUploadFile(merchantId);
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            String originalFileName = new File(pathToFile).getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", merchantId.concat("_").concat("Logo").concat(".").concat(fileExtension)));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }

}
