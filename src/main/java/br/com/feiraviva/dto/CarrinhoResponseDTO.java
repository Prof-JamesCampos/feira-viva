package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Carrinho com totais calculados pelo Service")
public record CarrinhoResponseDTO(
        @Schema(example = "1")
        Long id,
        List<ItemResponseDTO> itens,

        @Schema(description = "Código do cupom aplicado (null se nenhum)", example = "FEIRA10")
        String cupom,
        @Schema(description = "Desconto em reais", example ="7.00")
        BigDecimal desconto,
        @Schema(description = "Estratégia de frete vigente", example = "PADRAO", allowableValues = {"PADRAO", "FIXO", "RETIRADA"})
        String estrategiaFrete, // NOVO CAMPO

        @Schema(example = "70.00")
        BigDecimal subtotal,
        @Schema(example = "15.00")
        BigDecimal frete,
        @Schema(example = "78.00")
        BigDecimal total) {
}