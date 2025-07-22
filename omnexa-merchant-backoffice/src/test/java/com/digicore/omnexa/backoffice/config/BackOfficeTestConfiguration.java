/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.digicore.omnexa.common.lib.properties.DatabasePropertyConfig;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * * Test configuration for BackOffice integration tests. * * Sets up necessary beans and mocks for
 * testing environment.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-12(Sat)-2025
 */
@TestConfiguration
@Profile("test")
public class BackOfficeTestConfiguration {

  /** Mock DatabasePropertyConfig for tests. */
  @Bean
  @Primary
  public DatabasePropertyConfig databasePropertyConfig() {
    DatabasePropertyConfig config = mock(DatabasePropertyConfig.class);
    when(config.getDriver()).thenReturn("org.h2.Driver");
    when(config.getUrl()).thenReturn("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    when(config.getUsername()).thenReturn("sa");
    when(config.getPassword()).thenReturn("password");
    return config;
  }

  /** Mock MessagePropertyConfig for tests. */
  @Bean
  @Primary
  public MessagePropertyConfig messagePropertyConfig() {
    MessagePropertyConfig config = mock(MessagePropertyConfig.class);

    // Mock common messages
    when(config.getOnboardMessage("DUPLICATE"))
        .thenReturn("User with email {PROFILE} already exists");
    when(config.getOnboardMessage("NOT_FOUND")).thenReturn("User with email {PROFILE} not found");
    when(config.getOnboardMessage("INVALID")).thenReturn("Invalid request data");

    return config;
  }

  /** Password encoder for tests. */
  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
