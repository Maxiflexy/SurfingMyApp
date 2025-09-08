package com.digicore.omni.root.services.modules.merchants.agency.vas.mapper;

import com.digicore.omni.data.lib.modules.vas.dto.response.VasTransactionHistoryProjection;
import com.digicore.omni.data.lib.modules.vas.dto.response.VasTransactionHistoryResponse;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-21(Thu)-2025
 */
public class AgencyVasTransactionHistoryMapper {
    public static VasTransactionHistoryResponse mapToResponse(VasTransactionHistoryProjection projection) {
        return new VasTransactionHistoryResponse(
                projection.getMerchantId(),
                projection.getCategoryId(),
                projection.getBillerId(),
                projection.getBillerItemId(),
                projection.getBankCode(),
                projection.getCustomerId(),
                projection.getAmount(),
                projection.getCustomReference(),
                projection.getReference(),
                projection.getTransactionId(),
                projection.getVendorStatus(),
                projection.getVendorNarration(),
                projection.getMessage(),
                projection.getSuccess()
        );
    }
}