package com.digicore.omni.root.services.modules.merchants.submerchant.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.enums.PaymentToken;
import com.digicore.omni.data.lib.modules.common.permission.repository.RoleRepository;
import com.digicore.omni.data.lib.modules.merchant.enums.MerchantStatus;
import com.digicore.omni.data.lib.modules.merchant.exception.MerchantExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.model.SubMerchantInvite;
import com.digicore.omni.data.lib.modules.merchant.projection.CollectionSummaryProjection;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantProfileRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.SubMerchantInviteRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.TransactionRepository;
import com.digicore.omni.root.services.modules.merchants.submerchant.mapper.SubMerchantMapper;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantInviteRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantInviteResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantListResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantOverviewResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.digicore.omni.data.lib.modules.common.enums.PaymentToken.*;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SubMerchantService {

    private final SubMerchantInviteRepository subMerchantInviteRepository;
    private final MerchantProfileRepository merchantProfileRepository;
    private final TransactionRepository transactionRepository;
    private final RoleRepository roleRepository;
    private final SubMerchantMapper subMerchantMapper;
    private final PasswordEncoder passwordEncoder;

    public SubMerchantInvite inviteSubMerchant(SubMerchantInviteRequest request){
        validateIfExistByEmail(request.getEmail());
        String merchantEmail = ClientUtil.getLoggedInUsername();

        MerchantProfile merchantProfile = merchantProfileRepository.findFirstByEmailOrderByDateCreatedDesc(merchantEmail).orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));

        return subMerchantInviteRepository.save(subMerchantMapper.toSubMerchantInvite(request, merchantProfile));
    }

    public void validateIfExistByEmail(String email){
        if(subMerchantInviteRepository.existsByEmail(email)){
            throw new ZeusRuntimeException(new ApiError("Email already exist"));
        }
    }

    public SubMerchantInvite getMerchantInviteByEmailAndCodeAndStatusPending(String email, String inviteCode) {
        return subMerchantInviteRepository.findByEmailAndInviteCodeAndStatus(email, inviteCode, "PENDING")
                .orElseThrow(() -> new ZeusRuntimeException(new ApiError("Sub-merchant invite not found")));
    }

    public void saveSubMerchantInvite(SubMerchantInvite subMerchantUser) {
        subMerchantInviteRepository.save(subMerchantUser);
    }

    public Page<SubMerchantResponse> getAllSubMerchants(Pageable pageable, String search, MerchantStatus status) {
        String trimmedSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;

        Page<MerchantProfile> subMerchants = merchantProfileRepository
                .findSubMerchantsWithSearchAndFilter(getCurrentMerchantId(), trimmedSearch, status, pageable);
        return subMerchants.map(subMerchantMapper::mapToSubMerchantResponse);
    }

    public Page<SubMerchantInviteResponse> getPendingInvites(Pageable pageable) {
        Long merchantProfileId = getCurrentMerchantProfileId();
        Page<SubMerchantInvite> pendingInvites = subMerchantInviteRepository
                .findPendingInvitesByMerchantProfileId(merchantProfileId, pageable);
        return pendingInvites.map(this::mapToInviteResponse);
    }

    public SubMerchantResponse getSubMerchantByMerchantId(String subMerchantId) {
        MerchantProfile subMerchant = merchantProfileRepository
                .findByMerchantIdAndParentMerchantId(subMerchantId, getCurrentMerchantId())
                .orElseThrow(() -> new ZeusRuntimeException(new ApiError("SubMerchant not found or doesn't belong to current merchant")));

        return subMerchantMapper.mapToSubMerchantResponse(subMerchant);
    }

    public Page<SubMerchantResponse> getAllSubMerchants(Pageable pageable) {
        Page<MerchantProfile> subMerchants = merchantProfileRepository
                .findAllByParentMerchantIdOrderByDateCreatedDesc(getCurrentMerchantId(), pageable);
        return subMerchants.map(subMerchantMapper::mapToSubMerchantResponse);
    }


    public String getCurrentMerchantId() {
        String merchantEmail = ClientUtil.getLoggedInUsername();
        MerchantProfile merchantProfile = merchantProfileRepository
                .findFirstByEmailOrderByDateCreatedDesc(merchantEmail)
                .orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));
        return merchantProfile.getMerchantId();
    }

    private Long getCurrentMerchantProfileId() {
        String merchantEmail = ClientUtil.getLoggedInUsername();
        MerchantProfile merchantProfile = merchantProfileRepository
                .findFirstByEmailOrderByDateCreatedDesc(merchantEmail)
                .orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));
        return merchantProfile.getId();
    }

    private SubMerchantInviteResponse mapToInviteResponse(SubMerchantInvite invite) {
        return SubMerchantInviteResponse.builder()
                .id(invite.getId())
                .businessName(invite.getBusinessName())
                .email(invite.getEmail())
                .status(invite.getStatus())
                .inviteCode(invite.getInviteCode())
                .dateCreated(invite.getCreatedDate())
                .build();
    }

    public SubMerchantOverviewResponse getSubMerchantOverview() {
        String currentMerchantId = getCurrentMerchantId();

        List<String> subMerchantIds = merchantProfileRepository.fetchListOfMerchantIdsByParentMerchantId(currentMerchantId);
        Long totalSubMerchants = (long) subMerchantIds.size();

        if (subMerchantIds.isEmpty()) {
            return SubMerchantOverviewResponse.builder()
                    .totalSubMerchants(0L)
                    .totalCollections(buildEmptyCollectionSummary())
                    .totalCardCollections(buildEmptyCollectionSummary())
                    .webAndPosCollections(buildEmptyCollectionSummary())
                    .webAndPosCollectionsCards(buildEmptyCollectionSummary())
                    .webAndPosCollectionsUssd(buildEmptyCollectionSummary())
                    .webAndPosCollectionsQr(buildEmptyCollectionSummary())
                    .build();
        }

        // Get total collections for all sub-merchants
        SubMerchantOverviewResponse.CollectionSummary totalCollections =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIds(subMerchantIds));

        // Get total card collections
        SubMerchantOverviewResponse.CollectionSummary totalCardCollections =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentToken(subMerchantIds, "CARD"));

        // Get web and POS collections
        List<String> webAndPosChannels = Arrays.asList("WEB", "POS");
        SubMerchantOverviewResponse.CollectionSummary webAndPosCollections =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentChannels(subMerchantIds, webAndPosChannels));

        // Get web and POS collections (CARDs)
        SubMerchantOverviewResponse.CollectionSummary webAndPosCollectionsCards =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentChannelAndToken(subMerchantIds, webAndPosChannels, CARD.name()));

        // Get web and POS collections (TRANSFER/Virtual Account)
        SubMerchantOverviewResponse.CollectionSummary webAndPosCollectionVirtualAccount =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentChannelAndToken(subMerchantIds, webAndPosChannels, TRANSFER.name()));

        // Get web and POS collections (USSD)
        SubMerchantOverviewResponse.CollectionSummary webAndPosCollectionsUssd =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentChannelAndToken(subMerchantIds, webAndPosChannels, USSD.name()));

        // Get web and POS collections (QR)
        SubMerchantOverviewResponse.CollectionSummary webAndPosCollectionsQr =
                getCollectionSummaryFromProjection(transactionRepository.getTotalCollectionsByMerchantIdsAndPaymentChannelAndToken(subMerchantIds, webAndPosChannels, QR.name()));

        return SubMerchantOverviewResponse.builder()
                .totalSubMerchants(totalSubMerchants)
                .totalCollections(totalCollections)
                .totalCardCollections(totalCardCollections)
                .webAndPosCollections(webAndPosCollections)
                .webAndPosCollectionsCards(webAndPosCollectionsCards)
                .webAndPosCollectionVirtualAccount(webAndPosCollectionVirtualAccount)
                .webAndPosCollectionsUssd(webAndPosCollectionsUssd)
                .webAndPosCollectionsQr(webAndPosCollectionsQr)
                .build();
    }

    private SubMerchantOverviewResponse.CollectionSummary getCollectionSummaryFromProjection(CollectionSummaryProjection projection) {
        if (projection == null) {
            return buildEmptyCollectionSummary();
        }

        Double value = projection.getTotalAmount() != null ? projection.getTotalAmount() : 0.0;
        Long count = projection.getTransactionCount() != null ? projection.getTransactionCount() : 0L;

        return SubMerchantOverviewResponse.CollectionSummary.builder()
                .value(value)
                .count(count)
                .build();
    }

    private SubMerchantOverviewResponse.CollectionSummary buildEmptyCollectionSummary() {
        return SubMerchantOverviewResponse.CollectionSummary.builder()
                .value(0.0)
                .count(0L)
                .build();
    }
}
