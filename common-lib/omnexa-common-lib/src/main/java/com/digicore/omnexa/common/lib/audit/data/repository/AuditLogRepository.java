/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.audit.data.repository;

import com.digicore.omnexa.common.lib.audit.data.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {}
