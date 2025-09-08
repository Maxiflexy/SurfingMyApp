package com.digicore.omnexa.notification.lib.contract.pushnotifier.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PushNotifierRequest {

  private String title;

  private String body;

  private List<String> tokens;

  private String topic;
  private String imageLink;
}
