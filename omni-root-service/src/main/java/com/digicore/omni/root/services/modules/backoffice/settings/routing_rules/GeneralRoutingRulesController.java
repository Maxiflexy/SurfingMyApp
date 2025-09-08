package com.digicore.omni.root.services.modules.backoffice.settings.routing_rules;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-20(Tue)-2023
 */


import com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO;
import com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service.GeneralRoutingRuleService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings/routing-rules/process/")
@RequiredArgsConstructor
public class GeneralRoutingRulesController {

    private final GeneralRoutingRuleService generalRoutingRuleService;

    @TokenValid()
    @PatchMapping("update-routing-rule")
    public Object updateRoutingRule(@Valid @RequestBody RoutingRulesDTO routingRulesDTO) {
        return generalRoutingRuleService.updateGeneralRoutingRule(routingRulesDTO,null);
    }

    @TokenValid()
    @PatchMapping("remove-routing-rule")
    public Object deleteRoutingRule(@RequestBody RoutingRulesDTO routingRulesDTO) {
        return generalRoutingRuleService.removeGeneralRoutingRule(routingRulesDTO,null);
    }

    @TokenValid()
    @GetMapping("get-routing-rule")
    public ResponseEntity<Object> getRoutingRule(@RequestParam Long id) {
        return CommonUtils.buildSuccessResponse(generalRoutingRuleService.getRoutingRule(id));
    }

    @TokenValid()
    @GetMapping("get-card-types")
    public ResponseEntity<Object> getAllDocumentType(){
        return generalRoutingRuleService.getSupportedCardTypes();
    }


    @TokenValid()
    @GetMapping("fetch-all-rules")
    public ResponseEntity<Object> getAllRoutingRules(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        return CommonUtils.buildSuccessResponse(generalRoutingRuleService.getAllRoutingRules(pageSize,pageNumber));
    }
}
