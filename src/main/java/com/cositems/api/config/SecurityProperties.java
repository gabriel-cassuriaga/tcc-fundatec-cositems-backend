package com.cositems.api.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application.security")
@Validated
public record SecurityProperties(@NotNull TokenProperties token, @NotNull AdminProperties admin) {

    public record TokenProperties(
            @NotBlank String secret,
            int expirationHours,
            String algorithm) {
    }

    public record AdminProperties(@NotBlank String password) {
    }
}