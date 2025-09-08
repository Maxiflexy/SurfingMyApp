package com.digicore.omni.root.services.modules.merchants.dispute.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.DisputeDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.dispute.DisputeDeclineDTO;

import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantDisputeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

/**
 * @author Monsuru
 * @since Nov-12(Sat)-2022
 */
@Service
@RequiredArgsConstructor
public class DisputeService {

    private final MerchantDisputeService merchantDisputeService;

    public DisputeDTO fetchDispute(String disputeId)  {

            return merchantDisputeService.fetchDispute(disputeId);

    }

    public PaginatedResponseApiModel<DisputeDTO> fetchAllMerchantDispute(Principal principal, int page, int pageSize){
        return merchantDisputeService.fetchAllMerchantDisputes(principal, page, pageSize);
    }

    public DisputeDTO acceptDispute(String disputeId, boolean isManualHandling)  {
            return merchantDisputeService.acceptDispute(disputeId, isManualHandling);

    }

    public DisputeDTO declineDispute(String disputeId, DisputeDeclineDTO disputeDeclineDTO) {
            return merchantDisputeService.declineDispute(disputeId, disputeDeclineDTO);

    }

    public PaginatedResponseApiModel<DisputeDTO> fetchAllPendingMerchantDispute(Principal principal, int page, int pageSize) {
        return merchantDisputeService.fetchAllPendingMerchantDisputes(principal, page, pageSize);
    }

    public PaginatedResponseApiModel<DisputeDTO> fetchAllPastMerchantDispute(Principal principal, int page, int pageSize) {
        return merchantDisputeService.fetchAllPastMerchantDisputes(principal, page, pageSize);
    }

    public PaginatedResponseApiModel<DisputeDTO> fetchAllAcceptedMerchantDispute(String merchantId, int page, int pageSize) {
        return merchantDisputeService.fetchAllAcceptedMerchantDisputes(merchantId, page, pageSize);
    }

    public PaginatedResponseApiModel<DisputeDTO> fetchAllDeclinedMerchantDispute(String merchantId, int page, int pageSize) {
        return merchantDisputeService.fetchAllDeclinedMerchantDisputes(merchantId, page, pageSize);
    }

    public PaginatedResponseApiModel<DisputeDTO> fetchDisputesByFilter(String disputeId, LocalDate startDate, LocalDate endDate,
                                                                       String paymentChannel, String paymentToken, String disputeStatus,
                                                                       String payerName, String transactionStatus, int page, int pageSize,
                                                                       Principal principal) {
        return merchantDisputeService.fetchDisputeByFilter(disputeId, startDate, endDate, paymentChannel, paymentToken,
                disputeStatus, payerName, transactionStatus, principal, page, pageSize);
    }

    public void downloadAllMerchantDisputeInExcelForMerchant(HttpServletResponse res, String paymentChannel, String paymentToken,
                                                             String disputeStatus, String payerName, String transactionStatus,
                                                             LocalDate startDate, LocalDate endDate, Principal principal)  {
        merchantDisputeService.exportAllMerchantDisputeAsExcelFileForMerchant(res, paymentChannel, paymentToken, disputeStatus,
                payerName, transactionStatus, startDate, endDate, principal);
    }

    public void downloadAllMerchantDisputeInPdfForMerchant(HttpServletResponse res, String paymentChannel, String paymentToken,
                                                           String disputeStatus, String payerName, String transactionStatus,
                                                           LocalDate startDate, LocalDate endDate, Principal principal)  {
        merchantDisputeService.exportAllMerchantDisputeAsPdfFileForMerchant(res, paymentChannel, paymentToken, disputeStatus,
                payerName, transactionStatus, startDate, endDate, principal);
    }

    public void downloadAllMerchantDisputeInCsvForMerchant(HttpServletResponse res, String paymentChannel, String paymentToken,
                                                           String disputeStatus, String payerName, String transactionStatus,
                                                           LocalDate startDate, LocalDate endDate, Principal principal)  {
        merchantDisputeService.exportAllMerchantDisputeAsCsvFileForMerchant(res, paymentChannel, paymentToken, disputeStatus,
                payerName, transactionStatus, startDate, endDate, principal);
    }
    public CompletableFuture<ReportGeneratorService.ReportResponse> downloadAllMerchantDisputeInCsvForMerchant(String paymentChannel, String paymentToken,
                                                                                                               String disputeStatus, String payerName, String transactionStatus,
                                                                                                               String startDate, String endDate)  {
       return merchantDisputeService.exportAllMerchantDisputeAsCSV(paymentChannel, paymentToken, disputeStatus,
                payerName, transactionStatus, startDate, endDate, ClientUtil.getLoggedInUsername());
    }

    public PaginatedResponseApiModel<DisputeDTO> searchMerchantDispute(String searchKey, int page, int size) {
        return merchantDisputeService.searchMerchantDisputeByGenericFilter(searchKey, page, size);
    }
}
