package br.com.feiraviva.controller;

import br.com.feiraviva.dto.CategoriaArvoreDTO;
import br.com.feiraviva.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Catálogo")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {

        this.categoriaService = categoriaService;
    }

    @GetMapping("/arvore")
    @Operation(summary = "Árvore de categorias (Composite)",
                description = "Retorna a hierarquia completa de categorias com subcategorias "
                        + "(1 select + montagem recursiva de memória).")
    @ApiResponse(responseCode = "200", description = "Árvore de categorias")
    public List<CategoriaArvoreDTO> arvore() {
        return categoriaService.arvore();
    }
}