/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.data.repository;

import com.digicore.omnexa.common.lib.authentication.data.model.LoginAttempt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-24(Fri)-2025
 */

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
  Optional<LoginAttempt> findFirstByUsernameOrderByCreatedDate(String username);
}
