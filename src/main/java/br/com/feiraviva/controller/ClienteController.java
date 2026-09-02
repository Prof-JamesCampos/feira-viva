package br.com.feiraviva.controller;

import br.com.feiraviva.dto.ClienteDTO;
import br.com.feiraviva.dto.ClienteResponseDTO;
import br.com.feiraviva.dto.EnderecoDTO;
import br.com.feiraviva.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)                       // 201
    public ClienteResponseDTO criar(@Valid @RequestBody ClienteDTO dto) {
        return clienteService.criar(dto);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscar(@PathVariable Long id) {  // 200
        return clienteService.buscar(id);
    }

    @PostMapping("/{id}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)                       // 201
    public void adicionarEndereco(@PathVariable Long id,
                                  @Valid @RequestBody EnderecoDTO dto) {
        clienteService.adicionarEndereco(id, dto);
    }
}