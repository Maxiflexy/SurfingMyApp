/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.user.data.repository;

import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for BackOfficeUser entity operations.
 *
 * <p>Provides database operations for back office users including
 * finding users by email and checking existence.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
public interface BackOfficeUserRepository extends JpaRepository<BackOfficeUser, Long> {

    /**
     * Finds a back office user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    Optional<BackOfficeUser> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds a back office user by userId.
     *
     * @param userId the userId to search for
     * @return an Optional containing the user if found
     */
    Optional<BackOfficeUser> findByUserId(String userId);
}