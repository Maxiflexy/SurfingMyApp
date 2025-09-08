package com.digicore.omni.root.services.modules.backoffice.settlement.controller;


import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.enums.SettlementStatus;
import com.digicore.omni.data.lib.modules.merchant.dto.SettlementDTO;
import com.digicore.omni.root.services.modules.backoffice.settlement.service.BackOfficeSettlementService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.digicore.registhentication.util.PageableUtil.*;

/**
 * @author Monsuru
 * @since Dec-27(Tue)-2022
 */
@RestController
@RequestMapping("/api/v1/backoffice/settlement/process/")
@RequiredArgsConstructor
public class BackOfficeSettlementController {

    private final BackOfficeSettlementService backOfficeSettlementService;

    @TokenValid
    @GetMapping("get-all")
    public ResponseEntity<Object> fetchAllSettlements(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                      @RequestParam(value = "status") SettlementStatus settlementStatus,
                                                      @RequestParam("transactionMode") String transactionMode){
        return CommonUtils.buildSuccessResponse(backOfficeSettlementService.fetchAllBySettlementStatus(settlementStatus,page, pageSize,transactionMode));
    }

    @TokenValid
    @GetMapping("get-details")
    public ResponseEntity<Object> getSettlementDetails(@RequestParam("settlementTransactionId") String settlementTransactionId,
                                                       @RequestParam("transactionMode") String transactionMode){
        return CommonUtils.buildSuccessResponse(backOfficeSettlementService.fetchSettlementDetails(settlementTransactionId,transactionMode));
    }

    @TokenValid
    @GetMapping("get-transactions")
    public ResponseEntity<Object> getSettlementTransactions(@RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int pageNumber,
                                                            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int pageSize,@RequestParam("settlementTransactionId") String settlementTransactionId,@RequestParam("transactionMode") String transactionMode){
        return CommonUtils.buildSuccessResponse(backOfficeSettlementService.fetchSettlementTransactions(pageNumber,pageSize,settlementTransactionId,transactionMode));
    }

