package com.digicore.omni.root.services.modules.merchants.apikeys.controller;

import com.digicore.omni.data.lib.modules.merchant.dto.APIKeyDTO;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.apikeys.service.ApiKeyService;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/merchant-api-key/process/")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }


    //@LogActivity(activity = LogActivityType.CREATE_ACTIVITY)
    @TokenValid()
    @GetMapping("generate-api-keys")
    public ResponseEntity<Object> generateMerchantApiKey(Principal principal){
        return CommonUtils.buildSuccessResponse(apiKeyService.generateApiKey(principal));
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PatchMapping ("update-api-key-info")
    public ResponseEntity<Object> updateMerchantApiInfo(@RequestBody APIKeyDTO apiKeyDTO, Principal principal){
        apiKeyService.updateApiKeyInfo(apiKeyDTO,principal);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @GetMapping("get-user-api-key")
    public ResponseEntity<Object> getUserApiKey(Principal principal){
        return CommonUtils.buildSuccessResponse(apiKeyService.fetchUserApiKey(principal));
    }
}
