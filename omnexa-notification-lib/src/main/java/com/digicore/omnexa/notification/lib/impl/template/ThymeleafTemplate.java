package com.digicore.omnexa.notification.lib.impl.template;

import com.digicore.omnexa.notification.lib.config.NotificationPropConfig;
import com.digicore.omnexa.notification.lib.contract.template.TemplateEngine;
import com.digicore.omnexa.notification.lib.contract.template.model.TemplateProvider;
import com.digicore.omnexa.notification.lib.exception.ExceptionOf;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThymeleafTemplate implements TemplateEngine {

  private final SpringTemplateEngine templateEngine;
  private final NotificationPropConfig notificationPropConfig;

  @PostConstruct
  public void templateResolver() {
    FileTemplateResolver resolver = new FileTemplateResolver();
    resolver.setPrefix(notificationPropConfig.getTemplatePath());
    resolver.setSuffix(".html");
    resolver.setTemplateMode("HTML5");
    resolver.setOrder(templateEngine.getTemplateResolvers().size());
    resolver.setCacheable(false);
    templateEngine.addTemplateResolver(resolver);
  }

  @Override
  public String parseTemplate(String templateName, Object placeHolders) {
    try {
      Context context = (Context) placeHolders;
      return templateEngine.process(templateName, context);
    } catch (Exception e) {
      log.info("Error processing Thymeleaf template", e);
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(e);
    }
  }

  @Override
  public TemplateProvider templateProvider() {
    return TemplateProvider.THYMELEAF;
  }
}
