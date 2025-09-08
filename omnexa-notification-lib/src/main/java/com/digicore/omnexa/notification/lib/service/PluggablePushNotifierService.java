package com.digicore.omnexa.notification.lib.service;

import com.digicore.omnexa.notification.lib.contract.pushnotifier.PushNotifierEngine;
import com.digicore.omnexa.notification.lib.contract.pushnotifier.model.PushNotifierProvider;
import com.digicore.omnexa.notification.lib.exception.ExceptionOf;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 25 Fri Apr, 2025
 */
@Component
@RequiredArgsConstructor
public class PluggablePushNotifierService {

  private final Map<PushNotifierProvider, PushNotifierEngine> engineRegistry =
      new EnumMap<>(PushNotifierProvider.class);

  private final List<PushNotifierEngine> engines;

  @PostConstruct
  public void initialize() {
    for (PushNotifierEngine engine : engines) {
      engineRegistry.put(engine.provider(), engine);
    }
  }

  public PushNotifierEngine getEngine(PushNotifierProvider pushNotifierProvider) {
    PushNotifierEngine engine = engineRegistry.get(pushNotifierProvider);
    if (engine == null) {
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(
          "No push notifier engine found for: " + pushNotifierProvider);
    }
    return engine;
  }
}
