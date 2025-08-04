/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.digicore.omnexa.backoffice.modules.user.authentication.dto.response.BackOfficeLoginProfileDTO;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT token operations in the back office module.
 *
 * <p>This utility handles JWT token creation, validation, and claim extraction for back office user
 * authentication.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeJwtUtil {

  private final RSAPrivateKey privateKey;
  private final RSAPublicKey publicKey;
  private final JWT jwtDecoder;

  /**
   * Creates a JWT token with the specified subject and claims.
   *
   * @param subject the subject of the token (typically username)
   * @param claims the claims to include in the token
   * @return the generated JWT token
   */
  public String createJwtForClaims(String subject, Map<String, String> claims) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(Instant.now().toEpochMilli());
    calendar.add(Calendar.MINUTE, 60); // Token expires in 60 minutes

    JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

    // Add claims
    claims.forEach(jwtBuilder::withClaim);

    // Add expiredAt and etc
    return jwtBuilder
        .withNotBefore(new Date())
        .withExpiresAt(calendar.getTime())
        .sign(Algorithm.RSA256(publicKey, privateKey));
  }

  /**
   * Checks if a JWT token is expired.
   *
   * @param token the JWT token to check
   * @return true if the token is expired, false otherwise
   */
  public boolean isTokenExpired(String token) {
    DecodedJWT payload = jwtDecoder.decodeJwt(token);
    LocalDateTime expiryDate = convertToLocalDateTimeViaMiliSecond(payload.getExpiresAt());
    return expiryDate.isBefore(LocalDateTime.now());
  }

  /**
   * Extracts the username from a JWT token.
   *
   * @param token the JWT token
   * @return the username (subject) from the token
   */
  public String extractUsername(String token) {
    DecodedJWT payload = jwtDecoder.decodeJwt(token);
    return payload.getSubject();
  }

  /**
   * Converts a Date to LocalDateTime.
   *
   * @param dateToConvert the date to convert
   * @return the converted LocalDateTime
   */
  private LocalDateTime convertToLocalDateTimeViaMiliSecond(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  /**
   * Builds JWT claims for a back office user.
   *
   * @param username the username
   * @param userDetails the user details
   * @return Map containing the claims
   */
  public static Map<String, String> buildClaims(
      String username, BackOfficeLoginProfileDTO userDetails) {
    Map<String, String> claims = new HashMap<>();
    claims.put("username", username);
    claims.put("profileId", userDetails.getProfileId());
    claims.put("role", userDetails.getRole());

    String authorities =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
    claims.put("permissions", authorities);

    return claims;
  }
}
