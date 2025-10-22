package com.cositems.api.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.NOT_FOUND.value(),
                                "Resource Not Found",
                                ex.getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(BusinessRuleException.class)
        public ResponseEntity<ErrorResponseDTO> handleBusinessRule(BusinessRuleException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.CONFLICT.value(),
                                "Business Rule Violation",
                                ex.getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Invalid Argument",
                                ex.getMessage(),
                                request.getRequestURI());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AuthorizationException.class)
        public ResponseEntity<ErrorResponseDTO> handleAuthorization(AuthorizationException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.FORBIDDEN.value(),
                                "Forbidden",
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                "Authentication Failed",
                                "E-mail ou senha inválidos.",
                                request.getRequestURI());

                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(OptimisticLockingFailureException.class)
        public ResponseEntity<ErrorResponseDTO> handleOptimisticLockingFailure(OptimisticLockingFailureException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                "O recurso foi atualizado por outra operação. Tente novamente.",
                                request.getRequestURI());

                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(PaymentFailedException.class)
        public ResponseEntity<ErrorResponseDTO> handlePaymentFailed(PaymentFailedException ex,
                        HttpServletRequest request) {
                ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Payment failed",
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
}