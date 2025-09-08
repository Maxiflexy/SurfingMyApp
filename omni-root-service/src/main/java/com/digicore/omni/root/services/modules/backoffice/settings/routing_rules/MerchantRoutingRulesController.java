package com.digicore.omni.root.services.modules.backoffice.settings.routing_rules;

import com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO;
import com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service.MerchantRoutingRuleService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ibrahim Lawal
 * @createdOn 21/May/2024
 */


@RestController
@RequestMapping("/api/v1/settings/merchant-routing-rules/process/")
@RequiredArgsConstructor
public class MerchantRoutingRulesController {

    private final MerchantRoutingRuleService merchantRoutingRuleService;

    @TokenValid()
    @PatchMapping("update-routing-rule")
    public Object updateRoutingRule(@Valid @RequestBody RoutingRulesDTO routingRulesDTO) {
        return merchantRoutingRuleService.updateMerchantRoutingRule(routingRulesDTO,null);
    }

    @TokenValid()
    @PatchMapping("remove-routing-rule")
    public Object deleteRoutingRule(@RequestBody RoutingRulesDTO routingRulesDTO) {
        return merchantRoutingRuleService.removeMerchantRoutingRule(routingRulesDTO,null);
    }

    @TokenValid()
    @GetMapping("get-routing-rule")
    public ResponseEntity<Object> getRoutingRule(@RequestParam Long id) {
        return CommonUtils.buildSuccessResponse(merchantRoutingRuleService.getRoutingRule(id));
    }

    @TokenValid()
    @GetMapping("get-card-types")
    public ResponseEntity<Object> getAllDocumentType(){
        return merchantRoutingRuleService.getSupportedCardTypes();
    }


    @TokenValid()
    @GetMapping("fetch-all-rules")
    public ResponseEntity<Object> getAllRoutingRules(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,@RequestParam(name = "merchantId")String merchantId) {
        return CommonUtils.buildSuccessResponse(merchantRoutingRuleService.getAllRoutingRules(pageSize,pageNumber,merchantId));
    }
}
