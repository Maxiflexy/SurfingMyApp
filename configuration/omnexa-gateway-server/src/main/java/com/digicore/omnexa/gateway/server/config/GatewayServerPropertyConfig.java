/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.gateway.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */

@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.gateway.server")
@Getter
@Setter
public class GatewayServerPropertyConfig {
 private boolean encryptPayload = true;
 private List<String> corsAllowedOrigins = null;
 private List<String> corsAllowedMethods = null;
 private List<String> corsAllowedHeaders = null;
 private List<String> corsAllowedExposedHeaders = null;
 private List<String> filteredUrl = null;
 private String redisHost = "";
 private String redisPort = "";
 private String redisPassword = "";
}
