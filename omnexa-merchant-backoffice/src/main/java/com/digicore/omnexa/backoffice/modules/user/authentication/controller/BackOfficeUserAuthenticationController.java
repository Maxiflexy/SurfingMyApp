/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.controller;

import static com.digicore.omnexa.backoffice.modules.user.authentication.facade.BackOfficeUserAuthenticationFacade.BACKOFFICE_AUTHENTICATION;
import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.AuthenticationSwaggerDocConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authentication.service.BackOfficeUserAuthProfileService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.facade.resolver.FacadeResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office user authentication operations.
 *
 * <p>Provides endpoints for back office user login and authentication processes. This controller
 * follows the established architecture pattern with proper separation of concerns through facade
 * delegation.
 *
 * <p>Features: - User authentication with JWT token generation - Integration with Spring Security -
 * Comprehensive API documentation with Swagger - Proper error handling and validation
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
@RestController
@RequestMapping(API_V1 + AUTHENTICATION_API)
@RequiredArgsConstructor
@Tag(name = AUTHENTICATION_CONTROLLER_TITLE, description = AUTHENTICATION_CONTROLLER_DESCRIPTION)
public class BackOfficeUserAuthenticationController {

  private final FacadeResolver facadeResolver;
  private final BackOfficeUserAuthProfileService backOfficeUserAuthProfileService;

  /**
   * Authenticates a back office user with the provided credentials.
   *
   * @param loginRequestDTO the login request containing username and password
   * @param httpServletRequest the HTTP servlet request
   * @return ResponseEntity containing the authentication response with JWT token
   */
  @PostMapping()
  @Operation(
      summary = AUTHENTICATION_CONTROLLER_LOGIN_TITLE,
      description = AUTHENTICATION_CONTROLLER_LOGIN_DESCRIPTION)
  public ResponseEntity<Object> login(
      @Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {

    Facade<AuthenticationRequest, AuthenticationResponse> facade =
        facadeResolver.resolve(BACKOFFICE_AUTHENTICATION);
    Optional<AuthenticationResponse> response = facade.process(loginRequestDTO);
    return ControllerResponse.buildSuccessResponse(response, "Authentication Successful");
  }


  @PostMapping(FORGOT_PASSWORD_INIT_API)
  @Operation(
          summary = FORGOT_PASSWORD_INIT_CONTROLLER_TITLE,
          description = FORGOT_PASSWORD_INIT_CONTROLLER_DESCRIPTION)
  public ResponseEntity<Object> forgotPassword(
          @RequestParam String username, HttpServletRequest httpServletRequest) {

    backOfficeUserAuthProfileService.initResetPassword(username);
    return ControllerResponse.buildSuccessResponse(
            "Success, a passcode will be sent to your email.");
  }
}
