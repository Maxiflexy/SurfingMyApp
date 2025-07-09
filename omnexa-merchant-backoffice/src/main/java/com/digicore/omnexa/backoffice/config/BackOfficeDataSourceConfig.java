/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.config;

import com.digicore.omnexa.common.lib.properties.DatabasePropertyConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

/**
 * DataSource configuration for BackOffice module.
 *
 * <p>Configures database connection, entity scanning, and repository
 * initialization for the back office module.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Profile({"dev", "pilot", "prod"})
@Configuration
@ComponentScan({
        "com.digicore.omnexa.backoffice.modules",
        "com.digicore.omnexa.merchant.common.lib"
})
@EntityScan({
        "com.digicore.omnexa.backoffice.modules"
})
@EnableJpaRepositories(basePackages = {
        "com.digicore.omnexa.backoffice.modules"
})
@RequiredArgsConstructor
@EnableJpaAuditing
public class BackOfficeDataSourceConfig {

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
        dataSource.setConnectionTimeout(30000L);
        dataSource.setIdleTimeout(600000L);
        dataSource.setMaxLifetime(1800000L);
        dataSource.setMaximumPoolSize(100);
        dataSource.setMinimumIdle(10);
        dataSource.setLeakDetectionThreshold(30000);

        return dataSource;
    }
}
