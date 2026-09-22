package br.com.feiraviva.controller;

import br.com.feiraviva.dto.ClienteDTO;
import br.com.feiraviva.dto.ClienteResponseDTO;
import br.com.feiraviva.dto.EnderecoDTO;
import br.com.feiraviva.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@Tag(name="Clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {

        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    @Operation(summary = "Cadastrar cliente",
                description = "Cria um novo cliente. E-mail duplicado retorna 409.")
    @ApiResponse(responseCode = "201", description = "Cliente criado")
    @ApiResponse(responseCode = "400", description = "DTO inválido")
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    public ClienteResponseDTO criar(
            @Valid @RequestBody ClienteDTO dto) {
        return clienteService.criar(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar cliente",
                description = "Retorna cliente com seus endereços (senhaHash nunca é exposto).")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ClienteResponseDTO buscar(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id) {  // 200
        return clienteService.buscar(id);
    }

    @PostMapping("/{id}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)                       // 201
    @Operation(summary = "Adicionar endereço",
                description = "Adicionar um endereço de entrega ao cliente (relação de 1:N con cascade).")
    @ApiResponse(responseCode = "201", description = "Endereço adicionado")
    @ApiResponse(responseCode = "400", description = "DTO inválido (ex.: CEP sem 8 dígitos)")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public void adicionarEndereco(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EnderecoDTO dto) {
        clienteService.adicionarEndereco(id, dto);
    }
}