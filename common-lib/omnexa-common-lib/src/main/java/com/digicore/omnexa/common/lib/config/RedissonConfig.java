/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.config;

import com.digicore.omnexa.common.lib.security.SecurityPropertyConfig;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {
  private final SecurityPropertyConfig securityPropertyConfig;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config
        .useSingleServer()
        .setAddress(
            "redis://"
                + securityPropertyConfig.getRedisHost()
                + ":"
                + securityPropertyConfig.getRedisPort())
        .setPassword(
            securityPropertyConfig.getRedisPassword().isEmpty()
                ? null
                : securityPropertyConfig.getRedisPassword());

    return Redisson.create(config);
  }
}
