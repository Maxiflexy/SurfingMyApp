package com.digicore.omni.root.services.modules.backoffice.settings.routing_rules.service;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-22(Thu)-2023
 */

import com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeRoutingRuleService;
import com.digicore.omni.root.services.config.PropertyConfig;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.MakerChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralRoutingRuleService {

 private final BackOfficeRoutingRuleService backOfficeRoutingRuleService;

 private final PropertyConfig propertyConfig;

 public RoutingRulesDTO getRoutingRule(Long id){
  return backOfficeRoutingRuleService.fetchRoutingRulesById(id);
 }
 @MakerChecker(checkerPermission = "approve-or-decline-routing-rule", makerPermission = "update-routing-rule",
         requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO")
 public Object removeGeneralRoutingRule(Object request, Object[] pendingFileDTOs){
  backOfficeRoutingRuleService.deleteRoutingRule((RoutingRulesDTO) request);
  return null;
 }

 @MakerChecker(checkerPermission = "approve-or-decline-routing-rule", makerPermission = "update-routing-rule",
         requestClassName = "com.digicore.omni.data.lib.modules.backoffice.dto.RoutingRulesDTO")
 public Object updateGeneralRoutingRule(Object request, Object[] pendingFileDTOs){
  backOfficeRoutingRuleService.saveRoutingRules((RoutingRulesDTO) request);
  return null;
 }
 public PaginatedResponseApiModel<RoutingRulesDTO> getAllRoutingRules(int size, int page) {
  return backOfficeRoutingRuleService.fetchAllRules(size,page);
 }

 public ResponseEntity<Object> getSupportedCardTypes(){
  return CommonUtils.buildSuccessResponse(propertyConfig.getCardTypes());
 }

}
