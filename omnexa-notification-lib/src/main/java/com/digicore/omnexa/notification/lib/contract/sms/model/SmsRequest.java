package com.digicore.omnexa.notification.lib.contract.sms.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 01 Fri Aug, 2025
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SmsRequest {

  private Set<String> recipients;
  private String copy;
  private String senderId;
}
