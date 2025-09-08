package com.digicore.omnexa.notification.lib.service;

import com.digicore.omnexa.notification.lib.config.NotificationPropConfig;
import com.digicore.omnexa.notification.lib.contract.sms.SmsEngine;
import com.digicore.omnexa.notification.lib.contract.sms.model.SmsProvider;
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
public class PluggableSmsService {

  @Getter private final NotificationPropConfig notificationPropConfig;

  private final Map<SmsProvider, SmsEngine> engineRegistry = new EnumMap<>(SmsProvider.class);

  private final List<SmsEngine> engines;

  @PostConstruct
  public void initialize() {
    for (SmsEngine engine : engines) {
      engineRegistry.put(engine.provider(), engine);
    }
  }

  public SmsEngine getEngine(SmsProvider smsProvider) {
    SmsEngine engine = engineRegistry.get(smsProvider);
    if (engine == null) {
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(
          "No sms engine found for: " + smsProvider);
    }
    return engine;
  }
}
