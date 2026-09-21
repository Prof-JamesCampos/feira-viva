# Feira Viva — Modelo de Domínio

> Documento vivo do modelo de domínio do projeto.
> **Stack:** Java 21 LTS • Spring Boot 4.1.1 • Maven • React (Vite)
> **Pacote base:** `br.com.feiraviva`
> **Ambiente de desenvolvimento:** H2 em memória (`jdbc:h2:mem:feiraviva`) • porta **8083** • console em `/h2-console` (user `sa`, senha vazia)
> **Versão:** 0.9 — atualizado na Aula 12 (fechamento do Módulo 2)

## 1. Identificação

| Item | Definição |
|---|---|
| **Projeto** | Feira Viva — loja online de produtos locais |
| **Arquitetura** | Monólito modular baseado em MVC |
| **Stack Backend** | Java 21 (LTS) · Spring Boot 4.1.1 · Maven · Jakarta EE 11 · springdoc-openapi 3.0.1 |
| **Stack Frontend** | React + Vite (a partir da Aula 17) |
| **Pacote base** | `br.com.feiraviva` |
| **Model** | Entidades + regras de negócio (Spring/Jakarta) |
| **View** | Interface React |
| **Controller** | Rotas/endpoints (Spring MVC) |
| **Parceiro** | Associação de Produtores Locais |
| **Problema real** | Produtores vendem apenas na feira presencial; a comunidade não compra fora do horário da feira |
| **Público-alvo** | Moradores da região e pequenos produtores |

## 2. MVP

Compra de ponta a ponta: catálogo → detalhe do produto → carrinho (com cupom opcional e escolha da estratégia de frete) → login/cadastro → endereço de entrega → confirmação do pedido (pagamento simulado).

## 3. Entidades (Model)

| Entidade | Principais atributos | Relacionamentos | Status |
|---|---|---|---|
| **Produto** | id, nome, descricao, preco, sku, estoque, ativo, urlImagem | N:1 Categoria | ✅ Aula 07 |
| **Categoria** | id, nome, descricao, categoriaPai, subcategorias, `ehFolha()` | 1:N Produto; N:1 Categoria (pai); 1:N Categoria (filhas) | ✅ Aula 07 + Aula 11 (Composite) |
| **Cliente** | id, nome, email, senhaHash, telefone, papel | 1:N Endereco; 1:N Pedido; 1:1 Carrinho | ✅ Aula 08 |
| **Endereco** | id, cep, logradouro, numero, complemento, bairro, cidade, uf | N:1 Cliente; 1:N Pedido | ✅ Aula 08 |
| **Carrinho** | id, dataCriacao, codigoCupom, estrategiaFrete (totais calculados no Service) | 1:1 Cliente; 1:N ItemCarrinho | ✅ Aula 09 + Aula 10 (cupom) + Aula 11 (frete) |
| **ItemCarrinho** | id, quantidade, precoUnitario | N:1 Carrinho; N:1 Produto | ✅ Aula 09 |
| **Pedido** | id, numero (`FV-0001`), status, subtotal, frete, total, dataCriacao | N:1 Cliente; N:1 Endereco; 1:N ItemPedido; 1:1 Pagamento | ✅ Aula 09 |
| **ItemPedido** | id, quantidade, precoUnitario (**snapshot**) | N:1 Pedido; N:1 Produto | ✅ Aula 09 |
| **Pagamento** | id, metodo, status, idTransacao | 1:1 Pedido | 🕒 planejado (Aula 24) |

> **Snapshot de preço (Aula 09):** o `ItemPedido.precoUnitario` congela o preço no momento da compra; alterações futuras de preço não afetam pedidos históricos.

### Value Objects e componentes GoF

| Elemento | Tipo | Descrição | Status |
|---|---|---|---|
| **Cupom** (abstrata) | Value Object | `codigo`, `descricao`, `calcularDesconto(subtotal)`; persiste-se apenas `codigo_cupom` no carrinho | ✅ Aula 10 |
| **CupomPercentual** | Value Object | Desconto = subtotal × % / 100 (2 casas, HALF_UP) | ✅ Aula 10 |
| **CupomFixo** | Value Object | Desconto = `valor.min(subtotal)` (teto — R7) | ✅ Aula 10 |
| **CupomFactory** | Factory (simple factory) | `criar(codigo)` decide a classe concreta; código inválido → 404 | ✅ Aula 10 |
| **ConfiguracoesFeiraViva** | Singleton (escopo Spring) | `@Component` imutável: freteFixo (15.00), freteGratisAcimaDe (100.00), nomeLoja, moeda | ✅ Aula 10 |
| **StrategyFrete** | Interface Strategy | `calcular(subtotal)` — contrato do cálculo de frete | ✅ Aula 11 |
| **FretePadraoStrategy** | Strategy (`@Component("PADRAO")`) | Grátis se subtotal ≥ 100; caso contrário, freteFixo | ✅ Aula 11 |
| **FreteFixoStrategy** | Strategy (`@Component("FIXO")`) | Cobra freteFixo sempre | ✅ Aula 11 |
| **FreteRetiradaStrategy** | Strategy (`@Component("RETIRADA")`) | Sempre zero (retirada na barraca) | ✅ Aula 11 |
| **CalculadoraFrete** | Contexto Strategy | Injeta `Map<String, StrategyFrete>` (chave = nome do bean); tipo inválido → 404 | ✅ Aula 11 |

