package com.cositems.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cositems.api.user.model.Admin;
import com.cositems.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Override
    public void run(String... args) throws Exception {
        final String adminEmail = "admin@cositems.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            String adminPassword = securityProperties.admin().password();

            Admin adminUser = Admin.builder()
                    .displayName("Admin Cositems")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .build();

            userRepository.save(adminUser);
            System.out.println("Usuário ADMIN padrão criado com sucesso!");
        }
    }
}