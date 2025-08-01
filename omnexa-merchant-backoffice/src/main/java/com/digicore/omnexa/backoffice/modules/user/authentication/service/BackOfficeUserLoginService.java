/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.service;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.repository.BackOfficeUserAuthProfileRepository;
import com.digicore.omnexa.backoffice.modules.user.authentication.dto.response.BackOfficeLoginProfileDTO;
import com.digicore.omnexa.backoffice.modules.user.authentication.helper.BackOfficeLoginHelper;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationService;
import com.digicore.omnexa.common.lib.authentication.contract.LoginAttemptService;
import com.digicore.omnexa.common.lib.authentication.dto.LoginAttemptDTO;
import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.DENIED;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.FAILED;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_DENIED_ERROR;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
@Service
@RequiredArgsConstructor
public class BackOfficeUserLoginService implements AuthenticationService, UserDetailsService {
  private final BackOfficeUserAuthProfileRepository backOfficeUserAuthProfileRepository;
  private final LoginAttemptService loginAttemptService;
  private final PasswordEncoder passwordEncoder;
  private final MessagePropertyConfig messagePropertyConfig;
  private final BackOfficeLoginHelper loginHelper;

  /**
   * Authenticates a back office user with the provided credentials.
   *
   * @param request the authentication request containing login credentials
   * @return AuthenticationResponse containing JWT token and user information
   * @throws OmnexaException if authentication fails
   */
  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    LoginRequestDTO loginRequestDTO = (LoginRequestDTO) request;
    BackOfficeLoginProfileDTO userDetails =
            (BackOfficeLoginProfileDTO) loadUserByUsername(loginRequestDTO.getUsername());

    LoginAttemptDTO loginAttemptDTO = LoginAttemptDTO.builder()
            .name(userDetails.getFirstName())
            .role(userDetails.getRole())
            .username(userDetails.getUsername())
            .authenticationType(loginRequestDTO.getAuthenticationType())
            .build();

    // Verify login access and password match
    loginAttemptService.verifyLoginAccess(
            loginAttemptDTO,
            passwordEncoder.matches(loginRequestDTO.getPassword(), userDetails.getPassword()));

    return loginHelper.getLoginResponse(loginRequestDTO, userDetails);
  }

  /**
   * Loads user details by username for Spring Security integration.
   *
   * @param username the username to search for
   * @return UserDetails implementation containing user information
   * @throws UsernameNotFoundException if user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return backOfficeUserAuthProfileRepository
            .findByUsername(username)
            .orElseThrow(() -> new OmnexaException(
                    messagePropertyConfig.getLoginMessage(DENIED, SYSTEM_DEFAULT_DENIED_ERROR),
                    HttpStatus.UNAUTHORIZED));
  }
}
