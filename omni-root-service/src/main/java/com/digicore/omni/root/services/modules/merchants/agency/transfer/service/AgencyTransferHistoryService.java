package com.digicore.omni.root.services.modules.merchants.agency.transfer.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.merchant.exception.MerchantExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantProfileRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantUserRepository;
import com.digicore.omni.data.lib.modules.transfer.dto.response.DepositTransactionHistoryResponse;
import com.digicore.omni.data.lib.modules.transfer.repository.DepositTransactionHistoryRepository;
import com.digicore.omni.root.services.modules.merchants.agency.transfer.mapper.AgencyTransferHistoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-21(Thu)-2025
 */
@Service
@AllArgsConstructor
public class AgencyTransferHistoryService {
    private final DepositTransactionHistoryRepository depositTransactionHistoryRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final MerchantUserRepository merchantUserRepository;

    public Page<DepositTransactionHistoryResponse> getTransferHistory(int pageNumber, int pageSize, String searchTerm) {
        String merchantId = retrieveMerchantID(ClientUtil.getLoggedInUsername());
        return depositTransactionHistoryRepository
                .findDepositTransactionHistoriesByTerminalInventory_MerchantId(merchantId, searchTerm, PageRequest.of(pageNumber, pageSize))
                .map(AgencyTransferHistoryMapper::mapToResponse);
    }

    private String retrieveMerchantID(String email) {
        Optional<MerchantProfile> merchantProfile = merchantProfileRepository.findFirstByEmailOrderByDateCreatedDesc(email);
        if (merchantProfile.isEmpty())
            return fetchMerchantIdByUserEmail(email);
        return merchantProfile.get().getMerchantId();
    }

    private String fetchMerchantIdByUserEmail(String merchantUserEmail) {
        return merchantUserRepository.findFirstByEmailOrderByDateCreatedDesc(merchantUserEmail)
                .orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantUserProfile(merchantUserEmail))
                .getMerchantProfile().getMerchantId();
    }
}