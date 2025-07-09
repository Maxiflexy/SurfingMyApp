/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.security;

import com.digicore.common.lib.exception.OmnexaException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Configuration class for JWT-related beans.
 *
 * <p>This class provides the necessary beans for handling JWT signing and validation using RSA keys
 * stored in a keystore. It includes methods to load the keystore, retrieve RSA private and public
 * keys, and configure a JWT decoder.
 *
 * <p>Features: - Loads keystore from a file path specified in application properties. - Retrieves
 * RSA private and public keys for signing and validation. - Configures a JWT decoder for validating
 * JWT tokens.
 *
 * <p>Usage: - Ensure the keystore file path and credentials are correctly configured in the
 * application properties. - Example properties:
 *
 * <pre>
 *   omnexa.security.jwt.key-store-path=/path/to/keystore.jks
 *   omnexa.security.jwt.key-store-password=keystorePassword
 *   omnexa.security.jwt.key-alias=keyAlias
 *   omnexa.security.jwt.private-key-passphrase=privateKeyPassphrase
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-23(Thu)-2025
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {
  private final SecurityPropertyConfig securityPropertyConfig;

  /**
   * Creates a {@link KeyStore} bean by loading the keystore file.
   *
   * @return the loaded {@link KeyStore}.
   * @throws OmnexaException if the keystore cannot be loaded.
   */
  @Bean
  public KeyStore keyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      Path path = Paths.get(securityPropertyConfig.getJwtKeyStorePath());
      InputStream resourceAsStream = new FileInputStream(path.toFile());
      keyStore.load(
          resourceAsStream, securityPropertyConfig.getJwtKeyStorePassword().toCharArray());
      return keyStore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      log.error("Unable to load keystore: {}", securityPropertyConfig.getJwtKeyStorePath(), e);
    }

    throw new OmnexaException("Unable to load keystore");
  }

  /**
   * Retrieves the RSA private key from the keystore for JWT signing.
   *
   * @param keyStore the {@link KeyStore} containing the private key.
   * @return the RSA private key.
   * @throws OmnexaException if the private key cannot be loaded.
   */
  @Bean
  public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
    try {
      Key key =
          keyStore.getKey(
              securityPropertyConfig.getJwtKeyAlias(),
              securityPropertyConfig.getJwtPrivateKeyPassphrase().toCharArray());
      if (key instanceof RSAPrivateKey rsaPrivateKey) {
        return rsaPrivateKey;
      }
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
      log.error(
          "Unable to load private key from keystore: {}",
          securityPropertyConfig.getJwtKeyStorePath(),
          e);
    }

    throw new OmnexaException("Unable to load private key");
  }

  /**
   * Retrieves the RSA public key from the keystore for JWT validation.
   *
   * @param keyStore the {@link KeyStore} containing the public key.
   * @return the RSA public key.
   * @throws OmnexaException if the public key cannot be loaded.
   */
  @Bean
  public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
    try {
      Certificate certificate = keyStore.getCertificate(securityPropertyConfig.getJwtKeyAlias());
      PublicKey publicKey = certificate.getPublicKey();

      if (publicKey instanceof RSAPublicKey rsaPublicKey) {
        return rsaPublicKey;
      }
    } catch (KeyStoreException e) {
      log.error(
          "Unable to load private key from keystore: {}",
          securityPropertyConfig.getJwtKeyStorePath(),
          e);
    }

    throw new OmnexaException("Unable to load RSA public key");
  }

  /**
   * Configures a {@link JwtDecoder} bean for validating JWT tokens.
   *
   * @param rsaPublicKey the RSA public key used for validation.
   * @return the configured {@link JwtDecoder}.
   */
  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
    return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
  }
}
