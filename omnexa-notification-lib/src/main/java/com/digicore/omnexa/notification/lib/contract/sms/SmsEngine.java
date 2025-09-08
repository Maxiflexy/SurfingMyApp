package com.digicore.omnexa.notification.lib.contract.sms;

import com.digicore.omnexa.notification.lib.contract.sms.model.SmsProvider;
import com.digicore.omnexa.notification.lib.contract.sms.model.SmsRequest;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
public interface SmsEngine {

  void sendSms(SmsRequest smsRequest);

  void sendSmsAsync(SmsRequest smsRequest);

  SmsProvider provider();
}
