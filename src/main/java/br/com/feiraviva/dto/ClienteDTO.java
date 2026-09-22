package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados de cadastro do cliente")
public record ClienteDTO(
        @Schema(description = "Nome completo", example = "Maria Souza", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 3, max = 120) String nome,

        @Schema(description = "E-mail único", example = "maria@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email String email,

        @Schema(description = "Senha (mínimo 6; provisória — hash entra na Aula 15)", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 6, max = 60) String senha,
        @Schema(description = "Telefone (opcional)", example = "(11) 98888-7777")
        String telefone) {
}