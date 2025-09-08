package com.digicore.omnexa.notification.lib.service;

import com.digicore.omnexa.notification.lib.contract.template.TemplateEngine;
import com.digicore.omnexa.notification.lib.contract.template.model.TemplateProvider;
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
 * @createdOn 24 Thu Apr, 2025
 */
@Component
@RequiredArgsConstructor
public class PluggableTemplateService {

  private final Map<TemplateProvider, TemplateEngine> engineRegistry =
      new EnumMap<>(TemplateProvider.class);

  private final List<TemplateEngine> engines;

  @PostConstruct
  public void initialize() {
    for (TemplateEngine engine : engines) {
      engineRegistry.put(engine.templateProvider(), engine);
    }
  }

  public TemplateEngine getEngine(TemplateProvider templateProvider) {
    TemplateEngine engine = engineRegistry.get(templateProvider);
    if (engine == null) {
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(
          "No template engine found for: " + templateProvider);
    }
    return engine;
  }
}
