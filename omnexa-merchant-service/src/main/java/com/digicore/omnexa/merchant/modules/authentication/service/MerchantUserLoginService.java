/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.FAILED;

import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationService;
import com.digicore.omnexa.common.lib.authentication.contract.LoginAttemptService;
import com.digicore.omnexa.common.lib.authentication.dto.LoginAttemptDTO;
import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.merchant.modules.authentication.data.repository.MerchantUserAuthProfileRepository;
import com.digicore.omnexa.merchant.modules.authentication.dto.response.MerchantLoginProfileDTO;
import com.digicore.omnexa.merchant.modules.authentication.helper.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserLoginService implements AuthenticationService, UserDetailsService {
  private final MerchantUserAuthProfileRepository merchantUserAuthProfileRepository;
  private final MessagePropertyConfig messagePropertyConfig;
  private final LoginHelper loginHelper;
  private final PasswordEncoder passwordEncoder;
  private final LoginAttemptService loginAttemptService;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    LoginRequestDTO loginRequestDTO = (LoginRequestDTO) request;
    MerchantLoginProfileDTO userDetails =
        (MerchantLoginProfileDTO) loadUserByUsername(loginRequestDTO.getUsername());
    LoginAttemptDTO loginAttemptDTO =
        LoginAttemptDTO.builder()
            .name(userDetails.getName())
            .role(userDetails.getRole())
            .username(userDetails.getUsername())
            .authenticationType(loginRequestDTO.getAuthenticationType())
            .build();

    loginAttemptService.verifyLoginAccess(
        loginAttemptDTO,
        passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword()));
    return loginHelper.getLoginResponse(loginRequestDTO, userDetails);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return merchantUserAuthProfileRepository
        .findByUsername(username)
        .orElseThrow(
            () ->
                new OmnexaException(
                    messagePropertyConfig.getLoginMessage(FAILED), HttpStatus.UNAUTHORIZED));
  }
}
