package br.com.feiraviva.strategy;

import br.com.feiraviva.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CalculadoraFrete {

    // O Spring injeta automaticamente: {"PADRAO": bean1, "FIXO": bean2, "RETIRADA": bean3}
    private final Map<String, StrategyFrete> strategies;

    public CalculadoraFrete(Map<String, StrategyFrete> strategies) {
        this.strategies = strategies;
    }

    public boolean existe(String tipo) {
        return strategies.containsKey(normalizar(tipo));
    }

    public BigDecimal calcular(String tipo, BigDecimal subtotal) {
        String normalizado = normalizar(tipo);
        if (!strategies.containsKey(normalizado)) {
            throw new ResourceNotFoundException("Estratégia de frete inválida: " + tipo);
        }
        return strategies.get(normalizado).calcular(subtotal);
    }

    private String normalizar(String tipo) {
        return (tipo == null || tipo.isBlank()) ? "PADRAO" : tipo.toUpperCase().trim();
    }
}