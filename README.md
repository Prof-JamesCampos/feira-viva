# 🛒 Feira Viva — Backend API

**E-commerce de produtos locais** — backend monolítico MVC em Spring Boot.

`Java 21 LTS` · `Spring Boot 4.1.1` · `Módulo 2 concluído`

## Sobre

Backend do projeto Feira Viva, desenvolvido na disciplina **Desenvolvimento para
Servidores II** em parceria com a Associação de Produtores Locais.
Resolve um problema real: levar a feira presencial para a internet, permitindo
compra de ponta a ponta.

**Stack:** Java 21 LTS · Spring Boot 4.1.1 · Maven · H2 (dev) · Jakarta EE 11
**Pacote base:** `br.com.feiraviva` · **Porta:** 8083

## Como rodar

    git clone https://github.com/<equipe>/feira-viva.git
    cd feira-viva
    ./mvnw spring-boot:run

| Recurso | URL |
|---|---|
| API | http://localhost:8083 |
| Swagger UI | http://localhost:8083/swagger-ui.html |
| OpenAPI JSON | http://localhost:8083/api-docs |
| H2 Console | http://localhost:8083/h2-console (`jdbc:h2:mem:feiraviva`, user `sa`, senha vazia) |

## Rotas principais

| Grupo | Rotas |
|---|---|
| Catálogo | `GET /produtos` · `GET /produtos/{id}` · `GET /categorias/arvore` |
| Clientes | `POST /clientes` · `GET /clientes/{id}` · `POST /clientes/{id}/enderecos` |
| Carrinho | `GET /carrinho` · `POST/PUT/DELETE /carrinho/itens...` · `POST/DELETE /carrinho/cupom` · `POST /carrinho/frete` |
| Pedidos | `POST /pedidos` · `GET /pedidos` · `GET /pedidos/{id}` · `POST /pedidos/{id}/cancelamento` |

Contrato completo, com exemplos e "Try it out", no **Swagger UI**.

## Contrato de erros

| Situação | Status | Corpo |
|---|---|---|
| DTO inválido (`@Valid`) | 400 | `{"erro":"validacao","mensagem":"campo: msg | ..."}` |
| Recurso inexistente (produto, cliente, endereço, cupom, estratégia, pedido) | 404 | `{"erro":"nao_encontrado","mensagem":"..."}` |
| Regra de negócio (estoque, e-mail duplicado, cancelamento) | 409 | `{"erro":"regra_de_negocio","mensagem":"..."}` |
| JSON malformado | 400 | `{"erro":"json_invalido","mensagem":"..."}` |

## Padrões GoF aplicados

| Padrão | Implementação |
|---|---|
| Singleton (escopo Spring) | `ConfiguracoesFeiraViva` (imutável) |
| Factory | `CupomFactory` (FEIRA10, BEMVINDO) |
| Composite | Árvore de categorias (`GET /categorias/arvore`) |
| Strategy | `CalculadoraFrete` (PADRAO, FIXO, RETIRADA) |

## Regras de negócio

- **R1** — estoque verificado ao adicionar e ao finalizar; devolvido no cancelamento
- **R2** — total = subtotal + frete (Strategy) − desconto do cupom
- **R4** — cancelamento só com status `CRIADO`
- **R7** — desconto fixo nunca supera o subtotal
- **R6** — hardcoded aposentada na Aula 11; comportamento preservado na estratégia `PADRAO`

## Documentação de domínio

O contrato interno (entidades, regras, status de implementação) está em
[`docs/modelo.md`](docs/modelo.md). A especificação OpenAPI arquivada está em
`docs/openapi.json`.

## Próximos passos (Módulo 3 em diante)

- Migrações de banco com **Flyway** (substitui `ddl-auto=update`)
- **Spring Security + JWT** (remove o `?clienteId=` das rotas)
- Frontend **React** (Módulo 4)
- **ViaCEP/Maps** e sandbox de pagamento (Módulo 5)
- Deploy em produção com convenção `/api/v1` (Módulo 7)

## Licença

Projeto acadêmico de extensão — uso educacional e comunitário.