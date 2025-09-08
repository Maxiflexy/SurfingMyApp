package com.digicore.omnexa.notification.lib.impl.email;

import com.digicore.omnexa.notification.lib.contract.email.EmailEngine;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;
import com.digicore.omnexa.notification.lib.exception.ExceptionOf;
import com.digicore.omnexa.notification.lib.helper.LogHelper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SendGridEmail implements EmailEngine {
  @Value(
      "${omnexa.sendgrid.api-key:SG.xxxxxxx}") // Replace with your SendGrid API key or use environment variable
  private String sendGridApiKey;

  @Override
  public void sendEmail(EmailRequest emailRequest) {
    String traceId = UUID.randomUUID().toString();
    LogHelper.logObject(emailRequest, traceId);
    try {
      Mail mail = buildMail(emailRequest);
      SendGrid sg = new SendGrid(sendGridApiKey);

      Request request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());

      LogHelper.logObject(request, traceId);

      Response response = sg.api(request);

      LogHelper.logObject(response, traceId);

    } catch (Exception e) {
      log.error("Failed to send email via SendGrid to {}", emailRequest.getRecipients(), e);
      throw ExceptionOf.System.InternalError.SERVER_ERROR.exception(e);
    }
  }

  @Async
  public void sendEmailAsync(EmailRequest emailRequest) {
    sendEmail(emailRequest);
  }

  private Mail buildMail(EmailRequest emailRequest) throws IOException {
    Mail mail = new Mail();
    mail.setFrom(new Email(emailRequest.getSender()));
    mail.setSubject(emailRequest.getSubject());

    if (emailRequest.isUseTemplate()) {
      mail.setTemplateId(emailRequest.getTemplateName());
      addTemplatePersonalization(mail, emailRequest);
    } else {
      mail.addContent(
          new Content(emailRequest.isHtml() ? "text/html" : "text/plain", emailRequest.getCopy()));
      addBasicPersonalization(mail, emailRequest);
    }

    addAttachments(mail, emailRequest.getAttachments());
    return mail;
  }

  private void addTemplatePersonalization(Mail mail, EmailRequest request) {
    for (String recipient : request.getRecipients()) {
      Personalization personalization = createPersonalization(request, recipient);

      Object rawPlaceholders = request.getPlaceHolders();
      if (rawPlaceholders instanceof Map<?, ?> placeholdersMap) {
        placeholdersMap.forEach(
            (key, value) -> {
              if (key instanceof String k) {
                personalization.addDynamicTemplateData(k, value);
              }
            });
      } else {
        log.warn("Invalid placeholders object — expected Map<String, Object>");
      }

      mail.addPersonalization(personalization);
    }
  }

  private void addBasicPersonalization(Mail mail, EmailRequest request) {
    for (String recipient : request.getRecipients()) {
      Personalization personalization = createPersonalization(request, recipient);
      mail.addPersonalization(personalization);
    }
  }

  private Personalization createPersonalization(EmailRequest request, String recipient) {
    Personalization personalization = new Personalization();
    personalization.addTo(new Email(recipient));

    if (request.getCcs() != null) {
      request.getCcs().forEach(cc -> personalization.addCc(new Email(cc)));
    }

    if (request.getBccs() != null) {
      request.getBccs().forEach(bcc -> personalization.addBcc(new Email(bcc)));
    }

    return personalization;
  }

  private void addAttachments(Mail mail, List<MultipartFile> attachments) throws IOException {
    for (MultipartFile file : attachments) {
      Attachments attachment = new Attachments();
      attachment.setFilename(file.getOriginalFilename());
      attachment.setType(file.getContentType());
      attachment.setDisposition("attachment");
      attachment.setContent(Base64.getEncoder().encodeToString(file.getBytes()));
      mail.addAttachments(attachment);
    }
  }

  @Override
  public EmailChannelType channelType() {
    return EmailChannelType.SENDGRID;
  }
}
