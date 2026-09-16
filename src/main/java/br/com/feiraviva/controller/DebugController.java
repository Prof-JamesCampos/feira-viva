package br.com.feiraviva.controller;

import br.com.feiraviva.config.ConfiguracoesFeiraViva;
import br.com.feiraviva.service.CarrinhoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private final ConfiguracoesFeiraViva configuracoes;
    private final CarrinhoService carrinhoService;

    public DebugController(ConfiguracoesFeiraViva configuracoes,
                           CarrinhoService carrinhoService) {
        this.configuracoes = configuracoes;
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/instancias")
    public Map<String, Object> instancias() {
        long hashController = System.identityHashCode(configuracoes);
        long hashService = carrinhoService.identityHashCodeConfiguracoes();
        return Map.of(
                "hashNoController", hashController,
                "hashNoCarrinhoService", hashService,
                "mesmaInstancia", hashController == hashService,
                "explicacao", "Escopo padrão do Spring é singleton: 1 instância por container"
        );
    }
}