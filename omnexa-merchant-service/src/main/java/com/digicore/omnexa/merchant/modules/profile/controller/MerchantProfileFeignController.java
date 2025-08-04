package com.digicore.omnexa.merchant.modules.profile.controller;


import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.UserSwaggerDoc.*;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */

@Hidden
@RestController
@RequestMapping(API_V1 + MERCHANT_API)
@RequiredArgsConstructor
public class MerchantProfileFeignController {

    private final ProfileService merchantProfileFeignService;

    @GetMapping()
    @PreAuthorize("hasAuthority('view-merchant-profile')")
    public ResponseEntity<Object> getAllMerchantProfile(
            @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
            @RequestParam(required = false)
            Integer pageNumber,
            @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
            @RequestParam(required = false)
            Integer pageSize) {

        return ControllerResponse.buildCreateSuccessResponse(
                merchantProfileFeignService.getAllProfiles(pageNumber, pageSize));
    }


}
