package com.digicore.omnexa.notification.lib.impl.pushnotifier;

import com.digicore.omnexa.notification.lib.contract.pushnotifier.PushNotifierEngine;
import com.digicore.omnexa.notification.lib.contract.pushnotifier.model.PushNotifierProvider;
import com.digicore.omnexa.notification.lib.contract.pushnotifier.model.PushNotifierRequest;
import com.digicore.omnexa.notification.lib.helper.GoogleAccessTokenProvider;
import com.digicore.omnexa.notification.lib.helper.LogHelper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FcmPushNotifier implements PushNotifierEngine {

  private final WebClient webClient = WebClient.create();
  private final GoogleAccessTokenProvider tokenProvider;

  @Value("${firebase.project-id:xtbank-4c6e1}")
  private String projectId;

  @Value("${firebase.fcm-url:https://fcm.googleapis.com/v1/projects/}")
  private String fcmUrl;

  public void sendPush(PushNotifierRequest request) {
    String traceId = UUID.randomUUID().toString();
    LogHelper.logObject(request, traceId);
    String token = tokenProvider.getAccessToken();

    if (request.getTokens() != null && !request.getTokens().isEmpty()) {
      // Send to individual tokens
      List<String> responses =
          request.getTokens().stream()
              .map(
                  tokenTarget ->
                      sendToSingleToken(
                          token,
                          tokenTarget,
                          request.getTitle(),
                          request.getBody(),
                          request.getImageLink()))
              .toList();

      log.info("Notifications sent to {}", responses.size() + " devices");
    } else if (request.getTopic() != null && !request.getTopic().isEmpty()) {
      // Send to a topic
      sendToTopic(
          token, request.getTopic(), request.getTitle(), request.getBody(), request.getImageLink());
      log.info("Notification sent to topic: {}", request.getTopic());
    } else {
      log.info("No tokens or topic provided in the request.");
    }
  }

  @Async
  public void sendPushAsync(PushNotifierRequest request) {
    sendPush(request);
  }

  private String sendToSingleToken(
      String bearerToken, String targetToken, String title, String body, String imageLink) {
    Map<String, Object> payload =
        Map.of(
            "message",
            Map.of(
                "token",
                targetToken,
                "notification",
                Map.of(
                    "title", title,
                    "body", body,
                    "image", StringUtils.isBlank(imageLink) ? "" : imageLink)));

    try {

      String response =
          webClient
              .post()
              .uri(fcmUrl + projectId + "/messages:send")
              .headers(
                  headers -> {
                    headers.setBearerAuth(bearerToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                  })
              .bodyValue(payload)
              .retrieve()
              .bodyToMono(String.class)
              .block();

      log.info("Sent to token {}: {}", targetToken, response);
      return response;

    } catch (Exception e) {
      log.info("Failed to send to token {}: {}", targetToken, e.getMessage());
      return "Error sending to " + targetToken;
    }
  }

  private String sendToTopic(
      String bearerToken, String topic, String title, String body, String imageLink) {
    Map<String, Object> payload =
        Map.of(
            "message",
            Map.of(
                "notification",
                Map.of(
                    "title", title,
                    "body", body,
                    "image", StringUtils.isBlank(imageLink) ? "" : imageLink),
                "topic",
                topic));

    try {

      String response =
          webClient
              .post()
              .uri(fcmUrl + projectId + "/messages:send")
              .headers(
                  headers -> {
                    headers.setBearerAuth(bearerToken);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                  })
              .bodyValue(payload)
              .retrieve()
              .bodyToMono(String.class)
              .block();

      log.info("Sent to topic {}: {}", topic, response);
      return response;

    } catch (Exception e) {
      log.info("Failed to send to topic {}: {}", topic, e.getMessage());
      return "Error sending to topic " + topic;
    }
  }

  @Override
  public PushNotifierProvider provider() {
    return PushNotifierProvider.FCM;
  }
}