    @TokenValid
    @GetMapping("fetch-by-settlement-filter")
    public ResponseEntity<Object> fetchByMerchantSettlementFilter(@RequestParam(value = "settlementTransactionId", required = false) String settlementTransactionId,
                                                                                    @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                                    @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                                                    @RequestParam(value = "paymentChannel", required = false) String paymentChannel,
                                                                                    @RequestParam(value = "paymentToken", required = false) String paymentToken,
                                                                                    @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
                                                                                    @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
                                                                                    @RequestParam(value = "merchantId", required = false) String merchantId,
                                                                                    @RequestParam(value = "settlementMode") String settlementMode,
                                                                                    @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)  {
        return CommonUtils.buildSuccessResponse(backOfficeSettlementService.fetchBySettlementFilter(settlementTransactionId, startDate, endDate, paymentChannel,
                paymentToken, settlementStatus, merchantBusinessName, merchantId, settlementMode, pageNumber, pageSize));
    }
//
//    @GetMapping("download-in-excel-for-backoffice")
//    public void downloadInExcelForBackOffice(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                             @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                             @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                             @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                             @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                             @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
//                                             @RequestParam(value = "merchantId", required = false) String merchantId,
//                                             @RequestParam(value = "settlementMode", required = false) String settlementMode,
//                                             HttpServletResponse response)  {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/octet-stream","xlsx", "backoffice-settlements");
//
//            backOfficeSettlementService.downloadSettlementInExcelFormat(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate,  merchantBusinessName, merchantId, settlementMode);
//
//    }
//
//    @GetMapping("download-in-pdf-for-backoffice")
//    public void downloadInPdfForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                         @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                         @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
//                                         @RequestParam(value = "merchantId", required = false) String merchantId,
//                                         @RequestParam(value = "settlementMode", required = false) String settlementMode,
//                                         HttpServletResponse response)   {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/pdf","pdf", "backoffice-settlements");
//
//            backOfficeSettlementService.downloadSettlementInPdfFormat(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate,  merchantBusinessName, merchantId, settlementMode);
//
//    }
//
//    @GetMapping("download-in-csv-for-backoffice")
//    public void downloadInCSVForMerchant(@RequestParam(value = "paymentChannel", required = false) String paymentChannel,
//                                         @RequestParam(value = "paymentToken", required = false) String paymentToken,
//                                         @RequestParam(value = "settlementStatus", required = false) String settlementStatus,
//                                         @RequestParam(value = "startDate",required = false) LocalDate startDate,
//                                         @RequestParam(value = "endDate", required = false) LocalDate endDate,
//                                         @RequestParam(value = "merchantBusinessName", required = false) String merchantBusinessName,
//                                         @RequestParam(value = "merchantId", required = false) String merchantId,
//                                         @RequestParam(value = "settlementMode", required = false) String settlementMode,
//                                         HttpServletResponse response)  {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "backoffice-settlements");
//
//            backOfficeSettlementService.downloadSettlementInCsvFormat(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, merchantBusinessName, merchantId, settlementMode);
//
//    }


//    @GetMapping("search")
//    @TokenValid
//    public ResponseEntity<Object> searchResellerProducts(
//            @RequestParam(value = PAGE_NUMBER, defaultValue = PAGE_NUMBER_DEFAULT_VALUE, required = false) int pageNumber,
//            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_DEFAULT_VALUE, required = false) int pageSize,
//            @RequestParam("transactionMode") String transactionMode,
//            @RequestParam(value = VALUE) String value) {
//        BillentSearchRequest billentSearchRequest = new BillentSearchRequest();
//        billentSearchRequest.setPage(pageNumber);
//        billentSearchRequest.setSize(pageSize);
//        billentSearchRequest.setValue(value);
//        return ControllerResponse.buildSuccessResponse(
//                resellerBillerProductService.searchForProducts(billentSearchRequest),
//                "Searched for products successfully");
//    }

    @TokenValid()
    @PostMapping("initiation")
    public ResponseEntity<Object> processMerchantSettlement(@RequestParam String settlementTransactionId) {
        SettlementDTO settlementDetails = backOfficeSettlementService.fetchSettlementDetails(settlementTransactionId);
        SettlementDTO settlementDTO = new SettlementDTO();
        settlementDTO.setSettlementTransactionId(settlementTransactionId);
        settlementDTO.setTransactionAmount(settlementDetails.getTransactionAmount());
        settlementDTO.setTransactionFee(settlementDetails.getTransactionFee());
        settlementDTO.setActualSettlementAmount(settlementDetails.getActualSettlementAmount());
        settlementDTO.setSettlementFee(settlementDetails.getSettlementFee());
        settlementDTO.setSettlementAmountDue(settlementDetails.getSettlementAmountDue());
        settlementDTO.setDestinationAccountName(settlementDetails.getDestinationAccountName());
        settlementDTO.setDestinationBankName(settlementDetails.getDestinationBankName());
        settlementDTO.setDestinationAccountNumber(settlementDetails.getDestinationAccountNumber());
        settlementDTO.setMerchantBusinessName(settlementDetails.getMerchantBusinessName());
        settlementDTO.setSettlementInitiator(ClientUtil.getLoggedInUsername());
        settlementDTO.setSettlementInitiationTime(settlementDetails.getSettlementInitiationTime());
        settlementDTO.setSettlementMode(settlementDetails.getSettlementMode());
        settlementDTO.setSettlementTransactionsCount(settlementDetails.getSettlementTransactionsCount());
        settlementDTO.setDisputedTransactionsCount(settlementDetails.getDisputedTransactionsCount());
        return CommonUtils.buildSuccessResponse(backOfficeSettlementService.processDueMerchantSettlement(settlementDTO, null));
    }


}
