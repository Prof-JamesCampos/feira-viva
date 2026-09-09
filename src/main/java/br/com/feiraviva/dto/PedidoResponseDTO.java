package br.com.feiraviva.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponseDTO(
        Long id, String numero, String status,
        BigDecimal subtotal, BigDecimal frete, BigDecimal total,
        List<ItemResponseDTO> itens) { }
