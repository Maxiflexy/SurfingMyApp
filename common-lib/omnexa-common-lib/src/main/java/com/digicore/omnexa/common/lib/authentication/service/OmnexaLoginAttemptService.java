/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.*;
import static com.digicore.omnexa.common.lib.constant.module.ModuleConstant.AUTHENTICATION;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.common.lib.audit.contract.AuditLogService;
import com.digicore.omnexa.common.lib.authentication.contract.LoginAttemptService;
import com.digicore.omnexa.common.lib.authentication.data.model.LoginAttempt;
import com.digicore.omnexa.common.lib.authentication.data.repository.LoginAttemptRepository;
import com.digicore.omnexa.common.lib.authentication.dto.LoginAttemptDTO;
import com.digicore.omnexa.common.lib.authentication.helper.DeviceDetectionHelper;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.security.SecurityPropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-23(Thu)-2025
 */
@EntityScan("com.digicore.omnexa.common.lib.authentication.data.model")
@EnableJpaRepositories(
    basePackages = {"com.digicore.omnexa.common.lib.authentication.data.repository"})
@Service
@RequiredArgsConstructor
public class OmnexaLoginAttemptService implements LoginAttemptService {
  private final LoginAttemptRepository loginAttemptRepository;
  private final MessagePropertyConfig messagePropertyConfig;
  private final SecurityPropertyConfig securityPropertyConfig;
  private final AuditLogService auditLogService;
  private final HttpServletRequest httpServletRequest;
  private final DeviceDetectionHelper deviceDetectionHelper;

  private static final String ACTIVITY_TYPE = "LOGIN";

  private void systemLockUser(LoginAttempt loginAttempt) {
    loginAttempt.setLoginLocked(true);
    loginAttempt.setAutomatedUnlockTime(null);
    this.save(loginAttempt);
  }

  private boolean shouldWarnUser(LoginAttempt loginAttempt) {
    return loginAttempt.getFailedLoginAttempts()
            >= securityPropertyConfig.getLoginAttemptLimitBeforeWarning()
        && !loginAttempt.isLoginLocked();
  }

  private LoginAttempt getOrCreateByUsername(LoginAttemptDTO loginAttemptDTO) {
    return this.findByUsername(loginAttemptDTO.getUsername())
        .orElseGet(
            () -> {
              LoginAttempt loginAttempt = new LoginAttempt();
              loginAttempt.setFailedLoginAttempts(0);
              loginAttempt.setLoginLocked(false);
              loginAttempt.setAutomatedUnlockTime(null);
              loginAttempt.setAuthenticationType(loginAttemptDTO.getAuthenticationType());
              loginAttempt.setDeviceInfo(deviceDetectionHelper.getDeviceInfo(httpServletRequest));
              loginAttempt.setLastLoginIp(loginAttemptDTO.getLastLoginIp());
              loginAttempt.setUsername(loginAttemptDTO.getUsername());
              loginAttempt.setName(loginAttemptDTO.getName());
              return this.save(loginAttempt);
            });
  }

  private LoginAttempt save(LoginAttempt userLoginAttempt) {
    return this.loginAttemptRepository.save(userLoginAttempt);
  }

  private Optional<LoginAttempt> findByUsername(String username) {
    return this.loginAttemptRepository.findFirstByUsernameOrderByCreatedDate(username);
  }

  /**
   * Unlock user.
   *
   * @param loginAttemptDTO the loginAttemptDTO
   */
  public void unlockUser(LoginAttemptDTO loginAttemptDTO) {
    LoginAttempt userLoginAttempt = this.getOrCreateByUsername(loginAttemptDTO);
    this.unlock(userLoginAttempt);
  }

  private void unlock(LoginAttempt userLoginAttempt) {
    if (userLoginAttempt.isLoginLocked()) {
      userLoginAttempt.setFailedLoginAttempts(0);
      userLoginAttempt.setLoginLocked(false);
      userLoginAttempt.setAutomatedUnlockTime(null);
      this.save(userLoginAttempt);
    }
  }

