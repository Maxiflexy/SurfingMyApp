package com.digicore.omni.root.services.config;

import com.auth0.jwt.JWT;
import com.digicore.api.helper.exception.ZeusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-27(Sat)-2022
 */

@Configuration
@EnableWebSecurity
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class RootServiceConfig {

    public static final String AUTHORITIES_CLAIM_NAME = "permissions";

    @Qualifier("delegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint authEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/merchant-signup/process/**",
                                "/api/v2/merchant-signup/process/**",
                                "/api/v1/backoffice-authentication/process/**",
                                "/api/v1/merchant-authentication/process/**",
                                "/api/v1/backoffice-admin/process/login",
                                "/api/v1/backoffice/merchant-dashboard/process/**",
                                "/swagger-ui.html",
                                "/documentation/**",
                                "/documentation/v3/api-docs/swagger-config",
                                "/documentation/v3/api-docs/swagger-config/**",
                                "/zest-root/documentation/v3/api-docs/swagger-config",
                                "/zest-root/documentation/v3/api-docs/swagger-config/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/documentation/v3/api-docs",
                                "/download-license",
                                "/actuator/**",
                                "/api/v1/report/**",
                                "/api/v1/backoffice/terminal-app/process/update-completed/**",
                                "/api/v1/sub-merchant/complete-onboarding",
                                "/api/v1/backoffice/terminal-app/process/download-latest-update/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> {
                            try {
                                sess
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .and()
                                        .oauth2ResourceServer()
                                        .authenticationEntryPoint(authEntryPoint)
                                        .jwt()
                                        .jwtAuthenticationConverter(authenticationConverter());
                            } catch (Exception e) {
                                throw new ZeusRuntimeException(e.getMessage());
                            }
                        }
                )
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);
        return http.build();
    }


    protected JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM_NAME);
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JWT jwt() {
        return new JWT();
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }


}
