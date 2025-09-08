package com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.processor;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-22(Thu)-2023
 */

import com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service.GeneralRoutingRuleService;
import com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service.MerchantRoutingRuleService;
import com.digicore.request.processor.annotations.RequestHandler;
import com.digicore.request.processor.annotations.RequestType;
import com.digicore.request.processor.enums.RequestHandlerType;
import lombok.RequiredArgsConstructor;

@RequestHandler(type = RequestHandlerType.ROUTING_RULE_MANAGEMENT)
@RequiredArgsConstructor
public class BackOfficeRoutingRuleProcessor {

    private final GeneralRoutingRuleService generalRoutingRuleService;

    private final MerchantRoutingRuleService merchantRoutingRuleService;


    @RequestType(name = "updateGeneralRoutingRule")
    public Object updateGeneralRoutingRule(Object request){
        return generalRoutingRuleService.updateGeneralRoutingRule(request,null);
    }

    @RequestType(name = "removeGeneralRoutingRule")
    public Object removeGeneralRoutingRule(Object request){
        return generalRoutingRuleService.removeGeneralRoutingRule(request,null);
    }

    @RequestType(name = "updateMerchantRoutingRule")
    public Object updateMerchantRoutingRule(Object request){
        return merchantRoutingRuleService.updateMerchantRoutingRule(request,null);
    }

    @RequestType(name = "removeMerchantRoutingRule")
    public Object removeMerchantRoutingRule(Object request){
        return merchantRoutingRuleService.removeMerchantRoutingRule(request,null);
    }
}
