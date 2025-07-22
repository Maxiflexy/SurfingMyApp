/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.config;

import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Test configuration for BackOffice user module tests.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-10(Thur)-2026
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public MessagePropertyConfig testMessagePropertyConfig() {
        MessagePropertyConfig config = new MessagePropertyConfig();

        // Set up test messages
        Map<String, String> onboardMessages = new HashMap<>();
        onboardMessages.put("duplicate", "User with profile {profile} already exists");
        onboardMessages.put("invalid", "Invalid onboarding request");
        onboardMessages.put("not_found", "User with profile {profile} not found");
        onboardMessages.put("required", "Field {profile} is required");
        onboardMessages.put("successful", "Onboarding completed successfully");

        Map<String, String> userMessages = new HashMap<>();
        userMessages.put("duplicate", "User {profile} already exists");
        userMessages.put("not_found", "User {profile} not found");

        config.setOnboard(onboardMessages);
        config.setUser(userMessages);

        return config;
    }
}