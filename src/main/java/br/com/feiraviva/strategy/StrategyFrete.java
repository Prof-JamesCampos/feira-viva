package br.com.feiraviva.strategy;

import java.math.BigDecimal;

public interface StrategyFrete {
    BigDecimal calcular(BigDecimal subtotal);
}