package com.digicore.omni.root.services.modules.backoffice.settings.nqr.controller;

import com.digicore.omni.payment.common.lib.modules.qr.nqr.request.MerchantCreationRequest;
import com.digicore.omni.root.services.modules.backoffice.settings.nqr.service.NQRService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-04(Sun)-2023
 */

@RestController
@RequestMapping("/api/v1/backoffice-nqr/process/")
@RequiredArgsConstructor
public class NQRController {
    private final NQRService nqrService;

    @PostMapping("create-merchant")
    @TokenValid()
    public Object createMerchant(@Valid @RequestBody MerchantCreationRequest merchantCreationRequest) {
        return CommonUtils.buildSuccessResponse(nqrService.createMerchant(merchantCreationRequest));

    }

}
