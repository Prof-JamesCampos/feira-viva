package br.com.feiraviva.dto;

import java.math.BigDecimal;
import java.util.List;

public record ItemResponseDTO(
        Long id, Long produtoId, String nomeProduto,
        int quantidade, BigDecimal precoUnitario, BigDecimal subtotal) { }


