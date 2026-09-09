package br.com.feiraviva.controller;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO finalizar(@RequestParam Long clienteId,
                                       @Valid @RequestBody PedidoRequestDTO dto) {
        return pedidoService.finalizar(clienteId, dto);
    }

    @GetMapping
    public List<PedidoResponseDTO> historico(@RequestParam Long clienteId) {
        return pedidoService.historico(clienteId);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscar(@RequestParam Long clienteId, @PathVariable Long id) {
        return pedidoService.buscar(clienteId, id);
    }

    @PostMapping("/{id}/cancelamento")
    public PedidoResponseDTO cancelar(@RequestParam Long clienteId, @PathVariable Long id) {
        return pedidoService.cancelar(clienteId, id);
    }
}