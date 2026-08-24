package br.com.feiraviva.model;

import java.math.BigDecimal;

public record Produto(Long id, String nome, BigDecimal preco, int estoque) {

}
