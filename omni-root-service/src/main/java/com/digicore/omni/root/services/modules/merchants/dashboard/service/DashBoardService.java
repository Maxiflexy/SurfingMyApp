package com.digicore.omni.root.services.modules.merchants.dashboard.service;

import com.digicore.api.helper.services.ApiClientService;
import com.digicore.omni.data.lib.modules.common.enums.PaymentChannel;
import com.digicore.omni.payment.common.lib.modules.virtual_account.response.VasResponse;
import com.digicore.omni.root.lib.modules.common.reponse.DashBoardChartResponse;
import com.digicore.omni.root.lib.modules.merchant.response.DashBoardResponse;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantDashBoardService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final MerchantDashBoardService merchantDashBoardService;

    private final MerchantService merchantService;

    private final ApiClientService apiClientService;

    @Value("${web.engine.external.service.virtual.account.endpoint:http://localhost:2760/virtual-id/}")
    private String virtualAccountEndpoint;


    public DashBoardResponse getAllMerchantDataDashboard(Principal principal, PaymentChannel channel, String mode, String startDate, String endDate){
      return merchantDashBoardService.getMerchantDashBoardData(principal,channel,mode,startDate,endDate);
    }

    public DashBoardResponse fetchAllSuccessfulMerchantTransactionCountByPaymentType(PaymentChannel channel,String startDate, String endDate) {
        return merchantDashBoardService.getAllSuccessfulMerchantTransactionCountByPaymentType(channel,startDate,endDate);
    }

    public DashBoardResponse fetchAllSuccessfulMerchantTransactionPercentageByPaymentType(Principal principal,PaymentChannel channel, String mode,String startDate, String endDate) {
        return merchantDashBoardService.getAllSuccessfulMerchantTransactionPercentageByPaymentType(principal,channel,mode,startDate,endDate);
    }

    public int getMerchantTransactionByDate(String year, int month, PaymentChannel channel){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), month,1,0,0,0);
        LocalDateTime endDate = startDate.plusMonths(1L);

        return merchantDashBoardService.getAllMerchantTransactionPerDate(startDate, endDate,channel).size();
    }

    public int getMerchantTransactionYear(String year, PaymentChannel channel){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), 1,1,0,0,0);
        LocalDateTime endDate = startDate.plusYears(1L);

        return merchantDashBoardService.getAllMerchantTransactionPerDate(startDate, endDate, channel).size();
    }

    public DashBoardChartResponse getMerchantTransactionChart(String year,PaymentChannel channel) {
        return DashBoardChartResponse.builder()
                .jan(getMerchantTransactionByDate(year, 1, channel))
                .feb(getMerchantTransactionByDate(year, 2, channel))
                .mar(getMerchantTransactionByDate(year, 3, channel))
                .apr(getMerchantTransactionByDate(year, 4, channel))
                .may(getMerchantTransactionByDate(year, 5, channel))
                .jun(getMerchantTransactionByDate(year, 6, channel))
                .jul(getMerchantTransactionByDate(year, 7, channel))
                .aug(getMerchantTransactionByDate(year, 8, channel))
                .sep(getMerchantTransactionByDate(year, 9, channel))
                .oct(getMerchantTransactionByDate(year, 10, channel))
                .nov(getMerchantTransactionByDate(year, 11, channel))
                .dec(getMerchantTransactionByDate(year, 12, channel))
                .total(getMerchantTransactionYear(year, channel))
                .build();
    }

    public DashBoardResponse fetchAllMerchantDashboardDisputeData(Principal principal,PaymentChannel channel, String mode,String startDate, String endDate) {
        return merchantDashBoardService.getMerchantDashboardDisputeData(principal,channel,mode,startDate,endDate);
    }

    public List<Integer> fetchAllDistinctMerchantTransactionYears(Principal principal) {
        return merchantDashBoardService.getAllDistinctMerchantTransactionYears(principal);
    }

    public int getMerchantTransactionValueByDate(String year, int month,  PaymentChannel channel){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), month,1,0,0,0);
        LocalDateTime endDate = startDate.plusMonths(1L);

        return merchantDashBoardService.getSumOfAllMerchantTransactionPerDate(startDate, endDate, channel).intValue();
    }

    public int getMerchantTransactionValueYear(String year,PaymentChannel channel){
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), 1,1,0,0,0);
        LocalDateTime endDate = startDate.plusYears(1L);

        return merchantDashBoardService.getSumOfAllMerchantTransactionPerDate(startDate, endDate, channel).intValue();
    }

    public DashBoardChartResponse getMerchantTransactionValueChart(String year,PaymentChannel channel) {
        return DashBoardChartResponse.builder()
                .jan(getMerchantTransactionValueByDate(year, 1, channel))
                .feb(getMerchantTransactionValueByDate(year, 2, channel))
                .mar(getMerchantTransactionValueByDate(year, 3,  channel))
                .apr(getMerchantTransactionValueByDate(year, 4, channel))
                .may(getMerchantTransactionValueByDate(year, 5,  channel))
                .jun(getMerchantTransactionValueByDate(year, 6, channel))
                .jul(getMerchantTransactionValueByDate(year, 7, channel))
                .aug(getMerchantTransactionValueByDate(year, 8,  channel))
                .sep(getMerchantTransactionValueByDate(year, 9,  channel))
                .oct(getMerchantTransactionValueByDate(year, 10,  channel))
                .nov(getMerchantTransactionValueByDate(year, 11,  channel))
                .dec(getMerchantTransactionValueByDate(year, 12,  channel))
                .total(getMerchantTransactionValueYear(year,  channel))
                .build();
    }

    public Map<String, Object> generateStaticVirtualAccount(){
        Map<String,Object> merchantStaticAccount = new HashMap<>();
        String merchantId = merchantService.isMerchantDueForAStaticVirtualAccount();
        if(!merchantId.isEmpty()){
            VasResponse response = apiClientService.postRequest(virtualAccountEndpoint.concat("static-account-generation?accountName=".concat(merchantId)), null, VasResponse.class, MediaType.APPLICATION_JSON);
            merchantStaticAccount.put("staticVirtualAccountNumber",response.getAccountNumber());
            merchantStaticAccount.put("staticVirtualAccountName",response.getAccountName());
            merchantStaticAccount.put("staticVirtualAccountGenerated",true);
            merchantStaticAccount.put("staticVirtualAccountProvider", response.getProvider());
            merchantService.tieStaticVirtualAccountToMerchantProfile(merchantId,response.getAccountNumber());

            return merchantStaticAccount;
        }
        merchantStaticAccount.put("staticVirtualAccountNumber","N/A");
        merchantStaticAccount.put("staticVirtualAccountName","N/A");
        merchantStaticAccount.put("staticVirtualAccountGenerated",false);
        return merchantStaticAccount;
    }

    public String getMerchantPathToUploadFile(String merchantId) {
        return merchantDashBoardService.getPathToUploadedFile(merchantId, "Logo");
    }

}
