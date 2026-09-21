package br.com.feiraviva.service;

import br.com.feiraviva.dto.CategoriaArvoreDTO;
import br.com.feiraviva.model.Categoria;
import br.com.feiraviva.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaArvoreDTO> arvore() {
        // 1 único SELECT no banco (evita o problema de N+1)
        var todas = categoriaRepository.findAll();

        // Filtra apenas as raízes (que não têm pai) e inicia a recursão
        return todas.stream()
                .filter(c -> c.getCategoriaPai() == null)
                .map(raiz -> montar(raiz, todas))
                .toList();
    }

    // Método recursivo (Composite)
    private CategoriaArvoreDTO montar(Categoria c, List<Categoria> todas) {
        var filhas = todas.stream()
                .filter(f -> f.getCategoriaPai() != null
                        && f.getCategoriaPai().getId().equals(c.getId()))
                .map(f -> montar(f, todas)) // recursão
                .toList();

        return new CategoriaArvoreDTO(
                c.getId(),
                c.getNome(),
                c.getCategoriaPai() == null ? null : c.getCategoriaPai().getId(),
                filhas.isEmpty(),
                filhas
        );
    }
}