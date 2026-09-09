package br.com.feiraviva.repository;

import br.com.feiraviva.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByClienteId(Long clienteId);
}

// ItemCarrinhoRepository e ItemPedidoRepository: vazios, estendendo JpaRepository
// (itens são persistidos via cascade do pai)