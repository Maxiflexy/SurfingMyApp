package com.digicore.omnexa.notification.lib.impl.sms;

import com.digicore.omnexa.notification.lib.contract.sms.SmsEngine;
import com.digicore.omnexa.notification.lib.contract.sms.model.SmsProvider;
import com.digicore.omnexa.notification.lib.contract.sms.model.SmsRequest;
import com.digicore.omnexa.notification.lib.helper.LogHelper;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 01 Fri Aug, 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VansoSms implements SmsEngine {

  private final WebClient webClient = WebClient.create();

  @Value("${vanso.sms.username:NG.101.0721}")
  private String username;

  @Value("${vanso.sms.password:Pk3IaL3X}")
  private String password;

  @Value("${vanso.sms.url:https://sms.vanso.com/rest/sms/submit/bulk}")
  private String vansoSmsUrl;

  @Override
  public void sendSms(SmsRequest smsRequest) {
    String traceId = UUID.randomUUID().toString();
    LogHelper.logObject(smsRequest, traceId);
    try {

      Map<String, Object> payload =
          Map.of(
              "destinations",
              smsRequest.getRecipients(),
              "src",
              smsRequest.getSenderId(),
              "text",
              smsRequest.getCopy());

      String response =
          webClient
              .post()
              .uri(vansoSmsUrl)
              .headers(
                  headers -> {
                    headers.setBasicAuth(username, password);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                  })
              .bodyValue(payload)
              .retrieve()
              .bodyToMono(String.class)
              .block();

      LogHelper.logObject(response, traceId);

    } catch (Exception e) {
      log.info("Failed to send to sms: {}", e.getMessage());
    }
  }

  @Override
  @Async
  public void sendSmsAsync(SmsRequest smsRequest) {
    sendSms(smsRequest);
  }

  @Override
  public SmsProvider provider() {
    return SmsProvider.VANSO;
  }
}