> **Decisões conscientes:** cupom é *value object* (comportamento de desconto), não entidade — validade/uso-único exigirão tabela `cupons` (evolução). A árvore de categorias usa a variante **uniforme** do Composite (a própria entidade é folha e composta, com `ehFolha()`).

### Enums

- `Papel`: `CLIENTE`, `ADMIN` — hoje persistido como `String` (`papel`); vira enum + JWT nas Aulas 14–15
- `PedidoStatus`: `CRIADO`, `PAGO`, `EM_PREPARO`, `ENVIADO`, `ENTREGUE`, `CANCELADO` — ✅ Aula 09 (`@Enumerated(STRING)`)
- `MetodoPagamento`: `CARTAO`, `PIX`, `BOLETO` — 🕒 planejado (Aula 24)
- `PagamentoStatus`: `APROVADO`, `RECUSADO`, `PENDENTE` — 🕒 planejado (Aula 24)

### Dados de desenvolvimento (DataSeeder) e campanhas vigentes

- **Categorias (árvore):** Horta e Orgânicos → Verduras, Temperos • Laticínios → Queijos
- **Produtos:** Mel orgânico (12), Alface crespa (30), Queijo minas (8), Iogurte natural (18), Café da serra (20)
- **Cupons (CupomFactory):** `FEIRA10` = 10% do subtotal • `BEMVINDO` = R$ 15,00 fixos
- **Estratégias de frete (CalculadoraFrete):** `PADRAO` • `FIXO` • `RETIRADA`

## 4. Diagrama de Relacionamentos

```text
Cliente (1) ──── (N) Endereco
Cliente (1) ──── (N) Pedido
Cliente (1) ──── (1) Carrinho ──(codigo_cupom: VO Cupom | estrategia_frete: VO Strategy)
Carrinho (1) ──── (N) ItemCarrinho (N) ──── (1) Produto
Pedido (1) ──── (N) ItemPedido (N) ──── (1) Produto
Pedido (N) ──── (1) Endereco
Pedido (1) ──── (1) Pagamento            (planejado — Aula 24)
Produto (N) ──── (1) Categoria
Categoria (N) ──── (1) Categoria (categoriaPai — árvore Composite)
Categoria (1) ──── (N) Categoria (subcategorias)
```

## 5. Regras de Negócio

| ID | Regra | Camada | Status |
|---|---|---|---|
| **R1** | Produto com estoque zero não entra no carrinho; quantidade nunca excede o estoque; baixa na finalização e devolução no cancelamento | Service / Model | ✅ Aula 09 |
| **R2** | Total = soma dos itens (snapshot) + frete (Strategy) − desconto do cupom | Service | ✅ Aulas 09/10/11 |
| **R3** | Pedido exige cliente autenticado; rotas `/admin/**` exigem papel `ADMIN` | Security / Controller | 🕒 Aulas 14–15 |
| **R4** | Cancelamento pelo cliente só com status `CRIADO` (devolve estoque) | Service | ✅ Aula 09 |
| **R5** | Senhas armazenadas com hash; autenticação via JWT | Security | 🕒 Aulas 14–15 |
| **R6** | Frete grátis acima de R$ 100,00 — versão **hardcoded aposentada na Aula 11**; comportamento preservado na estratégia `PADRAO` | Strategy | ✅ Aula 11 (refactor) |
| **R7** | Desconto fixo (CupomFixo) nunca supera o subtotal; cupom não sobrevive à finalização do pedido | Model (CupomFixo) / Service | ✅ Aula 10 |

## 6. Leitura MVC

### View (telas em React — a partir da Aula 17)

1. Home/Catálogo (árvore de categorias via Composite)
2. Detalhe do produto
3. Carrinho (campo de cupom + seletor de estratégia de frete)
4. Login/Cadastro
5. Checkout (endereço via CEP + pagamento)
6. Confirmação do pedido *(iteração)*
7. Meus pedidos *(iteração)*
8. Administração *(iteração)*

### Controller (rotas Spring MVC)

