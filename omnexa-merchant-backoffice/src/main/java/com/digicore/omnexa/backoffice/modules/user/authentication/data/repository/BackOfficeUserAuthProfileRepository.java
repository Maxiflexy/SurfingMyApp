/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.data.repository;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.model.BackOfficeUserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
public interface BackOfficeUserAuthProfileRepository
    extends JpaRepository<BackOfficeUserAuthProfile, Long> {
  boolean existsByUsername(String username);
}
