package com.digicore.omnexa.notification.lib.service;

import com.digicore.omnexa.notification.lib.config.NotificationPropConfig;
import com.digicore.omnexa.notification.lib.contract.email.EmailEngine;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.exception.ExceptionOf;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Component
@RequiredArgsConstructor
public class PluggableEmailService {

  @Getter private final PluggableTemplateService pluggableTemplateService;
  @Getter private final NotificationPropConfig notificationPropConfig;

  private final Map<EmailChannelType, EmailEngine> engineRegistry =
      new EnumMap<>(EmailChannelType.class);

  private final List<EmailEngine> engines;

  @PostConstruct
  public void initialize() {
    for (EmailEngine engine : engines) {
      engineRegistry.put(engine.channelType(), engine);
    }
  }

  public EmailEngine getEngine(EmailChannelType channelType) {
    EmailEngine engine = engineRegistry.get(channelType);
    if (engine == null) {
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(
          "No email engine found for: " + channelType);
    }
    return engine;
  }
}
