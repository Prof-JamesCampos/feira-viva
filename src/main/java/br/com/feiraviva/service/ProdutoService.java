package br.com.feiraviva.service;

import br.com.feiraviva.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    public List<Produto> listar() {
        // Dados mockados — serão substituídos por JPA na Aula 07
        return List.of(
                new Produto(1L, "Mel orgânico", new BigDecimal("35.00"), 12),
                new Produto(2L, "Queijo minas", new BigDecimal("28.50"), 8),
                new Produto(3L, "Café da serra", new BigDecimal("42.00"), 20)
        );
    }
}
