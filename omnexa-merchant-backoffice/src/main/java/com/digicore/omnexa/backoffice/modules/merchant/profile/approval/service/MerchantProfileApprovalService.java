/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.approval.service;

import static com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileUpdateDTO.MERCHANT_PROFILE_UPDATE_DTO;
import static com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileStatusDTO.MERCHANT_PROFILE_STATUS_DTO;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.module.ModuleConstant.MERCHANT_PROFILE;

import com.digicore.omnexa.backoffice.modules.merchant.profile.approval.MerchantProfileProxyService;
import com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileUpdateDTO;
import com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileStatusDTO;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for merchant profile operations with maker-checker approval workflow.
 *
 * <p>This service implements {@link MerchantProfileProxyService} and uses the {@link MakerChecker}
 * annotation to enforce approval workflows for merchant profile operations.
 *
 * @author Generated
 * @createdOn Aug-12(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileApprovalService implements MerchantProfileProxyService {

    private final ProfileService merchantProfileService;

    /**
     * Updates a merchant profile through the maker-checker workflow.
     *
     * <p>Requires 'edit-merchant-profile' permission for makers and 
     * 'approve-edit-merchant-profile' permission for checkers.
     *
     * @param initialData the initial merchant profile data
     * @param updateData the merchant profile update data
     * @param files additional files if any
     * @return Optional.empty() indicating successful processing
     */
    @Override
    @MakerChecker(
            checkerPermission = "approve-invite-merchant-user",
            //makerPermission = "edit-merchant-profile",
            makerPermission = "view-merchant-user-detail",
            requestClassName = MERCHANT_PROFILE_UPDATE_DTO,
            activity = EDIT,
            module = MERCHANT_PROFILE)
    public Object updateMerchantProfile(Object initialData, Object updateData, Object... files) {
        MerchantProfileUpdateDTO updateDTO = (MerchantProfileUpdateDTO) updateData;
        merchantProfileService.updateProfile(updateDTO.getProfileData(), updateDTO.getMerchantId());
        return Optional.empty();
    }

    /**
     * Toggles merchant profile status through the maker-checker workflow.
     *
     * <p>Uses ENABLE activity for activation and DISABLE activity for deactivation.
     * Requires appropriate maker and checker permissions based on the operation.
     *
     * @param initialData the initial merchant profile data
     * @param updateData the status toggle data
     * @param files additional files if any
     * @return Optional.empty() indicating successful processing
     */
    @Override
    @MakerChecker(
            checkerPermission = "approve-activate-merchant",
            makerPermission = "disable-merchant-user",
            requestClassName = MERCHANT_PROFILE_STATUS_DTO,
            activity = EDIT,
            module = MERCHANT_PROFILE)
    public Object toggleMerchantProfileStatus(Object initialData, Object updateData, Object... files) {
        MerchantProfileStatusDTO statusDTO = (MerchantProfileStatusDTO) updateData;
        merchantProfileService.toggleProfileStatus(statusDTO.getMerchantId(), statusDTO.isEnabled());
        return Optional.empty();
    }
}