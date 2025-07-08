/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * Configuration class for database properties.
 *
 * <p>This class is used to bind database-related properties defined in the application's
 * configuration files (e.g., `application.properties` or `application.yml`) to Java fields. It uses
 * Spring Boot's {@link ConfigurationProperties} annotation to map properties with the prefix
 * `omnexa.database`.
 *
 * <p>Features: - Provides fields for common database configuration parameters such as URL,
 * username, password, driver, maximum active connections, and Hibernate dialect. - Includes default
 * values for some fields. - Uses Lombok annotations {@link Getter} and {@link Setter} to generate
 * getter and setter methods.
 *
 * <p>Usage: - Define database properties in the configuration file with the prefix
 * `omnexa.database`. - Example:
 *
 * <pre>
 *   omnexa.database.url=jdbc:mysql://localhost:3306/mydb
 *   omnexa.database.username=root
 *   omnexa.database.password=secret
 *   omnexa.database.driver=com.mysql.cj.jdbc.Driver
 *   omnexa.database.maxActive=500
 *   omnexa.database.hibernateDialect=org.hibernate.dialect.MySQLDialect
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.database")
@Getter
@Setter
public class DatabasePropertyConfig {

  /** The URL of the database. */
  private String url = "";

  /** The username for database authentication. */
  private String username = "";

  /** The password for database authentication. */
  private String password = "";

  /** The driver class name for the database connection. */
  private String driver = "";

  /** The maximum number of active connections allowed in the connection pool. */
  private String maxActive = "1000";

  /** The Hibernate dialect used for database interaction. */
  private String hibernateDialect = "org.hibernate.dialect.MySQLDialect";
}
