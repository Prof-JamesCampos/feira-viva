package br.com.feiraviva.factory;

import br.com.feiraviva.exception.ResourceNotFoundException;
import br.com.feiraviva.model.cupom.Cupom;
import br.com.feiraviva.model.cupom.CupomFixo;
import br.com.feiraviva.model.cupom.CupomPercentual;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CupomFactory {

    public Cupom criar(String codigo) {
        return switch (codigo == null ? "" : codigo.toUpperCase()) {
            case "FEIRA10"  -> new CupomPercentual("FEIRA10", "10% de desconto no subtotal",
                    new BigDecimal("10"));
            case "BEMVINDO" -> new CupomFixo("BEMVINDO", "R$ 15,00 de desconto",
                    new BigDecimal("15.00"));
            default -> throw new ResourceNotFoundException("Cupom inválido: " + codigo);
        };
    }
}