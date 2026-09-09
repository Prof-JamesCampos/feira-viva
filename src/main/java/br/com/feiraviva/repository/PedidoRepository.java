package br.com.feiraviva.repository;

import br.com.feiraviva.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);
}