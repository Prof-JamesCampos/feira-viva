package br.com.feiraviva.controller;

import br.com.feiraviva.dto.CategoriaArvoreDTO;
import br.com.feiraviva.service.CategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/arvore")
    public List<CategoriaArvoreDTO> arvore() {
        return categoriaService.arvore();
    }
}