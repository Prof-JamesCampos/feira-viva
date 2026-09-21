package br.com.feiraviva.strategy;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("RETIRADA")
public class FreteRetiradaStrategy implements StrategyFrete {
    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        return BigDecimal.ZERO; // Retirada na barraca: frete zero
    }
}