| Rota | Método | Finalidade | Acesso | Status |
|---|---|---|---|---|
| `/produtos` | GET | Listar com filtros | Público | ✅ Aula 06/07 |
| `/produtos/{id}` | GET | Detalhar | Público | ✅ Aula 07 |
| `/categorias` | GET | Listagem plana | Público | 🕒 planejado |
| `/categorias/arvore` | GET | Árvore de categorias (Composite, 1 select + recursão) | Público | ✅ Aula 11 |
| `/clientes` | POST | Cadastrar cliente (201) | Público | ✅ Aula 08 |
| `/clientes/{id}` | GET | Consultar cliente c/ endereços | Público → autenticado | ✅ Aula 08 |
| `/clientes/{id}/enderecos` | POST | Adicionar endereço (201) | Público → autenticado | ✅ Aula 08 |
| `/carrinho` | GET | Exibir carrinho (itens, cupom, desconto, estrategiaFrete, totais) | `?clienteId=` | ✅ Aula 09 |
| `/carrinho/itens` | POST | Adicionar item (201) | `?clienteId=` | ✅ Aula 09 |
| `/carrinho/itens/{itemId}` | PUT | Alterar quantidade | `?clienteId=&quantidade=` | ✅ Aula 09 |
| `/carrinho/itens/{itemId}` | DELETE | Remover item | `?clienteId=` | ✅ Aula 09 |
| `/carrinho/cupom` | POST | Aplicar cupom (404 se inválido) | `?clienteId=&codigo=` | ✅ Aula 10 |
| `/carrinho/cupom` | DELETE | Remover cupom | `?clienteId=` | ✅ Aula 10 |
| `/carrinho/frete` | POST | Definir estratégia de frete (404 se inválida) | `?clienteId=&tipo=` | ✅ Aula 11 |
| `/pedidos` | POST | Finalizar pedido (201; limpa itens, cupom **e estratégia**) | `?clienteId=` | ✅ Aulas 09/11 |
| `/pedidos` | GET | Histórico do cliente | `?clienteId=` | ✅ Aula 09 |
| `/pedidos/{id}` | GET | Detalhar pedido | `?clienteId=` | ✅ Aula 09 |
| `/pedidos/{id}/cancelamento` | POST | Cancelar (R4) | `?clienteId=` | ✅ Aula 09 |
| `/auth/login` · `/auth/registro` | POST | Autenticar / registrar (JWT) | Público | 🕒 Aulas 14–15 |
| `/admin/produtos` | POST/PUT/DELETE | Gerenciar produtos | `ADMIN` | 🕒 iteração |
| `/admin/pedidos/{id}/status` | PUT | Atualizar status | `ADMIN` | 🕒 iteração |

> ⚠️ **Dívida técnica ativa:** `clienteId` como query parameter — substituído pelo JWT nas Aulas 14–15.
> 🗑️ **Removido na Aula 12:** `GET /debug/instancias` (endpoint didático de prova do singleton — dívida paga e registrada no histórico).

### Tratamento de erros (contrato da API)

| Situação | Status | Corpo |
|---|---|---|
| Criação com sucesso | 201 | DTO de resposta |
| Consulta com sucesso | 200 | DTO de resposta |
| DTO inválido (`@Valid`) | 400 | `{"erro":"validacao","mensagem":"campo: msg | ..."}` |
| Recurso inexistente (cliente, produto, endereço, pedido, cupom, estratégia de frete) | 404 | `{"erro":"nao_encontrado","mensagem":"..."}` |
| Regra de negócio (estoque, e-mail duplicado, cancelamento) | 409 | `{"erro":"regra_de_negocio","mensagem":"..."}` |
| JSON malformado | 400 | `{"erro":"json_invalido","mensagem":"..."}` |

### Padrões GoF aplicados

| Padrão | Implementação | Status |
|---|---|---|
| Singleton (escopo Spring, bean imutável) | `ConfiguracoesFeiraViva` | ✅ Aula 10 |
| Factory (simple factory com switch Java 21) | `CupomFactory.criar(codigo)` | ✅ Aula 10 |
| Composite (variante uniforme) | Árvore de categorias — `GET /categorias/arvore` | ✅ Aula 11 |
| Strategy (contexto com `Map` injetado) | `CalculadoraFrete` — PADRAO, FIXO, RETIRADA | ✅ Aula 11 |

## 7. Estrutura de Pacotes

