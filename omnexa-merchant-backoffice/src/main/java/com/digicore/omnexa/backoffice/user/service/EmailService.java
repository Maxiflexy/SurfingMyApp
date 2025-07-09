/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails to back office users.
 *
 * <p>This is a dummy implementation that logs email actions
 * without actually sending emails.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Service
@Slf4j
public class EmailService {

    /**
     * Sends an invitation email to a new user.
     *
     * @param email recipient email address
     * @param firstName recipient first name for personalization
     */
    public void sendInvitationEmail(String email, String firstName) {
        log.info("DUMMY EMAIL SERVICE: Sending invitation email to {} ({})", email, firstName);
        // TODO: Implement actual email sending logic
    }

    /**
     * Sends a welcome email after successful signup.
     *
     * @param email recipient email address
     * @param fullName recipient full name
     */
    public void sendWelcomeEmail(String email, String fullName) {
        log.info("DUMMY EMAIL SERVICE: Sending welcome email to {} ({})", email, fullName);
        // TODO: Implement actual email sending logic
    }
}