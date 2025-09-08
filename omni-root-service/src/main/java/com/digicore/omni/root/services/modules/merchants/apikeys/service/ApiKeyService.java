package com.digicore.omni.root.services.modules.merchants.apikeys.service;


import com.digicore.omni.data.lib.modules.merchant.dto.APIKeyDTO;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final MerchantService merchantService;
    private final AuthInformationUtils authInformationUtils;
    private final AuditLogProcessor auditLogProcessor;


    public APIKeyDTO generateApiKey(Principal principal){
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        auditLogProcessor.saveAuditWithDescription(auditLog.getRole(),auditLog.getUser(),auditLog.getEmail(), LogActivityType.CREATE_ACTIVITY,"Generated merchant secret API key");
        return merchantService.generateKeys(principal);
    }

    public void updateApiKeyInfo(APIKeyDTO apiKeyDTO, Principal principal){
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        auditLogProcessor.saveAuditWithDescription(auditLog.getRole(),auditLog.getUser(),auditLog.getEmail(), LogActivityType.CREATE_ACTIVITY,"Updated merchant secret API key");
        merchantService.updateApiKeyInfo(apiKeyDTO, principal);
    }

    public APIKeyDTO fetchUserApiKey(Principal principal){
        return merchantService.getApiKeyForUser(principal);
    }



}
