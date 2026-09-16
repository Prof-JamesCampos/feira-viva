package br.com.feiraviva.model.cupom;

import java.math.BigDecimal;

public abstract class Cupom {

    private final String codigo;
    private final String descricao;

    protected Cupom(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }

    public abstract BigDecimal calcularDesconto(BigDecimal subtotal);
}