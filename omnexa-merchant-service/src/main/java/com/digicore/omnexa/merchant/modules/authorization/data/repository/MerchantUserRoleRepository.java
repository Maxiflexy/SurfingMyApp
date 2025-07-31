/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.data.repository;

import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface MerchantUserRoleRepository extends JpaRepository<MerchantUserRole, Long> {
  boolean existsByNameAndDeletedAndMerchantProfileMerchantId(
      String name, boolean deleted, String merchantId);

  Optional<MerchantUserRole> findFirstByName(String name);

  Optional<MerchantUserRole> findFirstByNameAndMerchantProfileMerchantId(
      String name, String merchantId);
}
