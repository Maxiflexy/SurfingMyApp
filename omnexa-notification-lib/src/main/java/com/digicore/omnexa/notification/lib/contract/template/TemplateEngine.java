package com.digicore.omnexa.notification.lib.contract.template;

import com.digicore.omnexa.notification.lib.contract.template.model.TemplateProvider;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
public interface TemplateEngine {

  default String parseTemplate(String templatePath, String templateName, Object placeHolders) {
    return "";
  }

  default String parseTemplate(String templateName, Object placeHolders) {
    return "";
  }

  TemplateProvider templateProvider();
}
