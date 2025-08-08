/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.bean;

import com.auth0.jwt.JWT;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import com.digicore.omnexa.common.lib.approval.workflow.request.RequestHandlerPostProcessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-08(Tue)-2025
 */
@Configuration
@RequiredArgsConstructor
public class BeanConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JWT jwt() {
    return new JWT();
  }

  @Bean
  public RequestHandlerPostProcessor requestHandlerPostProcessor() {
    return new RequestHandlerPostProcessor(List.of(RequestHandlerType.PROCESS_MAKER_REQUESTS));
  }
}
