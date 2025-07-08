/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.config;

import com.digicore.omnexa.common.lib.properties.DatabasePropertyConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Profile({"dev", "pilot", "prod"})
@Configuration
@ComponentScan({"com.digicore.omnexa.merchant.service.modules"})
@EntityScan({"com.digicore.omnexa.merchant.service.modules"})
@EnableJpaRepositories(basePackages = {"com.digicore.omnexa.merchant.service.modules"})
@RequiredArgsConstructor
@EnableJpaAuditing
public class DataSourceConfig {
  private final DatabasePropertyConfig databasePropertyConfig;

  @Bean
  @Primary
  @Profile({"dev", "pilot", "prod"})
  public DataSource dataSource() {
    return this.getHikariDataSource();
  }

  private HikariDataSource getHikariDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(databasePropertyConfig.getDriver());
    dataSource.setJdbcUrl(databasePropertyConfig.getUrl());
    dataSource.setUsername(databasePropertyConfig.getUsername());
    dataSource.setPassword(databasePropertyConfig.getPassword());

    // Connection pool settings
    dataSource.setConnectionTimeout(30000L); // Max time to wait for a connection
    dataSource.setIdleTimeout(600000L); // Max time a connection can be idle in pool (10 mins)
    dataSource.setMaxLifetime(1800000L); // Max time a connection can live in pool (30 mins)

    // Connection pool size
    dataSource.setMaximumPoolSize(100); // Adjust based on your load
    dataSource.setMinimumIdle(10); // Set minimum idle connections to avoid waiting for connections

    // Enable connection leak detection (optional, for debugging long-running transactions)
    dataSource.setLeakDetectionThreshold(30000); // 30 seconds to detect leaks

    return dataSource;
  }
}
