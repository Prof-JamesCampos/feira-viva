package br.com.feiraviva.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(@NotNull Long enderecoId) {
}
