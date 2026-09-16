package br.com.feiraviva.model.cupom;

import java.math.BigDecimal;

public class CupomFixo extends Cupom {

    private final BigDecimal valor;

    public CupomFixo(String codigo, String descricao, BigDecimal valor) {
        super(codigo, descricao);
        this.valor = valor;
    }

    @Override
    public BigDecimal calcularDesconto(BigDecimal subtotal) {
        return valor.min(subtotal);   // R7: teto = subtotal (nunca negativa)
    }
}