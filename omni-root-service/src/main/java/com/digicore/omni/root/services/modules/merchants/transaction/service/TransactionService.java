package com.digicore.omni.root.services.modules.merchants.transaction.service;


import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.SearchRequest;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.VirtualAccountTransactionsDTO;


import com.digicore.omni.root.lib.modules.common.services.CsvService;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.services.VirtualAccountService;
import com.digicore.omni.root.lib.modules.common.utils.CsvDto;
import com.digicore.omni.root.lib.modules.common.utils.DateUtils;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantTransactionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class TransactionService {

private final MerchantTransactionService merchantTransactionService;
private final VirtualAccountService virtualAccountService;
public final CsvService csvService;


    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> getMerchantVirtualAccountTransaction(int pageNumber , int pageSizes ) {
     return virtualAccountService.getAllVirtualAccountTransactionsByMerchantId(pageSizes,pageNumber);

    }

    public PaginatedResponseApiModel<MerchantTransaction> getMerchantTransaction(int pageNumber , int pageSizes) {
        return merchantTransactionService.fetchAllMerchantTransactions(pageNumber,pageSizes);

    }

    public List<String> getMerchantOutlets() {
        return merchantTransactionService.retrieveMerchantOutlets();

    }




    public CompletableFuture<ReportGeneratorService.ReportResponse> downloadAllMerchantTransactionInCSVForMerchant(String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {

       return merchantTransactionService.exportAllMerchantTransactionsAsCSVForMerchant(startDate,endDate,transactionFilterDTO, ClientUtil.getLoggedInUsername());
    }
    public PaginatedResponseApiModel<MerchantTransaction> searchMerchantTransactionByTableHeader(String searchKey, int page, int size) {
        return merchantTransactionService.searchMerchantTransactionByGenericFilter(searchKey, page, size);
    }

    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> searchStaticTransactionByTableHeader(String searchKey, int page, int size) {
        return virtualAccountService.searchMerchantVirtualAccountByGenericFilter(searchKey, page, size);
    }

    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> filterMerchantTransactionByTableHeader(SearchRequest searchRequest) {
        return virtualAccountService.filterForMerchant(searchRequest);
    }

    public void downloadVirtualAccountTransactionsInCSV(HttpServletResponse response, SearchRequest searchRequest) {
        CsvDto<VirtualAccountTransactionsDTO> csvDto = new CsvDto<>();
        csvDto.setSearchRequest(searchRequest);
        csvDto.setResponse(response);
        csvService.prepareCSVExport(csvDto, virtualAccountService::prepareMerchantCSV);
    }

    public PaginatedResponseApiModel<MerchantTransaction> getTransactionByFilter(String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
        return merchantTransactionService.fetchByTransactionFilter(startDate, endDate, transactionFilterDTO);
    }
}
