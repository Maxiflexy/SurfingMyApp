/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.profile.data.repository;

import com.digicore.service.modules.profile.data.model.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing merchant profile data.
 *
 * <p>This interface extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations and
 * custom query methods for the {@link MerchantProfile} entity.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {

  /**
   * Checks if a merchant profile exists by business name or business email.
   *
   * @param businessName the business name to check.
   * @param businessEmail the business email to check.
   * @return {@code true} if a merchant profile exists with the given business name or email, {@code
   *     false} otherwise.
   */
  boolean existsByBusinessNameOrBusinessEmail(String businessName, String businessEmail);
}
