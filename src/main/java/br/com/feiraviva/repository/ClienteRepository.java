package br.com.feiraviva.repository;

import br.com.feiraviva.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);   // método derivado: WHERE email = ?
}