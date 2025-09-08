package com.digicore.omni.root.services.modules.backoffice.merchant_management.service;


import com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration;
import com.digicore.omni.data.lib.modules.common.dtos.compliance.BackOfficeMerchantComplianceRequest;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantProfileEditDTO;
import com.digicore.omni.data.lib.modules.merchant.service.MerchantFeeConfigurationService;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantManagementService;


import com.digicore.request.processor.annotations.MakerChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-12(Mon)-2022
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantManagementComplianceService {

    private final BackOfficeMerchantManagementService merchantManagementService;

    private final MerchantFeeConfigurationService merchantFeeConfigurationService;

    @MakerChecker(checkerPermission = "approve-merchant-compliance",makerPermission = "review-merchant-compliance",requestClassName = "com.digicore.omni.root.lib.modules.common.dtos.compliance.BackOfficeMerchantComplianceRequest")
    public Object approveMerchantCompliance(Object request, Object[] pendingFileDTOs)  {
            return  merchantManagementService.approveMerchantCompliance((BackOfficeMerchantComplianceRequest) request);
    }

  //  @MakerChecker(checkerPermission = "approve-merchant-compliance",makerPermission = "review-merchant-compliance", hasFile = "FALSE",requestClassName = "com.digicore.omni.root.lib.modules.common.dtos.compliance.BackOfficeMerchantComplianceRequest")
    public MerchantProfileApiModel declineMerchantCompliance(String merchantId)  {
        return  merchantManagementService.declineMerchantCompliance(merchantId);
    }

    @MakerChecker(checkerPermission = "approve-edited-merchant-info", makerPermission = "edit-merchant-info", requestClassName = "com.digicore.omni.root.lib.modules.merchant.dto.MerchantProfileEditDTO")
    public Object editMerchantProfile(Object request, Object[] pendingFileDTOs) {
        return merchantManagementService.editMerchantProfile((MerchantProfileEditDTO) request);
    }

    @MakerChecker(checkerPermission = "approve-or-decline-fee-config-update", makerPermission = "update-fee-configuration", requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration")
    public void updateMerchantFeeConfiguration(Object request, Object[] pendingFileDTOs) {
        merchantFeeConfigurationService.updateMerchantFeeConfiguration((BaseWebFeeConfiguration) request);
    }
}
