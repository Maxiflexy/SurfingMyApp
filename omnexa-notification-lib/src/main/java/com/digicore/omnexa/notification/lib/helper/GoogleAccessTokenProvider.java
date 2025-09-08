package com.digicore.omnexa.notification.lib.helper;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Component
public class GoogleAccessTokenProvider {

  private static final String FIREBASE_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
  private final GoogleCredentials credentials;

  private final ReentrantLock lock = new ReentrantLock();
  private volatile AccessToken cachedToken;

  public GoogleAccessTokenProvider(
      @Value("${firebase.service-account-path:/firebase/xtbank-4c6e1.json}")
          String serviceAccountPath)
      throws IOException {
    InputStream credentialsStream;

    Path filePath = Paths.get(serviceAccountPath);
    if (Files.exists(filePath)) {
      credentialsStream = Files.newInputStream(filePath);
    } else {
      ClassPathResource classPathResource =
          new ClassPathResource(
              serviceAccountPath.startsWith("/")
                  ? serviceAccountPath.substring(1)
                  : serviceAccountPath);
      if (!classPathResource.exists()) {
        throw new FileNotFoundException("Service account file not found at: " + serviceAccountPath);
      }
      credentialsStream = classPathResource.getInputStream();
    }

    this.credentials =
        GoogleCredentials.fromStream(credentialsStream).createScoped(List.of(FIREBASE_SCOPE));
  }

  @SneakyThrows
  public String getAccessToken() {
    if (tokenNeedsRefresh()) {
      refreshTokenSafely();
    }
    return cachedToken.getTokenValue();
  }

  private boolean tokenNeedsRefresh() {
    AccessToken token = this.cachedToken;
    if (token == null || token.getExpirationTime() == null) {
      return true;
    }
    Instant expiresAt = token.getExpirationTime().toInstant();
    return Instant.now().isAfter(expiresAt.minus(Duration.ofMinutes(1)));
  }

  private void refreshTokenSafely() throws IOException {
    lock.lock();
    try {
      if (tokenNeedsRefresh()) {
        credentials.refreshIfExpired();
        this.cachedToken = credentials.getAccessToken();
      }
    } finally {
      lock.unlock();
    }
  }
}
