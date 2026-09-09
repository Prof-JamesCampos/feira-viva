package br.com.feiraviva.dto;

import java.math.BigDecimal;
import java.util.List;

    public record CarrinhoResponseDTO(
            Long id, List<ItemResponseDTO> itens,
            BigDecimal subtotal, BigDecimal frete, BigDecimal total) { }

