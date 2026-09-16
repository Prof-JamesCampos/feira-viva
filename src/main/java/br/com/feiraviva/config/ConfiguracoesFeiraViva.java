package br.com.feiraviva.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component   // escopo padrão do Spring: singleton (1 instância por container)
public class ConfiguracoesFeiraViva {

    private static final Logger log = LoggerFactory.getLogger(ConfiguracoesFeiraViva.class);

    private final BigDecimal freteFixo = new BigDecimal("15.00");
    private final BigDecimal freteGratisAcimaDe = new BigDecimal("100.00");
    private final String nomeLoja = "Feira Viva";
    private final String moeda = "BRL";

    @PostConstruct
    void logInstancia() {
        log.info("ConfiguracoesFeiraViva criada: identityHashCode={}", System.identityHashCode(this));
    }

    public BigDecimal getFreteFixo() { return freteFixo; }
    public BigDecimal getFreteGratisAcimaDe() { return freteGratisAcimaDe; }
    public String getNomeLoja() { return nomeLoja; }
    public String getMoeda() { return moeda; }
}