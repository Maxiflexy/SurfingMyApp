/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.data.repository;

import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantApiKeyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
public interface MerchantApiKeyProfileRepository
    extends JpaRepository<MerchantApiKeyProfile, Long> {}
