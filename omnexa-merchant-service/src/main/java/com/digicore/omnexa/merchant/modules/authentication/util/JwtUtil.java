/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.digicore.omnexa.merchant.modules.authentication.dto.response.MerchantLoginProfileDTO;
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
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final RSAPrivateKey privateKey;
  private final RSAPublicKey publicKey;

  private final JWT jwtDecoder;

  public String createJwtForClaims(String subject, Map<String, String> claims) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(Instant.now().toEpochMilli());
    calendar.add(Calendar.MINUTE, 60);

    JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);

    // Add claims
    claims.forEach(jwtBuilder::withClaim);

    // Add expiredAt and etc
    return jwtBuilder
        .withNotBefore(new Date())
        .withExpiresAt(calendar.getTime())
        .sign(Algorithm.RSA256(publicKey, privateKey));
  }

  public boolean isTokenExpired(String token) {
    DecodedJWT payload = jwtDecoder.decodeJwt(token);
    LocalDateTime expiryDate = convertToLocalDateTimeViaMiliSecond(payload.getExpiresAt());
    return expiryDate.isBefore(LocalDateTime.now());
  }

  public String extractUsername(String token) {
    DecodedJWT payload = jwtDecoder.decodeJwt(token);
    return payload.getSubject();
  }

  private LocalDateTime convertToLocalDateTimeViaMiliSecond(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  public static Map<String, String> buildClaims(
      String username, MerchantLoginProfileDTO userDetails) {
    Map<String, String> claims = new HashMap<>();
    claims.put("username", username);

    String authorities =
        userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));
    claims.put("permissions", authorities);
    return claims;
  }
}
