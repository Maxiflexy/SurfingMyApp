/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.data.repository;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.model.BackOfficeUserAuthProfile;
import java.util.Optional;
import com.digicore.omnexa.backoffice.modules.user.authentication.dto.response.BackOfficeLoginProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.digicore.omnexa.backoffice.modules.user.authentication.helper.BackOfficeLoginHelper.LOGIN_DETAIL_DTO_CLASS_NAME;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
public interface BackOfficeUserAuthProfileRepository
    extends JpaRepository<BackOfficeUserAuthProfile, Long> {
  boolean existsByUsername(String username);

  Optional<BackOfficeUserAuthProfile> findFirstByUsername(String username);

  /**
   * Finds a back office user authentication profile by username for login purposes.
   *
   * @param username the username to search for
   * @return an Optional containing the BackOfficeLoginProfileDTO if found, empty if not found
   */
  @Query(
          "SELECT new " + LOGIN_DETAIL_DTO_CLASS_NAME + "(" +
                  "       bup.profileId, " +
                  "       bup.role, " +
                  "       bua.username, " +
                  "       bup.firstName, " +
                  "       bup.lastName, " +
                  "       bua.password) " +
                  "FROM BackOfficeUserAuthProfile bua " +
                  "JOIN bua.backOfficeUserProfile bup " +
                  "WHERE bua.username = :username")
  Optional<BackOfficeLoginProfileDTO> findByUsername(@Param("username") String username);
}
