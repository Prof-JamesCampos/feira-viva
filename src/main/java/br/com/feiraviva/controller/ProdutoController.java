package br.com.feiraviva.controller;

import br.com.feiraviva.model.Produto;
import br.com.feiraviva.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name="Catálogo")
public class ProdutoController {

    private final ProdutoService produtoService;

    // Injeção de dependência via construtor
    public ProdutoController(ProdutoService produtoService) {

        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(summary = "Listar produtos ativos",
                description = "Retorna todos os produtos ativos do catalogo.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos")
    public List<Produto> listar() {

        return produtoService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar produto", description = "Busca produto pelo ID.")
    @ApiResponse(responseCode = "200", description = "Produto encontraado.")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
    public Produto buscar(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long id) {

        return produtoService.buscar(id);
    }

}