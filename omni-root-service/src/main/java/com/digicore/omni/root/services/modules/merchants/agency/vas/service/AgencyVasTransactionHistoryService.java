package com.digicore.omni.root.services.modules.merchants.agency.vas.service;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.merchant.exception.MerchantExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantProfileRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantUserRepository;
import com.digicore.omni.data.lib.modules.vas.dto.response.VasTransactionHistoryResponse;
import com.digicore.omni.data.lib.modules.vas.repository.VasTransactionHistoryRepository;
import com.digicore.omni.root.services.modules.merchants.agency.vas.mapper.AgencyVasTransactionHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-21(Thu)-2025
 */
@Service
@RequiredArgsConstructor
public class AgencyVasTransactionHistoryService {
    private final VasTransactionHistoryRepository vasTransactionHistoryRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final MerchantUserRepository merchantUserRepository;

    public Page<VasTransactionHistoryResponse> getVasTransactionHistory(String searchTerm, int pageNumber, int pageSize) {
        String merchantId = retrieveMerchantID(ClientUtil.getLoggedInUsername());
        return vasTransactionHistoryRepository
                .getVasTransactionHistoriesByMerchantId(merchantId, searchTerm, PageRequest.of(pageNumber, pageSize))
                .map(AgencyVasTransactionHistoryMapper::mapToResponse);
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
