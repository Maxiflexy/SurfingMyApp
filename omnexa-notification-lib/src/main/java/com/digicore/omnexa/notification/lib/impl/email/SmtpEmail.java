package com.digicore.omnexa.notification.lib.impl.email;

import com.digicore.omnexa.notification.lib.contract.email.EmailEngine;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;
import com.digicore.omnexa.notification.lib.contract.template.model.TemplateProvider;
import com.digicore.omnexa.notification.lib.exception.ExceptionOf;
import com.digicore.omnexa.notification.lib.helper.LogHelper;
import com.digicore.omnexa.notification.lib.service.PluggableTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpEmail implements EmailEngine {

  private final JavaMailSender mailSender;

  private final PluggableTemplateService pluggableTemplateService;

  @Override
  public void sendEmail(EmailRequest emailRequest) {
    String traceId = UUID.randomUUID().toString();
    LogHelper.logObject(emailRequest, traceId);
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      buildBasicMessageHelper(emailRequest, helper);

      normalizeEmailCopy(emailRequest, helper);

      includeAttachment(emailRequest, helper);

      mailSender.send(message);
      log.info("Email sent to {} successfully", emailRequest.getRecipients());

    } catch (Exception e) {
      log.error("Failed to send email to {}", emailRequest.getRecipients(), e);
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(e);
    }
  }

  private void buildBasicMessageHelper(EmailRequest emailRequest, MimeMessageHelper helper)
      throws MessagingException {
    helper.setFrom(emailRequest.getSender());
    helper.setTo(emailRequest.getRecipients().toArray(new String[0]));

    if (emailRequest.getCcs() != null && !emailRequest.getCcs().isEmpty()) {
      helper.setCc(emailRequest.getCcs().toArray(new String[0]));
    }

    if (emailRequest.getBccs() != null && !emailRequest.getBccs().isEmpty()) {
      helper.setBcc(emailRequest.getBccs().toArray(new String[0]));
    }

    helper.setSubject(emailRequest.getSubject());
    helper.setText(
        emailRequest.getCopy() == null ? "" : emailRequest.getCopy(), emailRequest.isHtml());
  }

  private void includeAttachment(EmailRequest emailRequest, MimeMessageHelper helper)
      throws MessagingException {
    for (MultipartFile attachment : emailRequest.getAttachments()) {
      if (!attachment.isEmpty()) {
        helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
      }
    }
  }

  private void normalizeEmailCopy(EmailRequest emailRequest, MimeMessageHelper helper)
      throws MessagingException {
    if (emailRequest.isUseTemplate()) {
      Context context = new Context();
      context.setVariables(emailRequest.getPlaceHolders());

      //      if (placeholders instanceof Context context) {
      String parsedHtml =
          pluggableTemplateService
              .getEngine(TemplateProvider.THYMELEAF)
              .parseTemplate(emailRequest.getTemplateName(), context);
      helper.setText(parsedHtml, true);
      //      } else {
      //        log.warn(
      //            "Invalid placeholder object type. Expected Thymeleaf Context but got: {}",
      //            placeholders != null ? placeholders.getClass().getName() : "null");
      //        helper.setText(
      //            emailRequest.getCopy() == null ? "" : emailRequest.getCopy(),
      // emailRequest.isHtml());
      //      }
    }
  }

  @Async
  public void sendEmailAsync(EmailRequest emailRequest) {
    sendEmail(emailRequest);
  }

  @Override
  public EmailChannelType channelType() {
    return EmailChannelType.SMTP;
  }
}
