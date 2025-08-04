/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.facade;

import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationService;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade for back office user authentication operations.
 *
 * <p>This facade coordinates authentication operations and follows the facade pattern to provide a
 * simplified interface for authentication workflows.
 *
 * <p>Implements the {@link Facade} interface to integrate with the application's facade resolution
 * system.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeUserAuthenticationFacade
    implements Facade<AuthenticationRequest, AuthenticationResponse> {

  private final AuthenticationService backOfficeUserLoginService;

  public static final String BACKOFFICE_AUTHENTICATION = "backOfficeAuthentication";

  /**
   * Processes an authentication request for back office users.
   *
   * @param request the authentication request containing login credentials
   * @return Optional containing the authentication response
   */
  @Override
  public Optional<AuthenticationResponse> process(AuthenticationRequest request) {
    AuthenticationResponse response = backOfficeUserLoginService.authenticate(request);
    // TODO: Send login notification
    return Optional.of(response);
  }

  /**
   * Returns the type identifier for this facade.
   *
   * @return the facade type identifier
   */
  @Override
  public String getType() {
    return BACKOFFICE_AUTHENTICATION;
  }
}
