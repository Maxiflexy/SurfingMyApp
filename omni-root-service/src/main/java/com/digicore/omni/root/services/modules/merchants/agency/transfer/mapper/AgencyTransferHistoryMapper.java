package com.digicore.omni.root.services.modules.merchants.agency.transfer.mapper;

import com.digicore.omni.data.lib.modules.transfer.dto.response.DepositTransactionHistoryProjection;
import com.digicore.omni.data.lib.modules.transfer.dto.response.DepositTransactionHistoryResponse;
import org.springframework.stereotype.Component;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-21(Thu)-2025
 */
@Component
public class AgencyTransferHistoryMapper {

    public static DepositTransactionHistoryResponse mapToResponse(DepositTransactionHistoryProjection projection) {
        return new DepositTransactionHistoryResponse(
                projection.getSourceAccountNumber(),
                projection.getSourceAccountName(),
                projection.getCustomerNumber(),
                projection.getAmount(),
                projection.getVat(),
                projection.getCharge(),
                projection.getDestinationAccountName(),
                projection.getDestinationAccountNumber(),
                projection.getDestinationBankCode(),
                projection.getDestinationBankName(),
                projection.getCustomReference(),
                projection.getNarration(),
                projection.getLookupReference(),
                projection.getProvider(),
                projection.getReference(),
                projection.getBankReference(),
                projection.getSessionId(),
                projection.getTransactionType(),
                projection.getTransactionDateTime(),
                projection.getTerminalInventoryId()
        );
    }
}