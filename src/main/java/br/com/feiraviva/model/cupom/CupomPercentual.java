package br.com.feiraviva.model.cupom;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CupomPercentual extends Cupom {

    private final BigDecimal percentual;   // 10 = 10%

    public CupomPercentual(String codigo, String descricao, BigDecimal percentual) {
        super(codigo, descricao);
        this.percentual = percentual;
    }

    @Override
    public BigDecimal calcularDesconto(BigDecimal subtotal) {
        return subtotal.multiply(percentual)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}