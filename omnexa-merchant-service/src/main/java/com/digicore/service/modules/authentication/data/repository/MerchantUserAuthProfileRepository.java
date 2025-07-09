/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.authentication.data.repository;

import com.digicore.service.modules.authentication.data.model.MerchantUserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
public interface MerchantUserAuthProfileRepository
    extends JpaRepository<MerchantUserAuthProfile, Long> {
  boolean existsByUsername(String username);
}
