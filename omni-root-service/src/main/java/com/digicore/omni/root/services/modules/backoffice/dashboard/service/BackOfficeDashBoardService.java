package com.digicore.omni.root.services.modules.backoffice.dashboard.service;


import com.digicore.omni.data.lib.modules.common.enums.Currency;
import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.data.lib.modules.merchant.dto.TransactionSummaryDTO;
import com.digicore.omni.root.lib.modules.backoffice.response.BackOfficeDashBoardResponse;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeDashboardService;
import com.digicore.omni.root.lib.modules.common.reponse.DashBoardChartResponse;
import com.digicore.omni.root.lib.modules.merchant.response.DashBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BackOfficeDashBoardService {

    private final BackOfficeDashboardService backOfficeDashboardService;

    public BackOfficeDashBoardResponse getAllSuccessfulTransactionsAndMerchantInfo(PaymentChannel channel, String transactionMode, String startDate, String endDate){
        return backOfficeDashboardService.getSuccessfulTransactionSummary(transactionMode,startDate,endDate);
       // return backOfficeDashboardService.getAllSuccessfulTransactionsAndMerchantInfo(channel,transactionMode,startDate,endDate);
    }


    public DashBoardChartResponse getBackOfficeTransactionChart(String year, PaymentChannel channel, String mode){
        return DashBoardChartResponse.builder()
                .jan(getBackofficeTransactionByDate(year,1,channel,mode))
                .feb(getBackofficeTransactionByDate(year,2,channel,mode))
                .mar(getBackofficeTransactionByDate(year,3,channel,mode))
                .apr(getBackofficeTransactionByDate(year,4,channel,mode))
                .may(getBackofficeTransactionByDate(year,5,channel,mode))
                .jun(getBackofficeTransactionByDate(year,6,channel,mode))
                .jul(getBackofficeTransactionByDate(year,7,channel,mode))
                .aug(getBackofficeTransactionByDate(year,8,channel,mode))
                .sep(getBackofficeTransactionByDate(year,9,channel,mode))
                .oct(getBackofficeTransactionByDate(year,10,channel,mode))
                .nov(getBackofficeTransactionByDate(year,11,channel,mode))
                .dec(getBackofficeTransactionByDate(year,12,channel,mode))
                .total(getBackOfficeYear(year,channel,mode))
                .build();
    }

    public DashBoardResponse getBackOfficeData(){
        return backOfficeDashboardService.getAllMerchantsData();
    }

    public Integer allMerchant(){
        return backOfficeDashboardService.totalMerchants();
    }

    public int getBackofficeTransactionByDate(String year, int month, PaymentChannel
                                               channel, String transactionMode){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), month,1,0,0,0);
        LocalDateTime endDate = startDate.plusMonths(1L);

        return backOfficeDashboardService.getAllSuccessfulBackOfficeTransactionByDate(startDate,endDate,channel,transactionMode).size();
    }

    public int getBackOfficeYear(String year, PaymentChannel channel, String transactionMode){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), 1,1,0,0,0);
        LocalDateTime endDate = startDate.plusYears(1L);

        return backOfficeDashboardService.getAllSuccessfulBackOfficeTransactionByDate(startDate,endDate,channel,transactionMode).size();
    }

    public BackOfficeDashBoardResponse fetchAllSuccessfulBackOfficeTransactionCountByPaymentType(PaymentChannel channel, String transactionMode,String startDate, String endDate) {
        return backOfficeDashboardService.getAllSuccessfulBackOfficeTransactionCountByPaymentType(channel,transactionMode,startDate,endDate);
    }

    public BackOfficeDashBoardResponse fetchAllSuccessfulBackOfficeTransactionPercentageByPaymentType(PaymentChannel channel, String transactionMode,String startDate, String endDate) {
        return backOfficeDashboardService.getAllSuccessfulBackOfficeTransactionPercentageByPaymentType(channel, transactionMode,startDate,endDate);
    }

    public List<Integer> fetchAllDistinctBackOfficeTransactionYears() {
        return backOfficeDashboardService.getAllDistinctBackOfficeTransactionYears();
    }

    public int getBackOfficeTransactionValueByDate(String year, int month, PaymentChannel channel, String mode){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), month,1,0,0,0);
        LocalDateTime endDate = startDate.plusMonths(1L);

        return backOfficeDashboardService.getSumOfAllBackOfficeTransactionPerDate(startDate, endDate, channel, mode).intValue();
    }

    public int getMerchantTransactionValueYear(String year, PaymentChannel channel, String mode){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), 1,1,0,0,0);
        LocalDateTime endDate = startDate.plusYears(1L);

        return backOfficeDashboardService.getSumOfAllBackOfficeTransactionPerDate(startDate, endDate, channel, mode).intValue();
    }

    public DashBoardChartResponse getMerchantTransactionValueChart(String year, PaymentChannel channel, String mode) {
        return DashBoardChartResponse.builder()
                .jan(getBackOfficeTransactionValueByDate(year, 1, channel, mode))
                .feb(getBackOfficeTransactionValueByDate(year, 2, channel, mode))
                .mar(getBackOfficeTransactionValueByDate(year, 3, channel, mode))
                .apr(getBackOfficeTransactionValueByDate(year, 4, channel, mode))
                .may(getBackOfficeTransactionValueByDate(year, 5, channel, mode))
                .jun(getBackOfficeTransactionValueByDate(year, 6, channel, mode))
                .jul(getBackOfficeTransactionValueByDate(year, 7, channel, mode))
                .aug(getBackOfficeTransactionValueByDate(year, 8, channel, mode))
                .sep(getBackOfficeTransactionValueByDate(year, 9, channel, mode))
                .oct(getBackOfficeTransactionValueByDate(year, 10, channel, mode))
                .nov(getBackOfficeTransactionValueByDate(year, 11, channel, mode))
                .dec(getBackOfficeTransactionValueByDate(year, 12, channel, mode))
                .total(getMerchantTransactionValueYear(year, channel, mode))
                .build();
    }

    public TransactionSummaryDTO getSuccessfulTransactionSummary(Currency currency, PaymentChannel channel, String startDate, String endDate, String apiKeyMode) {
        return backOfficeDashboardService.getDashboardSuccessfulTransactionSummary(currency, channel, startDate, endDate, apiKeyMode);
    }

    public TransactionSummaryDTO getSuccessfulTransactionSummary(PaymentToken paymentToken, Currency currency, PaymentChannel channel, String startDate, String endDate, String apiKeyMode) {
        return backOfficeDashboardService.getDashboardSuccessfulTransactionSummary(paymentToken, currency, channel, startDate, endDate, apiKeyMode);
    }

    public TransactionSummaryDTO getSuccessfulTransactionSummary(String transactionType, PaymentChannel channel, String startDate, String endDate, String apiKeyMode) {
        return backOfficeDashboardService.getDashboardSuccessfulTransactionSummary(transactionType, channel, startDate, endDate, apiKeyMode);
    }

    public TransactionSummaryDTO getSuccessfulDisputeSummary(String startDate, String endDate, String apiKeyMode) {
        return backOfficeDashboardService.getDashboardSuccessfulSuccessfulDisputeSummary(startDate, endDate, apiKeyMode);
    }

}
