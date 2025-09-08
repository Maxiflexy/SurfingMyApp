package com.digicore.omnexa.notification.lib.config;

import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.contract.sms.model.SmsProvider;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 01 Fri Aug, 2025
 */
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.notification")
@Getter
@Setter
public class NotificationPropConfig {
  private Map<String, String> templates;
  private String templatePath;
  private Map<String, String> subjects;
  private Map<String, String> senders;
  private String sender;
  private String serviceBaseUrl;
  private EmailChannelType emailChannelType = EmailChannelType.SMTP;
  private SmsProvider smsProvider = SmsProvider.VANSO;
  private String smsSenderId;
  private Map<String, String> smsSenderIds;

  public String getTemplate(String key, String defaultValue) {
    return templates != null ? templates.getOrDefault(key, defaultValue) : null;
  }

  public String getSubject(String key, String defaultValue) {
    return subjects != null ? subjects.getOrDefault(key, defaultValue) : null;
  }

  public String getSender(String key, String defaultValue) {
    if (senders != null) {
      return senders.getOrDefault(key, defaultValue);
    }
    return StringUtils.isNotBlank(sender) ? sender : defaultValue;
  }

  public String getSmsSenderId(String key, String defaultValue) {
    if (smsSenderIds != null) {
      return smsSenderIds.getOrDefault(key, defaultValue);
    }
    return StringUtils.isNotBlank(smsSenderId) ? smsSenderId : defaultValue;
  }
}
