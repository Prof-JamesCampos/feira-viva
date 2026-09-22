package br.com.feiraviva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Cliente com endereços (senhaHash nunca é exposto)")
public record ClienteResponseDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "Maria da Silva") String nome,
        @Schema(example = "maria@email.com") String email,
        @Schema(example = "(11) 98765-4321") String telefone,
        List<EnderecoDTO> enderecos) {
}