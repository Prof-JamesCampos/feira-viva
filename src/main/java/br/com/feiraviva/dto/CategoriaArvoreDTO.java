package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Nó da árvore de categorias (padrão Composite)")
public record CategoriaArvoreDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "Horta e Orgânicos") String nome,
        //@Schema(example = "Verduras, legumes e temperos") String descricao,
        @Schema(description = "ID da categoria pai (null nas raízes)", example = "1") Long categoriaPaiId,
        @Schema(description = "true se não possui subcategorias") boolean folha,
        List<CategoriaArvoreDTO> subcategorias) {
}