```text
feira-viva/
├── docs/
│   ├── modelo.md                  → este documento
│   └── openapi.json               → especificação exportada (Aula 12)
├── README.md                      → contrato público do backend (Aula 12)
├── frontend/                      → View (React + Vite) — a partir da Aula 17
└── src/main/java/br/com/feiraviva/
    ├── FeiravivaApplication.java
    ├── model/                     → Produto, Categoria (Composite), Cliente, Endereco,
    │   │                             Carrinho, ItemCarrinho, Pedido, ItemPedido,
    │   │                             PedidoStatus
    │   └── cupom/                 → Cupom (abstrata), CupomPercentual, CupomFixo  [Aula 10]
    ├── factory/                   → CupomFactory                                  [Aula 10]
    ├── strategy/                  → StrategyFrete, FretePadraoStrategy,
    │                                 FreteFixoStrategy, FreteRetiradaStrategy,
    │                                 CalculadoraFrete                              [Aula 11]
    ├── repository/                → ProdutoRepository, CategoriaRepository,
    │                                 ClienteRepository, CarrinhoRepository,
    │                                 PedidoRepository (+ itens)
    ├── service/                   → ProdutoService, ClienteService, CategoriaService,
    │                                 CarrinhoService, PedidoService (R1, R2, R4, R7)
    ├── controller/                → ProdutoController, CategoriaController,
    │                                 ClienteController, CarrinhoController,
    │                                 PedidoController (todos anotados p/ OpenAPI)
    ├── dto/                       → records de entrada/saída com @Schema
    │                                 (ItemCarrinhoDTO, CarrinhoResponseDTO c/ cupom,
    │                                 desconto e estrategiaFrete, CategoriaArvoreDTO,
    │                                 PedidoRequestDTO, PedidoResponseDTO, ...)
    ├── exception/                 → ResourceNotFoundException, RegraDeNegocioException,
    │                                 ApiErrorHandler (@RestControllerAdvice)
    └── config/                    → ConfiguracoesFeiraViva (singleton) [Aula 10],
                                      OpenApiConfig [Aula 12], DataSeeder, H2ConsoleConfig
```

## 8. Documentação e acesso (dev)

- **Swagger UI:** `http://localhost:8083/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8083/api-docs` (versão `0.9.0`)
- **H2 Console:** `http://localhost:8083/h2-console` (JDBC `jdbc:h2:mem:feiraviva`, user `sa`, senha vazia)
- **README do backend:** raiz do repositório (stack, como rodar, rotas, contrato de erros, GoF)
- **Especificação arquivada:** `docs/openapi.json` (consulta sem subir a aplicação)

> **Convenção de versionamento (registrada na Aula 12):** rotas futuras serão prefixadas com `/api/v1`; breaking changes exigirão `/api/v2` coexistindo durante período de depreciação. Implementação no Módulo 7 (deploy).

## 9. Iterações Futuras

- **Aula 13:** migrações com Flyway (substitui `ddl-auto=update`)
- **Aulas 14–15:** BCrypt + JWT (R3, R5); remoção do query param `clienteId`; Swagger protegido por perfil
- **Aula 17:** frontend React; cliente TypeScript gerado a partir do OpenAPI
- **Aula 23:** ViaCEP/Google Maps — nova estratégia `CEP` de frete e pontos de retirada
- **Aula 24:** Pagamento 1:1 com Pedido (sandbox); status `PAGO` deixa de ser manual
- **Módulo 7:** implementação da convenção `/api/v1`; desativação/proteção do Swagger em produção
- **Iterações:** cupom com validade/uso-único (tabela `cupons` + regras na fábrica), painel admin, acompanhamento de pedido, retirada na barraca, validação anti-ciclo na árvore de categorias

## 10. Histórico de Versões

| Versão | Aula | Mudança |
|---|---|---|
| `0.1` | 04 | Levantamento inicial: 5 entidades, 5 telas, 5 rotas, 3 regras e MVP |
| `0.2` | 05 | Modelo completo: 9 entidades, enums, regras R1–R5 e leitura MVC |
| `0.3` | 06 | Estrutura em código: pacote `br.com.feiraviva`, Java 21, Spring Boot 4.1.1, `GET /produtos` com mock |
| `0.4` | 07 | JPA/Hibernate: Produto e Categoria persistidos (H2), consultas derivadas |
| `0.5` | 08 | Cliente/Endereco, DTOs, validação (`jakarta.validation`) e erros centralizados (400/404/409) |
| `0.6` | 09 | Carrinho/Pedido: snapshot de preço, estoque (R1), frete provisório (R6), status e cancelamento (R4); dívida `clienteId` registrada |
| `0.7` | 10 | GoF: singleton `ConfiguracoesFeiraViva`, `CupomFactory` + hierarquia `Cupom` (value objects), cupom no carrinho (`codigo_cupom`, rotas POST/DELETE `/carrinho/cupom`), `CarrinhoResponseDTO` com `cupom`/`desconto`, regra R7, R2/R6 atualizadas, `/debug/instancias` provisório e dívidas registradas |
| `0.8` | 11 | GoF: Composite na árvore de categorias (`subcategorias`, `ehFolha()`, `GET /categorias/arvore`) e Strategy de frete (`StrategyFrete` + PADRAO/FIXO/RETIRADA + `CalculadoraFrete` com `Map` injetado); `estrategia_frete` no carrinho e limpeza na finalização; **R6 hardcoded aposentada**; seeder com subcategorias e produtos-folha |
