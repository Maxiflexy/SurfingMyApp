/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.data.repository;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface BackOfficeUserRoleRepository extends JpaRepository<BackOfficeUserRole, Long> {

    /**
     * Finds the first back office user role by name.
     *
     * @param name the name of the role to search for
     * @return an Optional containing the BackOfficeUserRole if found, empty if not found
     */
    Optional<BackOfficeUserRole> findFirstByName(String name);
}
