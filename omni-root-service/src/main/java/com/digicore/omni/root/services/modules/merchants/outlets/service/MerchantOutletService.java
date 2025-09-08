package com.digicore.omni.root.services.modules.merchants.outlets.service;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.merchant.enums.OutletStatus;
import com.digicore.omni.data.lib.modules.merchant.exception.MerchantExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantOutlet;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.projection.MerchantOutletProjection;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantOutletRepository;
import com.digicore.omni.data.lib.modules.merchant.repository.MerchantProfileRepository;
import com.digicore.omni.root.services.modules.merchants.outlets.mapper.MerchantOutletMapper;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.CreateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.UpdateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.model.response.MerchantOutletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantOutletService {

    private final MerchantOutletRepository merchantOutletRepository;

    private final MerchantProfileRepository merchantProfileRepository;

    private final MerchantOutletMapper merchantOutletMapper;

    public MerchantOutletResponse createOutlet(CreateOutletRequest request){
        String merchantEmail = ClientUtil.getLoggedInUsername();

        MerchantProfile merchantProfile = merchantProfileRepository.findFirstByEmailOrderByDateCreatedDesc(merchantEmail).orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));
        validateIfExistByTitleAndMerchant(request.getTitle(), merchantProfile.getId());

        return merchantOutletMapper.toMerchantOutletResponse(merchantOutletRepository.save(merchantOutletMapper.toMerchantOutlet(request, merchantProfile)));

    }

    public MerchantOutletResponse updateOutlet(UpdateOutletRequest request){

        MerchantOutlet merchantOutlet = merchantOutletRepository.findById(request.getId()).orElseThrow(
                ()-> new ZeusRuntimeException(new ApiError("Merchant outlet does not exist with the id provided"))
        );
        String merchantEmail = ClientUtil.getLoggedInUsername();

        MerchantProfile merchantProfile = merchantProfileRepository.findFirstByEmailOrderByDateCreatedDesc(merchantEmail).orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));
        validateIfExistByTitleAndMerchantAndIdNot(request.getTitle(), merchantProfile.getId(), merchantOutlet.getId());

        return merchantOutletMapper.toMerchantOutletResponse(merchantOutletRepository.save(merchantOutletMapper.toMerchantOutlet(request, merchantOutlet)));

    }

    public Page<MerchantOutletResponse> getPaginatedMerchantResponse(OutletStatus status, Pageable pageable) {
        String merchantEmail = ClientUtil.getLoggedInUsername();

        MerchantProfile merchantProfile = merchantProfileRepository
                .findFirstByEmailOrderByDateCreatedDesc(merchantEmail)
                .orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));

        return merchantOutletRepository
                .findByMerchantProfileIdAndStatus(merchantProfile.getId(), status.name(), pageable)
                .map(merchantOutletMapper::toMerchantOutletResponse);
    }

    public MerchantOutletResponse getMerchantOutletById(Long id) {
        String merchantEmail = ClientUtil.getLoggedInUsername();

        MerchantProfile merchantProfile = merchantProfileRepository
                .findFirstByEmailOrderByDateCreatedDesc(merchantEmail)
                .orElseThrow(() -> MerchantExceptionProcessor.invalidMerchantProfile(merchantEmail));

        MerchantOutletProjection projection = merchantOutletRepository
                .findByIdAndMerchantProfileId(id, merchantProfile.getId())
                .orElseThrow(() -> new ZeusRuntimeException(new ApiError("Outlet not found with the id")));

        return merchantOutletMapper.toMerchantOutletResponse(projection);
    }

    public void validateIfExistByTitleAndMerchant(String title, Long id){
        if(merchantOutletRepository.existsByTitleAndMerchantProfileId(title,id)){
            throw new ZeusRuntimeException(new ApiError("Outlet title already exist"));
        }
    }

    public void validateIfExistByTitleAndMerchantAndIdNot(String title, Long merchantProfileId, Long id){
        if(merchantOutletRepository.existsByTitleAndMerchantProfileIdAndIdNot(title,merchantProfileId, id)){
            throw new ZeusRuntimeException(new ApiError("Outlet title already exist"));
        }
    }
}
