package com.cositems.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.security.token")
public record SecurityProperties(String secret, int expirationHours, String algorithm) {
}
