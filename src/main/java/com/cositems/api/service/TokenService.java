package com.cositems.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cositems.api.config.SecurityProperties;
import com.cositems.api.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final SecurityProperties securityProperties;

    private Algorithm getAlgorithm() {
        return switch (securityProperties.token().algorithm().toUpperCase()) {
            case "HS512" -> Algorithm.HMAC512(securityProperties.token().secret());
            case "HS384" -> Algorithm.HMAC384(securityProperties.token().secret());
            default -> Algorithm.HMAC256(securityProperties.token().secret());
        };
    }

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = getAlgorithm();

            return JWT.create()
                    .withIssuer("cositems-api")
                    .withSubject(user.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            Algorithm algorithm = getAlgorithm();

            return JWT.require(algorithm)
                    .withIssuer("cositems-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(securityProperties.token().expirationHours())
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

}