  @Override
  public void verifyLoginAccess(LoginAttemptDTO loginAttemptDTO, boolean credentialMatches) {
    loginAttemptDTO.setLastLoginIp(RequestUtil.getIpAddress(httpServletRequest));
    LoginAttempt loginAttempt = this.getOrCreateByUsername(loginAttemptDTO);

    if (!credentialMatches) {
      if (loginAttempt.isLoginLocked()) {
        auditLogService.log(
            loginAttemptDTO.getRole(),
            loginAttemptDTO.getUsername(),
            loginAttemptDTO.getName(),
            ACTIVITY_TYPE,
            AUTHENTICATION,
            messagePropertyConfig
                .getLoginMessage(LOCKED, SYSTEM_DEFAULT_LOCKED_ERROR)
                .replace(USER, loginAttemptDTO.getName()));
        throw new OmnexaException(
            messagePropertyConfig.getLoginMessage(LOCKED, SYSTEM_DEFAULT_LOCKED_ERROR),
            HttpStatus.UNAUTHORIZED);
      }

      loginAttempt.setFailedLoginAttempts(loginAttempt.getFailedLoginAttempts() + 1);
      this.save(loginAttempt);
      if (loginAttempt.getFailedLoginAttempts()
          >= securityPropertyConfig.getLoginAttemptMaxCount()) {
        this.systemLockUser(loginAttempt);
        auditLogService.log(
            loginAttemptDTO.getRole(),
            loginAttemptDTO.getUsername(),
            loginAttemptDTO.getName(),
            ACTIVITY_TYPE,
            AUTHENTICATION,
            messagePropertyConfig.getLoginMessage(LOCKED, SYSTEM_DEFAULT_LOCKED_ERROR));
        throw new OmnexaException(
            messagePropertyConfig.getLoginMessage(LOCKED, SYSTEM_DEFAULT_LOCKED_ERROR),
            HttpStatus.UNAUTHORIZED);
      } else {
        auditLogService.log(
            loginAttemptDTO.getRole(),
            loginAttemptDTO.getUsername(),
            loginAttemptDTO.getName(),
            ACTIVITY_TYPE,
            AUTHENTICATION,
            messagePropertyConfig
                .getLoginMessage(DENIED, SYSTEM_DEFAULT_DENIED_ERROR)
                .replace(TIME, String.valueOf(loginAttempt.getFailedLoginAttempts()))
                .replace(USER, loginAttemptDTO.getName()));

        if (shouldWarnUser(loginAttempt)) {
          int remainingAttempts =
              securityPropertyConfig.getLoginAttemptMaxCount()
                  - loginAttempt.getFailedLoginAttempts();
          throw new OmnexaException(
              messagePropertyConfig
                  .getLoginMessage(WARNING, SYSTEM_DEFAULT_WARNING_ERROR)
                  .replace(COUNT, String.valueOf(remainingAttempts)),
              HttpStatus.UNAUTHORIZED);
        }
      }
    } else {
      if (loginAttempt.isLoginLocked() && shouldNotAutomaticallyUnlockProfile(loginAttempt)) {
        throw new OmnexaException(
            messagePropertyConfig.getLoginMessage(LOCKED, SYSTEM_DEFAULT_LOCKED_ERROR),
            HttpStatus.FORBIDDEN);
      }
      loginAttempt.setLastLoginAt(ZonedDateTime.now());
      save(loginAttempt);
      this.unlock(loginAttempt);
    }
  }

  private boolean shouldNotAutomaticallyUnlockProfile(LoginAttempt loginAttempt) {
    ZonedDateTime unlockTime = loginAttempt.getAutomatedUnlockTime();
    if (loginAttempt.getAutomatedUnlockTime() == null) {
      return false;
    }
    return !ZonedDateTime.now().isAfter(unlockTime);
  }
}
