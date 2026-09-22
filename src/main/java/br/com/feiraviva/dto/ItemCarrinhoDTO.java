package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Item a ser adicionado ao carrinho")
public record ItemCarrinhoDTO(
        @Schema(description = "ID do produto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long produtoId,
        @Schema(description = "Quantidade desejada (mínimo 1)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull @Min(1) Integer quantidade) { }

