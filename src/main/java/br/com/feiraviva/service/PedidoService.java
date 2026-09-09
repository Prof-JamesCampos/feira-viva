package br.com.feiraviva.service;

import br.com.feiraviva.dto.*;
import br.com.feiraviva.exception.RegraDeNegocioException;
import br.com.feiraviva.exception.ResourceNotFoundException;
import br.com.feiraviva.model.*;
import br.com.feiraviva.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final EnderecoRepository enderecoRepository;
    private final CarrinhoService carrinhoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoRepository carrinhoRepository,
                         EnderecoRepository enderecoRepository,
                         CarrinhoService carrinhoService) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.enderecoRepository = enderecoRepository;
        this.carrinhoService = carrinhoService;
    }

    @Transactional
    public PedidoResponseDTO finalizar(Long clienteId, PedidoRequestDTO dto) {
        var carrinho = carrinhoRepository.findByClienteId(clienteId)
                .filter(c -> !c.getItens().isEmpty())
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Carrinho vazio: adicione itens antes de finalizar"));

        var endereco = enderecoRepository.findById(dto.enderecoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereço não encontrado: " + dto.enderecoId()));

        var pedido = new Pedido();
        pedido.setCliente(carrinho.getCliente());
        pedido.setEndereco(endereco);
        pedido.setStatus(PedidoStatus.CRIADO);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (var item : carrinho.getItens()) {
            var produto = item.getProduto();

            // R1 de novo, agora no momento crítico
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RegraDeNegocioException("Estoque insuficiente para " + produto.getNome());
            }
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());  // baixa

            // SNAPSHOT: congela o preço do momento da compra
            var itemPedido = new ItemPedido(pedido, produto,
                    item.getQuantidade(), item.getPrecoUnitario());
            pedido.getItens().add(itemPedido);
            subtotal = subtotal.add(itemPedido.getSubtotal());
        }

        var frete = carrinhoService.calcularFrete(subtotal);
        pedido.setSubtotal(subtotal);
        pedido.setFrete(frete);
        pedido.setTotal(subtotal.add(frete));

        pedidoRepository.save(pedido);
        pedido.setNumero(String.format("FV-%04d", pedido.getId()));  // dirty checking persiste

        carrinho.getItens().clear();   // carrinho zerado após a compra
        return paraResponse(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscar(Long clienteId, Long pedidoId) {
        return paraResponse(buscarPedido(clienteId, pedidoId));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> historico(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByDataCriacaoDesc(clienteId)
                .stream().map(this::paraResponse).toList();
    }

    // R4: cancelamento só com status CRIADO — e devolve o estoque
    @Transactional
    public PedidoResponseDTO cancelar(Long clienteId, Long pedidoId) {
        var pedido = buscarPedido(clienteId, pedidoId);
        if (pedido.getStatus() != PedidoStatus.CRIADO) {
            throw new RegraDeNegocioException(
                    "Só é possível cancelar pedidos com status CRIADO");
        }
        pedido.setStatus(PedidoStatus.CANCELADO);
        pedido.getItens().forEach(i -> i.getProduto()
                .setEstoque(i.getProduto().getEstoque() + i.getQuantidade()));
        return paraResponse(pedido);
    }

    private Pedido buscarPedido(Long clienteId, Long pedidoId) {
        var pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido não encontrado: " + pedidoId));
        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new ResourceNotFoundException("Pedido não encontrado: " + pedidoId);
        }
        return pedido;
    }

    private PedidoResponseDTO paraResponse(Pedido p) {
        var itens = p.getItens().stream()
                .map(i -> new ItemResponseDTO(i.getId(), i.getProduto().getId(),
                        i.getProduto().getNome(), i.getQuantidade(),
                        i.getPrecoUnitario(), i.getSubtotal()))
                .toList();
        return new PedidoResponseDTO(p.getId(), p.getNumero(), p.getStatus().name(),
                p.getSubtotal(), p.getFrete(), p.getTotal(), itens);
    }
}