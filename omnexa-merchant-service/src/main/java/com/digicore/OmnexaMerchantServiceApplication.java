/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main entry point for the Omnexa Merchant Service application.
 *
 * <p>This class initializes and runs the Spring Boot application. It excludes certain
 * auto-configuration classes to customize the application behavior.
 *
 * <p>Features: - Enables Feign clients for declarative REST client functionality. - Excludes
 * `UserDetailsServiceAutoConfiguration` and `DataSourceAutoConfiguration` to disable default
 * security and database configurations.
 *
 * <p>Usage: - Run this class to start the application. - Example:
 *
 * <pre>
 *   java -jar OmnexaMerchantServiceApplication.jar
 *   </pre>
 *
 * @author Digicore Limited
 * @createdOn Jul-01(Tue)-2025
 */
@EnableFeignClients
@SpringBootApplication(
    exclude = {UserDetailsServiceAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class OmnexaMerchantServiceApplication {

  /**
   * Main method to launch the Spring Boot application.
   *
   * @param args command-line arguments passed to the application.
   */
  public static void main(String[] args) {
    SpringApplication.run(OmnexaMerchantServiceApplication.class, args);
  }
}
