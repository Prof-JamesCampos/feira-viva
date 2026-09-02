package br.com.feiraviva.dto;

import jakarta.validation.constraints.*;

public record ClienteDTO(
        @NotBlank @Size(min = 3, max = 120) String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 60) String senha,
        String telefone) {
}