/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.config.helper;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.model.BackOfficeUserAuthProfile;
import com.digicore.omnexa.backoffice.modules.user.authentication.data.repository.BackOfficeUserAuthProfileRepository;
import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.backoffice.modules.user.profile.data.repository.BackOfficeUserProfileRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-31(Thu)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeStartUpHelper {
  private final BackOfficeUserProfileRepository backOfficeUserProfileRepository;
  private final BackOfficeUserAuthProfileRepository backOfficeUserAuthProfileRepository;
  private final PasswordEncoder passwordEncoder;
  private final EntityManager entityManager;

  private void buildUserAuthProfile(BackOfficeUserProfile backOfficeUserProfile, String password) {
    BackOfficeUserAuthProfile profile =
        backOfficeUserAuthProfileRepository
            .findFirstByUsername(backOfficeUserProfile.getEmail())
            .orElse(new BackOfficeUserAuthProfile());

    profile.setUsername(backOfficeUserProfile.getEmail());
    profile.setPassword(passwordEncoder.encode(password));
    profile.setBackOfficeUserProfile(backOfficeUserProfile);

    backOfficeUserAuthProfileRepository.save(profile);
  }
}
