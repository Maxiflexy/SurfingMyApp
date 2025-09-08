package com.digicore.omnexa.common.lib.feign.config;

import com.digicore.omnexa.common.lib.util.RequestUtil;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Configuration
public class FeignClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      String accessToken = getBearerToken();
      if (!RequestUtil.nullOrEmpty(accessToken))
        requestTemplate.header("Authorization", "Bearer " + getBearerToken());
    };
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    // Return a custom error decoder if needed
    return new FeignErrorDecoder();
  }

  @Bean
  public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
    return new DynamicMultipartEncoder(new SpringFormEncoder(new SpringEncoder(messageConverters)));
  }

  private String getBearerToken() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
      Jwt jwt = jwtAuth.getToken();
      return jwt.getTokenValue();
    }
    return null;
  }
}
