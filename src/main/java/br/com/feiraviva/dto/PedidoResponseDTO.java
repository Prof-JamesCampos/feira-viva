package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Pedido confirmado (snapshot de preço nos itens)")
public record PedidoResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "FV-0001")
        String numero,

        @Schema(example = "CRIADO", allowableValues = {"CRIADO", "PAGO", "EM_PREPARO", "ENVIADO", "ENTREGUE", "CANCELADO"})
        String status,

        @Schema(example = "70.00")
        BigDecimal subtotal,

        @Schema(example = "15.00")
        BigDecimal frete,

        @Schema(example = "85.00")
        BigDecimal total,
        List<ItemResponseDTO> itens) {
}
