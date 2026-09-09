package br.com.feiraviva.controller;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.service.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    public CarrinhoResponseDTO obter(@RequestParam Long clienteId) {
        return carrinhoService.obter(clienteId);
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public CarrinhoResponseDTO adicionar(@RequestParam Long clienteId,
                                         @Valid @RequestBody ItemCarrinhoDTO dto) {
        return carrinhoService.adicionarItem(clienteId, dto);
    }

    @PutMapping("/itens/{itemId}")
    public CarrinhoResponseDTO alterar(@RequestParam Long clienteId,
                                       @PathVariable Long itemId,
                                       @RequestParam int quantidade) {
        return carrinhoService.alterarQuantidade(clienteId, itemId, quantidade);
    }

    @DeleteMapping("/itens/{itemId}")
    public CarrinhoResponseDTO remover(@RequestParam Long clienteId,
                                       @PathVariable Long itemId) {
        return carrinhoService.removerItem(clienteId, itemId);
    }
}