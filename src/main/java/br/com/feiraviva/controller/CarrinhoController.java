package br.com.feiraviva.controller;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@Tag(name = "Carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    @Operation(summary = "Obter carrinho do cliente",
               description = "Retorna itens, cupom, estratégia de frete e totais calculados.")
    @ApiResponse(responseCode = "200", description = "Carrinho retornado (pode estar vazio)")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public CarrinhoResponseDTO obter(@RequestParam Long clienteId) {
        return carrinhoService.obter(clienteId);
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar item ao carrinho",
               description = "Aplica a regra R1 (estoque); soma quantidade se o produto já estiver no carrinho.")
    @ApiResponse(responseCode = "201", description = "Item adicionado")
    @ApiResponse(responseCode = "400", description = "DTO inválido (quantidade < 1)")
    @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
    @ApiResponse(responseCode = "409", description = "Produto sem estoque")
    public CarrinhoResponseDTO adicionar(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Valid @RequestBody ItemCarrinhoDTO dto) {
        return carrinhoService.adicionarItem(clienteId, dto);
    }

    @PutMapping("/itens/{itemId}")
    @Operation(summary = "Alterar quantidade do item no carrinho",
               description = "Aplica a quantidade do item. Quantidade <= 0 remove o item.")
    @ApiResponse(responseCode = "200", description = "Quantidade alterada")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
    public CarrinhoResponseDTO alterar(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Parameter(description = "ID do item do carrinho", required = true, example = "1")
            @PathVariable Long itemId,
            @Parameter(description = "Nova quantidade", required = true, example = "1")
            @RequestParam int quantidade) {
        return carrinhoService.alterarQuantidade(clienteId, itemId, quantidade);
    }

    @DeleteMapping("/itens/{itemId}")
    @Operation(summary = "Remover item", description = "Remove o item do carrinho (orphanRemoval).")
    @ApiResponse(responseCode = "200", description = "Item removido")
    public CarrinhoResponseDTO remover(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Parameter(description = "ID item do carrinho", required = true, example = "1")
            @PathVariable Long itemId) {
        return carrinhoService.removerItem(clienteId, itemId);
    }

    @PostMapping("/cupom")
    @Operation(summary = "Aplicar cupom",
               description = "Aplica cupom da CuponFactory. Campanhas vigentes: FEIRA10 (10%) e  BENVINDO (R$ 15).")
    @ApiResponse(responseCode = "200", description = "Cupom aplicado")
    @ApiResponse(responseCode = "404", description = "Cupom inválido")
    public CarrinhoResponseDTO aplicarCupom(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Parameter(description = "Código do cupom", required = true, example = "FEIRA10")
            @RequestParam String codigo) {
        return carrinhoService.aplicarCupom(clienteId, codigo);
    }

    @DeleteMapping("/cupom")
    @Operation(summary = "Remover cupom",
               description = "Remove o cupom aplicado ao carrinho.")
    @ApiResponse(responseCode = "200", description = "Cupom removido")
    public CarrinhoResponseDTO removerCupom(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId) {
        return carrinhoService.removerCupom(clienteId);
    }

    @PostMapping("/frete")
    @Operation(summary = "Definir estratégia de frete",
               description = "Estratégias vigentes: PADRAO (grátis acima de R$ 100), FIXO (sempre cobra) e RETIRADA (frete zero).")
    @ApiResponse(responseCode = "200", description = "Estratégia definida")
    @ApiResponse(responseCode = "404", description = "Estratégia inválida")
    public CarrinhoResponseDTO definirFrete(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @RequestParam Long clienteId,
            @Parameter(description = "Tipo de estratégia", required = true, example = "PADRAO")
            @RequestParam String tipo) {
        return carrinhoService.definirEstrategiaFrete(clienteId, tipo);
    }
}