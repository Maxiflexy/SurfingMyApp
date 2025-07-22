/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Test application that excludes Spring Cloud Config client and Security. Use this for integration
 * tests instead of the main application.
 */
@SpringBootApplication
@EnableAutoConfiguration(
    exclude = {
      // Your existing exclusions
      org.springframework.cloud.config.client.ConfigClientAutoConfiguration.class,
      org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
      org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
          .class,

      // ADD THESE NEW EXCLUSIONS - This fixes your main error
      org.springframework.boot.actuate.autoconfigure.security.servlet
          .ManagementWebSecurityAutoConfiguration.class,
      org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration.class,
      org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration
          .class
    })
@ComponentScan(
    basePackages = {
      "com.digicore.omnexa.backoffice", // Include all backoffice components (controllers, services,
      // repos)
      "com.digicore.config" // Include any test configurations if needed
    })
public class TestApplication {
  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }
}
