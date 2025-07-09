/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.authentication.service;

import com.digicore.common.lib.authentication.contract.request.AuthenticationRequest;
import com.digicore.common.lib.authentication.contract.response.AuthenticationResponse;
import com.digicore.common.lib.authentication.contract.service.AuthenticationService;
import com.digicore.service.modules.authentication.data.repository.MerchantUserAuthProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserLoginService
    implements AuthenticationService, UserDetailsService {
  private final MerchantUserAuthProfileRepository merchantUserAuthProfileRepository;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    return null;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }
}
