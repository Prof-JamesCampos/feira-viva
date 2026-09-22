package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Item do carrinho ou do pedido")
public record ItemResponseDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long produtoId,
        @Schema(example = "Mel orgânico") String nomeProduto,
        @Schema(example = "2") int quantidade,
        @Schema(example = "35.00") BigDecimal precoUnitario,
        @Schema(example = "70.00") BigDecimal subtotal) {
}


