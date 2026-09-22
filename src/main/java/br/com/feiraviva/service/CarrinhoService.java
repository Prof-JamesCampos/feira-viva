package br.com.feiraviva.service;

import br.com.feiraviva.config.ConfiguracoesFeiraViva;
import br.com.feiraviva.dto.*;
import br.com.feiraviva.exception.RegraDeNegocioException;
import br.com.feiraviva.exception.ResourceNotFoundException;
import br.com.feiraviva.factory.CupomFactory;
import br.com.feiraviva.model.*;
import br.com.feiraviva.repository.*;
import br.com.feiraviva.strategy.CalculadoraFrete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarrinhoService {

//    private static final BigDecimal FRETE_FIXO = new BigDecimal("15.00");
//    private static final BigDecimal FRETE_GRATIS_ACIMA_DE = new BigDecimal("100.00");

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ConfiguracoesFeiraViva configuracoes;
    private final CupomFactory cupomFactory;
    private final CalculadoraFrete calculadoraFrete;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ProdutoRepository produtoRepository,
                           ClienteRepository clienteRepository,
                           ConfiguracoesFeiraViva configuracoes,
                           CupomFactory cupomFactory,
                           CalculadoraFrete calculadoraFrete) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.configuracoes = configuracoes;
        this.cupomFactory = cupomFactory;
        this.calculadoraFrete = calculadoraFrete;
    }

    @Transactional
    public CarrinhoResponseDTO obter(Long clienteId) {
        return paraResponse(buscarOuCriar(clienteId));
    }

    @Transactional
    public CarrinhoResponseDTO adicionarItem(Long clienteId, ItemCarrinhoDTO dto) {
        var carrinho = buscarOuCriar(clienteId);
        var produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não encontrado: " + dto.produtoId()));

        if (produto.getEstoque() <= 0) {
            throw new RegraDeNegocioException("Produto sem estoque: " + produto.getNome());
        }

        carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(dto.produtoId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> setQuantidade(item, item.getQuantidade() + dto.quantidade()),
                        () -> {
                            var item = new ItemCarrinho(carrinho, produto, dto.quantidade());
                            setQuantidade(item, dto.quantidade());
                            carrinho.getItens().add(item);
                        });
        return paraResponse(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO alterarQuantidade(Long clienteId, Long itemId, int quantidade) {
        var carrinho = buscarOuCriar(clienteId);
        var item = carrinho.getItens().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado: " + itemId));

        if (quantidade <= 0) {
            carrinho.getItens().remove(item);        // orphanRemoval dá DELETE
        } else {
            setQuantidade(item, quantidade);
        }
        return paraResponse(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO removerItem(Long clienteId, Long itemId) {
        var carrinho = buscarOuCriar(clienteId);
        carrinho.getItens().removeIf(i -> i.getId().equals(itemId));
        return paraResponse(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO aplicarCupom(Long clienteId, String codigo) {
        var carrinho = buscarOuCriar(clienteId);
        var cupom = cupomFactory.criar(codigo);      // 404 se inválido
        carrinho.setCodigoCupom(cupom.getCodigo());
        return paraResponse(carrinho);
    }

    @Transactional
    public CarrinhoResponseDTO removerCupom(Long clienteId) {
        var carrinho = buscarOuCriar(clienteId);
        carrinho.setCodigoCupom(null);
        return paraResponse(carrinho);
    }

    // R1: quantidade nunca excede o estoque
    private void setQuantidade(ItemCarrinho item, int quantidade) {
        if (quantidade > item.getProduto().getEstoque()) {
            throw new RegraDeNegocioException("Estoque insuficiente para "
                    + item.getProduto().getNome()
                    + " (disponível: " + item.getProduto().getEstoque() + ")");
        }
        item.setQuantidade(quantidade);
    }

    private Carrinho buscarOuCriar(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    var cliente = clienteRepository.findById(clienteId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Cliente não encontrado: " + clienteId));
                    return carrinhoRepository.save(new Carrinho(cliente));
                });
    }

//    public BigDecimal calcularFrete(BigDecimal subtotal) {
//       // return subtotal.compareTo(FRETE_GRATIS_ACIMA_DE) >= 0
//        //       ? BigDecimal.ZERO : FRETE_FIXO;
//        return subtotal.compareTo(configuracoes.getFreteGratisAcimaDe()) >= 0
//                ? BigDecimal.ZERO
//                : configuracoes.getFreteFixo();
//    }

    @Transactional
    public CarrinhoResponseDTO definirEstrategiaFrete(Long clienteId, String tipo) {
        if (!calculadoraFrete.existe(tipo)) {
            throw new ResourceNotFoundException("Estratégia de frete inválida: " + tipo);
        }
        var carrinho = buscarOuCriar(clienteId);
        carrinho.setEstrategiaFrete(tipo.toUpperCase().trim());
        return paraResponse(carrinho);
    }

//    private CarrinhoResponseDTO paraResponse(Carrinho c) {
//        var itens = c.getItens().stream()
//                .map(i -> new ItemResponseDTO(i.getId(), i.getProduto().getId(),
//                        i.getProduto().getNome(), i.getQuantidade(),
//                        i.getPrecoUnitario(), i.getSubtotal()))
//                .toList();
//        var subtotal = c.getItens().stream()
//                .map(ItemCarrinho::getSubtotal)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        var frete = calcularFrete(subtotal);
//        return new CarrinhoResponseDTO(c.getId(), itens, subtotal, frete, subtotal.add(frete));
//    }

//private CarrinhoResponseDTO paraResponse(Carrinho c) {
//    var itens = c.getItens().stream()
//            .map(i -> new ItemResponseDTO(i.getId(), i.getProduto().getId(),
//                    i.getProduto().getNome(), i.getQuantidade(),
//                    i.getPrecoUnitario(), i.getSubtotal()))
//            .toList();
//    var subtotal = c.getItens().stream()
//            .map(ItemCarrinho::getSubtotal)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
//    var frete = calcularFrete(subtotal);

//    BigDecimal desconto = BigDecimal.ZERO;
//    String cupomAplicado = null;
//    if (c.getCodigoCupom() != null) {
//        var cupom = cupomFactory.criar(c.getCodigoCupom());
//        desconto = cupom.calcularDesconto(subtotal);
//        cupomAplicado = cupom.getCodigo();
//    }
//
//    var total = subtotal.add(frete).subtract(desconto);
//    return new CarrinhoResponseDTO(c.getId(), itens, cupomAplicado,
//            desconto, subtotal, frete, total);
//    }

    private CarrinhoResponseDTO paraResponse(Carrinho c) {
        var itens = c.getItens().stream()
                .map(i -> new ItemResponseDTO(i.getId(), i.getProduto().getId(),
                        i.getProduto().getNome(), i.getQuantidade(),
                        i.getPrecoUnitario(), i.getSubtotal()))
                .toList();

        var subtotal = c.getItens().stream()
                .map(ItemCarrinho::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Usa a Strategy (se não tiver nada salvo, assume PADRAO)
        var estrategia = c.getEstrategiaFrete() == null ? "PADRAO" : c.getEstrategiaFrete();
        var frete = calculadoraFrete.calcular(estrategia, subtotal);

        BigDecimal desconto = BigDecimal.ZERO;
        String cupomAplicado = null;
        if (c.getCodigoCupom() != null) {
            var cupom = cupomFactory.criar(c.getCodigoCupom());
            desconto = cupom.calcularDesconto(subtotal);
            cupomAplicado = cupom.getCodigo();
        }

        var total = subtotal.add(frete).subtract(desconto);

        return new CarrinhoResponseDTO(c.getId(), itens, cupomAplicado, desconto,
                estrategia, subtotal, frete, total);
    }
//    // REMOVER este método (diagnóstico didático — já cumpriu seu papel):
//    // diagnóstico didático — removível no deploy
//    public long identityHashCodeConfiguracoes() {
//
//        return System.identityHashCode(configuracoes);
//    }
}