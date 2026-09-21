package br.com.feiraviva.dto;

import java.util.List;

public record CategoriaArvoreDTO(
        Long id,
        String nome,
        Long categoriaPaiId,
        boolean folha,
        List<CategoriaArvoreDTO> subcategorias) {
}