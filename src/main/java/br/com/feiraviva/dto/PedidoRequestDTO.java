package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para finalizar o pedido")
public record PedidoRequestDTO(
        @Schema(description = "ID do endereço de entrega", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long enderecoId) {
}
