/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.approval.processor;

import com.digicore.omnexa.backoffice.modules.merchant.profile.approval.MerchantProfileProxyService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestHandler;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestType;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/**
 * Processor for handling approved merchant profile requests in the maker-checker workflow.
 *
 * <p>This class processes approved requests and executes the actual business operations
 * for merchant profile management.
 *
 * @author Generated
 * @createdOn Aug-12(Tue)-2025
 */
@RequestHandler(type = RequestHandlerType.PROCESS_MAKER_REQUESTS)
@RequiredArgsConstructor
public class MerchantProfileProcessor {

    private final MerchantProfileProxyService merchantProfileOperations;

    /**
     * Processes approved merchant profile update requests.
     *
     * <p>This method is called by the approval workflow system when a merchant profile
     * update request has been approved by a checker.
     *
     * @param approvalDecisionDTO the approval decision containing the update data
     * @return the result of the update operation
     */
    @RequestType(name = "updateMerchantProfile")
    public Object updateMerchantProfile(Object approvalDecisionDTO) {
        return merchantProfileOperations.updateMerchantProfile(null, approvalDecisionDTO);
    }

    /**
     * Processes approved merchant profile status toggle requests.
     *
     * <p>This method is called by the approval workflow system when a merchant profile
     * status toggle request has been approved by a checker.
     *
     * @param approvalDecisionDTO the approval decision containing the status toggle data
     * @return the result of the status toggle operation
     */
    @RequestType(name = "toggleMerchantProfileStatus")
    public Object toggleMerchantProfileStatus(Object approvalDecisionDTO) {
        return merchantProfileOperations.toggleMerchantProfileStatus(null, approvalDecisionDTO);
    }
}