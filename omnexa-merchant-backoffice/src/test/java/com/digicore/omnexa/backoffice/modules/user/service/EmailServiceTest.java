/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for EmailService.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@DisplayName("EmailService Tests")
class EmailServiceTest {

    private EmailService emailService;
    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();

        // Setup log capturing for testing
        logger = (Logger) LoggerFactory.getLogger(EmailService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    @DisplayName("Should log invitation email sending")
    void shouldLogInvitationEmailSending() {
        // Given
        String email = "test@example.com";
        String firstName = "John";

        // When
        emailService.sendInvitationEmail(email, firstName);

        // Then
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage())
                .contains("DUMMY EMAIL SERVICE")
                .contains("Sending invitation email")
                .contains(email)
                .contains(firstName);
    }

    @Test
    @DisplayName("Should log welcome email sending")
    void shouldLogWelcomeEmailSending() {
        // Given
        String email = "test@example.com";
        String fullName = "John Doe";

        // When
        emailService.sendWelcomeEmail(email, fullName);

        // Then
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage())
                .contains("DUMMY EMAIL SERVICE")
                .contains("Sending welcome email")
                .contains(email)
                .contains(fullName);
    }

    @Test
    @DisplayName("Should handle null parameters gracefully in invitation email")
    void shouldHandleNullParametersGracefullyInInvitationEmail() {
        // When
        emailService.sendInvitationEmail(null, null);

        // Then
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage()).contains("DUMMY EMAIL SERVICE");
    }

    @Test
    @DisplayName("Should handle null parameters gracefully in welcome email")
    void shouldHandleNullParametersGracefullyInWelcomeEmail() {
        // When
        emailService.sendWelcomeEmail(null, null);

        // Then
        assertThat(listAppender.list).hasSize(1);
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage()).contains("DUMMY EMAIL SERVICE");
    }
}