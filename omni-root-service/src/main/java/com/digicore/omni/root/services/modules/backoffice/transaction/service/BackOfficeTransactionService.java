package com.digicore.omni.root.services.modules.backoffice.transaction.service;


import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.MerchantTransaction;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.SearchRequest;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.VirtualAccountTransactionsDTO;
import com.digicore.omni.data.lib.modules.merchant.model.Transaction;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantTransactionService;

import com.digicore.omni.root.lib.modules.common.services.CsvService;
import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.common.services.VirtualAccountService;
import com.digicore.omni.root.lib.modules.common.utils.CsvDto;
import com.digicore.omni.root.lib.modules.common.utils.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BackOfficeTransactionService {

    private final BackOfficeMerchantTransactionService transactionService;
    private final VirtualAccountService virtualAccountService;
    public final CsvService csvService;


    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> getMerchantVirtualAccountTransaction(int pageNumber , int pageSizes ) {
        return virtualAccountService.getAllVirtualAccountTransactions(pageSizes,pageNumber);

    }

    public List<String> getMerchantOutlets() {
        return transactionService.retrieveMerchantOutlets();
    }
    public PaginatedResponseApiModel<MerchantTransaction> getMerchantTransaction(int pageNumber , int pageSizes , String mode) {
        return transactionService.fetchAllMerchantTransactionsForBackOffice(pageNumber,pageSizes,mode);
    }

    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> searchStaticTransactionByTableHeader(String searchKey, int page, int size) {
        return virtualAccountService.searchVirtualAccountByGenericFilter(searchKey, page, size);
    }

    public PaginatedResponseApiModel<VirtualAccountTransactionsDTO> filterMerchantTransactionByTableHeader(SearchRequest searchRequest) {
        return virtualAccountService.filter(searchRequest);
    }

    public void downloadVirtualAccountTransactionsInCSV(HttpServletResponse response, SearchRequest searchRequest) {
        CsvDto<VirtualAccountTransactionsDTO> csvDto = new CsvDto<>();
        csvDto.setSearchRequest(searchRequest);
        csvDto.setResponse(response);
        csvService.prepareCSVExport(csvDto, virtualAccountService::prepareCSV);
    }

    public PaginatedResponseApiModel<MerchantTransaction> searchMerchantTransactionByTableHeader(String searchKey, String transactionMode, int page, int size) {
        return transactionService.searchMerchantTransactionByGenericFilter(searchKey, transactionMode, page, size);
    }





    public CompletableFuture<ReportGeneratorService.ReportResponse> downloadAllMerchantTransactionInCSV(String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
       return transactionService.exportAllMerchantTransactionsAsPDFCSV(startDate,endDate,transactionFilterDTO,ClientUtil.getLoggedInUsername());
    }


    public PaginatedResponseApiModel<MerchantTransaction> fetchTransactionsByFilter(String startDate, String endDate, TransactionFilterDTO transactionFilterDTO) {
        return transactionService.fetchByTransactionFilter(startDate, endDate, transactionFilterDTO);
    }
}
