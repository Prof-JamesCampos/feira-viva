package br.com.feiraviva.dto;

import java.util.List;

public record ClienteResponseDTO(
        Long id, String nome, String email, String telefone,
        List<EnderecoDTO> enderecos) {
}