package com.digicore.omnexa.notification.lib.contract.email;

import com.digicore.omnexa.notification.lib.contract.email.model.EmailChannelType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
public interface EmailEngine {
  void sendEmail(EmailRequest emailRequest);

  void sendEmailAsync(EmailRequest emailRequest);

  EmailChannelType channelType();
}
