package br.com.feiraviva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemCarrinhoDTO(
        @NotNull Long produtoId,
        @NotNull @Min(1) Integer quantidade) { }

