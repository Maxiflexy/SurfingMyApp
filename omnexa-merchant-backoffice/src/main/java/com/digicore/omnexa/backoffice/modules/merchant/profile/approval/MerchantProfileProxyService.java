/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.approval;

/**
 * Proxy service interface for merchant profile operations requiring maker-checker approval.
 *
 * @author Generated
 * @createdOn Aug-12(Tue)-2025
 */
public interface MerchantProfileProxyService {

    /**
     * Updates a merchant profile through the maker-checker workflow.
     *
     * @param initialData the initial data (current profile state)
     * @param updateData the update data containing changes
     * @param files additional files if any
     * @return the result of the operation
     */
    Object updateMerchantProfile(Object initialData, Object updateData, Object... files);

    /**
     * Toggles merchant profile status through the maker-checker workflow.
     *
     * @param initialData the initial data (current profile state)
     * @param updateData the status change data
     * @param files additional files if any
     * @return the result of the operation
     */
    Object toggleMerchantProfileStatus(Object initialData, Object updateData, Object... files);
}