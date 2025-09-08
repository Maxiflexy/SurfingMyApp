package com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service;

import com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantRoutingRuleService;
import com.digicore.omni.root.services.config.PropertyConfig;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.MakerChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Ibrahim Lawal
 * @createdOn 21/May/2024
 */

@Service
@RequiredArgsConstructor
public class MerchantRoutingRuleService {

    private final BackOfficeMerchantRoutingRuleService backOfficeMerchantRoutingRuleService;

    private final PropertyConfig propertyConfig;

    public RoutingRulesDTO getRoutingRule(Long id){
        return backOfficeMerchantRoutingRuleService.fetchRoutingRulesById(id);
    }
    @MakerChecker(checkerPermission = "approve-or-decline-routing-rule", makerPermission = "update-routing-rule",
            requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO")
    public Object removeMerchantRoutingRule(Object request, Object[] pendingFileDTOs){
        backOfficeMerchantRoutingRuleService.deleteRoutingRule((RoutingRulesDTO) request);
        return null;
    }

    @MakerChecker(checkerPermission = "approve-or-decline-routing-rule", makerPermission = "update-routing-rule",
            requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO")
    public Object updateMerchantRoutingRule(Object request, Object[] pendingFileDTOs){
        backOfficeMerchantRoutingRuleService.saveRoutingRules((RoutingRulesDTO) request);
        return null;
    }
    public PaginatedResponseApiModel<RoutingRulesDTO> getAllRoutingRules(int size, int page, String merchantId) {
        return backOfficeMerchantRoutingRuleService.fetchAllRules(size,page,merchantId);
    }

    public ResponseEntity<Object> getSupportedCardTypes(){
        return CommonUtils.buildSuccessResponse(propertyConfig.getCardTypes());
    }

}
