package br.com.feiraviva.dto;

import jakarta.validation.constraints.*;

public record EnderecoDTO(
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,
        @NotBlank @Size(max = 120) String logradouro,
        @NotBlank String numero,
        String complemento,
        @NotBlank String bairro,
        @NotBlank String cidade,
        @NotBlank @Size(min = 2, max = 2) String uf) {
}