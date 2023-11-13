package com.institutosermelhor.ManagerCore.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.institutosermelhor.ManagerCore.models.entity.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.create()
        .withIssuer("manager_ism")
        .withSubject(user.getEmail())
        .withExpiresAt(generateExpirationDate())
        .sign(algorithm);
  }

  public String validateToken(String token) {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return JWT.require(algorithm)
        .withIssuer("manager_ism")
        .build()
        .verify(token)
        .getSubject();
  }

  private Instant generateExpirationDate() {
    return LocalDateTime.now()
        .plusDays(1)
        .toInstant(ZoneOffset.of("-03:00"));
  }

}