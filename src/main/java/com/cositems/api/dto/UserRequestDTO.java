package com.cositems.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "O nome de usuário não pode ser vazio.")
        @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres.")
        @Pattern(regexp = "^[\\p{L}\\p{N}_. -]+$", message = "O nome de usuário contém caracteres inválidos.")
        String displayName,

        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password) {
}