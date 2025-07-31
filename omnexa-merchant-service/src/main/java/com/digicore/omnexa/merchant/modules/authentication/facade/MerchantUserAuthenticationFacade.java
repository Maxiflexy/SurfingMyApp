/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.facade;

import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationService;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-30(Wed)-2025
 */
@Component
@RequiredArgsConstructor
public class MerchantUserAuthenticationFacade
    implements Facade<AuthenticationRequest, AuthenticationResponse> {
  private final AuthenticationService merchantUserLoginService;
  public static final String MERCHANT_AUTHENTICATION = "merchantAuthentication";

  @Override
  public Optional<AuthenticationResponse> process(AuthenticationRequest request) {
    AuthenticationResponse response = merchantUserLoginService.authenticate(request);
    // todo send login notification
    return Optional.of(response);
  }

  @Override
  public String getType() {
    return MERCHANT_AUTHENTICATION;
  }
}
