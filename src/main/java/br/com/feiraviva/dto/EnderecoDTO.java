package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Endereço de entrega")
public record EnderecoDTO(
        @Schema(description = "CEP com 8 dígitos", example = "01310100", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,

        @Schema(description = "Logradouro", example = "Av. Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 120) String logradouro,

        @Schema(description = "Número", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String numero,

        @Schema(description = "Complemento (opcional)", example = "Apto 42")
        String complemento,

        @Schema(description = "Bairro", example = "Bela Vista", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String bairro,

        @Schema(description = "Cidade", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String cidade,

        @Schema(description = "UF com 2 letras", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 2, max = 2) String uf) {
}