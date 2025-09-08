package com.digicore.omnexa.notification.lib.contract.pushnotifier;

import com.digicore.omnexa.notification.lib.contract.pushnotifier.model.PushNotifierProvider;
import com.digicore.omnexa.notification.lib.contract.pushnotifier.model.PushNotifierRequest;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
public interface PushNotifierEngine {

  void sendPush(PushNotifierRequest pushNotifierRequest);

  void sendPushAsync(PushNotifierRequest pushNotifierRequest);

  PushNotifierProvider provider();
}
