package com.digicore.omnexa.notification.lib.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 01 Fri Aug, 2025
 */
@Configuration
public class JavaMailConfig {

  @Bean
  public JavaMailSender javaMailSender(
      @Value("${spring.thymeleaf.mail.host}") String host,
      @Value("${spring.thymeleaf.mail.port}") int port,
      @Value("${spring.thymeleaf.mail.username}") String username,
      @Value("${spring.thymeleaf.mail.password}") String password,
      @Value("${spring.thymeleaf.mail.properties.mail.smtp.auth}") String smtpAuth,
      @Value("${spring.thymeleaf.mail.properties.mail.smtp.starttls.enable}") String starttlsEnable,
      @Value("${spring.thymeleaf.mail.properties.mail.smtp.starttls.required}")
          String starttlsRequired,
      @Value("${spring.thymeleaf.mail.properties.mail.smtp.socketFactory.class}")
          String socketFactoryClass,
      @Value("${spring.thymeleaf.mail.properties.mail.smtp.socketFactory.fallback}")
          String socketFactoryFallback) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", smtpAuth);
    props.put("mail.smtp.starttls.enable", starttlsEnable);
    props.put("mail.smtp.starttls.required", starttlsRequired);
    props.put("mail.smtp.socketFactory.class", socketFactoryClass);
    props.put("mail.smtp.socketFactory.fallback", socketFactoryFallback);

    return mailSender;
  }
}
