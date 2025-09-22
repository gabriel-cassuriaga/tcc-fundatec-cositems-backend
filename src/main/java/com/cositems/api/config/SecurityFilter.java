package com.cositems.api.config;

import com.cositems.api.exception.ErrorResponseDTO;
import com.cositems.api.service.AuthenticationService;
import com.cositems.api.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {

            try {
                String userId = tokenService.validateTokenAndGetSubject(token);
                if (userId != null) {
                    UserDetails user = authenticationService.loadUserByUsername(userId);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            } catch (Exception ex) {
                log.warn("Falha na autenticação para {}: {}", request.getRequestURI(), ex.getMessage());

                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                        Instant.now(),
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Authentication Failed",
                        ex.getMessage(),
                        request.getRequestURI());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}