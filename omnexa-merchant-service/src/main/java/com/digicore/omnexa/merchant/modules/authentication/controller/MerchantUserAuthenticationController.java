/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.authentication.AuthenticationSwaggerDocConstant.*;
import static com.digicore.omnexa.merchant.modules.authentication.facade.MerchantUserAuthenticationFacade.MERCHANT_AUTHENTICATION;

import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-30(Wed)-2025
 */
@RestController
@RequestMapping(API_V1 + AUTHENTICATION_API)
@RequiredArgsConstructor
@Tag(name = AUTHENTICATION_CONTROLLER_TITLE, description = AUTHENTICATION_CONTROLLER_DESCRIPTION)
public class MerchantUserAuthenticationController {
  private final FacadeResolver facadeResolver;

  @PostMapping()
  @Operation(
      summary = AUTHENTICATION_CONTROLLER_LOGIN_TITLE,
      description = AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION)
  public ResponseEntity<Object> login(
      @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {
    Facade<AuthenticationRequest, AuthenticationResponse> facade =
        facadeResolver.resolve(MERCHANT_AUTHENTICATION);
    Optional<AuthenticationResponse> response = facade.process(loginRequestDTO);
    return ControllerResponse.buildSuccessResponse(response, "Authentication Successful");
  }
}
