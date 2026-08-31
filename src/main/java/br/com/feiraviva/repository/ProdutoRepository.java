package br.com.feiraviva.repository;

import br.com.feiraviva.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();
    List<Produto> findByCategoriaId(Long categoriaId);
}