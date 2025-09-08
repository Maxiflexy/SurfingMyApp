package com.digicore.omni.root.services.modules.backoffice.settlement.service;


import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.services.ApiClientService;

import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.enums.SettlementStatus;
import com.digicore.omni.data.lib.modules.merchant.dto.SettlementDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.SettlementTransactionsDTO;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantSettlementService;
import com.digicore.request.processor.annotations.MakerChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author Monsuru
 * @since Dec-27(Tue)-2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BackOfficeSettlementService {

    @Value("${web.engine.external.service.settlement.engine.endpoint:http://localhost:2753/api/v1/settlement/process/}")
    private String settlementEngineEndpoint;

    private final BackOfficeMerchantSettlementService settlementService;

    private final ApiClientService apiClientService;



    public PaginatedResponseApiModel<SettlementDTO> fetchAllBySettlementStatus(SettlementStatus settlementStatus, int page, int pageSize, String mode){
        return settlementService.fetchAllBySettlementStatus(settlementStatus, page, pageSize,mode);
    }

    public SettlementDTO fetchSettlementDetails(String settlementTransactionId, String mode){
        return settlementService.fetchSettlementDetails(settlementTransactionId,mode);
    }

    public SettlementDTO fetchSettlementDetails(String settlementTransactionId){
        return settlementService.fetchSettlementDetails(settlementTransactionId);
    }

    public PaginatedResponseApiModel<SettlementTransactionsDTO> fetchSettlementTransactions(int page, int pageSize, String settlementTransactionId, String mode){
        return settlementService.fetchSettlementTransactions(page, pageSize, settlementTransactionId,mode);
    }



    public PaginatedResponseApiModel<SettlementDTO> fetchBySettlementFilter(String settlementTransactionId,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate,
                                                                            String paymentChannel,
                                                                            String paymentToken,
                                                                            String settlementStatus,
                                                                            String merchantBusinessName,
                                                                            String merchantId,
                                                                            String settlementMode,
                                                                            int page,
                                                                            int pageSize)  {
        return settlementService.fetchBySettlementFilter(settlementTransactionId, startDate, endDate, paymentChannel, paymentToken,
                settlementStatus, merchantBusinessName, merchantId, settlementMode, page, pageSize);
    }
//
//    public void downloadSettlementInExcelFormat(HttpServletResponse res, String paymentChannel, String paymentToken,
//                                                String settlementStatus, LocalDate startDate, LocalDate endDate,
//                                                String merchantBusinessName, String merchantId, String settlementMode)  {
//        settlementService.exportSettlementsAsExcelFile(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, merchantBusinessName, merchantId, settlementMode);
//    }
//
//    public void downloadSettlementInPdfFormat(HttpServletResponse res, String paymentChannel, String paymentToken,
//                                              String settlementStatus, LocalDate startDate, LocalDate endDate,
//                                              String merchantBusinessName, String merchantId, String settlementMode)  {
//        settlementService.exportSettlementsAsPdfFile(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, merchantBusinessName, merchantId, settlementMode);
//    }
//
//    public void downloadSettlementInCsvFormat(HttpServletResponse res, String paymentChannel, String paymentToken,
//                                              String settlementStatus, LocalDate startDate, LocalDate endDate,
//                                              String merchantBusinessName, String merchantId, String settlementMode)  {
//        settlementService.exportSettlementsAsCsvFile(res, paymentChannel, paymentToken, settlementStatus, startDate, endDate, merchantBusinessName, merchantId, settlementMode);
//    }

    @MakerChecker(checkerPermission = "approve-or-disapprove-transfers", makerPermission = "create-single-or-bulk-transfer", requestClassName = "com.digicore.omni.data.lib.modules.merchant.dto.SettlementDTO")
    public Object processDueMerchantSettlement(Object request, Object[] pendingFileDTOs) {
        SettlementDTO settlementRequest = (SettlementDTO) request;
        String url = settlementEngineEndpoint.concat("?settlementTransactionId=".concat(settlementRequest.getSettlementTransactionId()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            String jwtToken = jwtAuthentication.getToken().getTokenValue();
            log.info(">>> initiating settlement... settlement URL: {}", url);
            return apiClientService.postRequestWithBearerAuthorizationHeader(url, null, null, MediaType.APPLICATION_JSON,jwtToken);

            // Use the JWT token as needed
        }
        throw new ZeusRuntimeException("invalid user");

    }



}
