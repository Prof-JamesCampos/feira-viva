package br.com.feiraviva.strategy;

import br.com.feiraviva.config.ConfiguracoesFeiraViva;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component("FIXO")
public class FreteFixoStrategy implements StrategyFrete {
    private final ConfiguracoesFeiraViva config;

    public FreteFixoStrategy(ConfiguracoesFeiraViva config) {
        this.config = config;
    }

    @Override
    public BigDecimal calcular(BigDecimal subtotal) {
        return config.getFreteFixo(); // Cobra sempre, independente do subtotal
    }
}