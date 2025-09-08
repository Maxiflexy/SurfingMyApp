/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.approval.validator;

import com.digicore.omnexa.backoffice.modules.merchant.profile.approval.MerchantProfileProxyService;
import com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileUpdateDTO;
import com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileStatusDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Validator service for merchant profile operations before submitting to maker-checker workflow.
 *
 * <p>This service performs validation and business rule checks before delegating operations
 * to the {@link MerchantProfileProxyService} for approval workflow processing.
 *
 * @author Generated
 * @createdOn Aug-12(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileValidatorService {

    private final MerchantProfileProxyService merchantProfileProxyService;

    /**
     * Validates and submits a merchant profile update request for approval.
     *
     * <p>Performs validation on the merchant ID and profile data before
     * submitting to the maker-checker approval workflow.
     *
     * @param merchantId the ID of the merchant profile to update
     * @param profileData the updated profile data
     * @throws OmnexaException if validation fails
     */
    public void updateMerchantProfile(String merchantId, Object profileData) {
        // Validate input parameters
        validateMerchantId(merchantId);
        validateProfileData(profileData);

        // Create DTO for the approval workflow
        MerchantProfileUpdateDTO updateDTO = new MerchantProfileUpdateDTO(merchantId, profileData);

        // Submit to maker-checker workflow
        merchantProfileProxyService.updateMerchantProfile(null, updateDTO);
    }

    /**
     * Validates and submits a merchant profile status toggle request for approval.
     *
     * <p>Performs validation on the merchant ID before submitting to the 
     * maker-checker approval workflow.
     *
     * @param merchantId the ID of the merchant profile to toggle status
     * @param enabled true to enable the profile, false to disable
     * @throws OmnexaException if validation fails
     */
    public void toggleMerchantProfileStatus(String merchantId, boolean enabled) {
        // Validate input parameters
        validateMerchantId(merchantId);

        // Create DTO for the approval workflow
        MerchantProfileStatusDTO statusDTO = new MerchantProfileStatusDTO(merchantId, enabled);

        // Submit to maker-checker workflow
        merchantProfileProxyService.toggleMerchantProfileStatus(null, statusDTO);
    }

    /**
     * Validates that the merchant ID is not null or empty.
     *
     * @param merchantId the merchant ID to validate
     * @throws OmnexaException if the merchant ID is invalid
     */
    private void validateMerchantId(String merchantId) {
        if (RequestUtil.nullOrEmpty(merchantId)) {
            throw new OmnexaException("Merchant ID is required and cannot be empty", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validates that the profile data is not null.
     *
     * @param profileData the profile data to validate
     * @throws OmnexaException if the profile data is invalid
     */
    private void validateProfileData(Object profileData) {
        if (RequestUtil.isNull(profileData)) {
            throw new OmnexaException("Profile data is required and cannot be null", HttpStatus.BAD_REQUEST);
        }
    }
}