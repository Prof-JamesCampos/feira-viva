# 🛒 Feira Viva

**Loja online de produtos locais** — projeto de extensão do programa *Fábrica de Software*.

> Monólito modular baseado em MVC: **Spring Boot 4.1.1 (Java 21 LTS)** no backend e **React (Vite)** no frontend.

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-green) ![React](https://img.shields.io/badge/React-19-blue) ![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

---

## 📖 Sobre o projeto

O **Feira Viva** nasce de um problema real: os produtores da associação local vendem apenas na feira presencial, e a comunidade não consegue comprar fora do horário da feira. O projeto leva a feira para a internet, com catálogo de produtos, carrinho de compras, pedidos e pagamento simulado.

Desenvolvido na disciplina **Desenvolvimento para Servidores II (IDS002)** do curso **Tecnologia em Sistemas para Internet** (4º semestre / 2026), em parceria com a **Associação de Produtores Locais**.

### MVP

Compra de ponta a ponta: catálogo → detalhe do produto → carrinho → login/cadastro → endereço de entrega → confirmação do pedido (pagamento simulado).

---

## 🚀 Tecnologias

| Camada | Tecnologias |
|---|---|
| **Backend** | Java 21 (LTS) · Spring Boot 4.1.1 · Spring MVC · Spring Data JPA · Spring Security · JWT · Flyway |
| **Frontend** | React · Vite · Axios · React Hook Form · React Router |
| **Build / Deps** | Maven |
| **Docs / Testes** | Swagger/OpenAPI · Spring Boot Test · MockMvc · React Testing Library · Cypress |
| **Ferramentas** | Git/GitHub (GitFlow, PRs, code review) · Docker |

---

## ✅ Requisitos

- JDK **21+**
- Maven **3.9+**
- Node **20+** (frontend)
- Git

---

## 📁 Estrutura do projeto

```text
feira-viva/
├── docs/
│   └── modelo.md              → documento vivo do modelo de domínio
├── frontend/                  → View (React + Vite)
├── src/
│   ├── main/
│   │   ├── java/br/com/feiraviva/
│   │   │   ├── FeiravivaApplication.java
│   │   │   ├── model/         → entidades e enums
│   │   │   ├── repository/    → acesso a dados (Spring Data JPA)
│   │   │   ├── service/       → regras de negócio
│   │   │   └── controller/    → rotas (Spring MVC)
│   │   └── resources/
│   │       └── application.properties
│   └── test/                  → testes de integração
├── .gitignore
├── pom.xml
└── README.md
```

---

## ▶️ Como executar

### Backend

```bash
git clone https://github.com/<sua-equipe>/feira-viva.git
cd feira-viva
./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Interface disponível em `http://localhost:5173`.

---

## 🔌 Principais endpoints

| Rota | Método | Finalidade | Acesso |
|---|---|---|---|
| `/produtos` | GET | Listar com filtros | Público |
| `/produtos/{id}` | GET | Detalhar produto | Público |
| `/categorias` | GET | Listar categorias | Público |
| `/auth/login` | POST | Autenticar (JWT) | Público |
| `/auth/registro` | POST | Cadastrar cliente | Público |
| `/carrinho` | GET | Exibir carrinho | Autenticado |
| `/carrinho/itens` | POST | Adicionar item | Autenticado |
| `/carrinho/itens/{id}` | PUT / DELETE | Alterar/remover item | Autenticado |
| `/pedidos` | POST | Finalizar pedido | Autenticado |
| `/pedidos` | GET | Histórico do cliente | Autenticado |
| `/pedidos/{id}` | GET | Detalhar pedido | Autenticado |
| `/admin/produtos` | POST/PUT/DELETE | Gerenciar produtos | ADMIN |
| `/admin/pedidos/{id}/status` | PUT | Atualizar status | ADMIN |

Documentação interativa (Swagger): `http://localhost:8080/swagger-ui.html` *(disponível a partir do Módulo 2)*.

---

## 📐 Modelo de domínio

O modelo completo (entidades, relacionamentos, enums, regras de negócio e leitura MVC) está em **[docs/modelo.md](docs/modelo.md)**.

**Entidades:** Produto · Categoria · Cliente · Endereco · Carrinho · ItemCarrinho · Pedido · ItemPedido · Pagamento

**Regras de negócio:**

| ID | Regra |
|---|---|
| R1 | Produto com estoque zero não entra no carrinho; quantidade nunca excede o estoque |
| R2 | Total do pedido = soma dos itens + frete calculado pelo CEP |
| R3 | Pedido exige cliente autenticado; rotas `/admin/**` exigem papel ADMIN |
| R4 | Cancelamento pelo cliente só com status CRIADO |
| R5 | Senhas armazenadas com hash; autenticação via JWT |

---

## 🔗 Integrações externas *(Módulo 5)*

- **ViaCEP** — autocomplete de endereços
- **Google Maps / Geocoding** — frete por região e pontos de retirada
- **Stripe / Mercado Pago (sandbox)** — pagamento simulado
- **Login social** — Google / GitHub

---

## 🧪 Testes

```bash
./mvnw test          # backend (Spring Boot Test + MockMvc)
cd frontend && npm test   # frontend (React Testing Library)
```

Testes E2E do fluxo de compra com Cypress *(Módulo 6)*.

---

## 🌿 Workflow Git

- `main` — branch estável
- `feature/<nome>` — novas funcionalidades (ex.: `feature/carrinho`)
- Todo merge passa por **Pull Request + code review**
- Mensagens de commit no padrão Conventional Commits: `feat:`, `fix:`, `docs:`, `test:`

---

## 🗺️ Roadmap do semestre

| Módulo | Foco | Aulas |
|---|---|---|
| 1 | Fundamentos, Git e arquitetura MVC | 1–5 |
| 2 | Backend monolítico com Spring MVC | 6–12 |
| 3 | Banco de dados e segurança | 13–16 |
| 4 | View com React | 17–22 |
| 5 | Integração com sistemas externos | 23–26 |
| 6 | Documentação e testes | 27–32 |
| 7 | Finalização e deploy | 33–40 |

---

## 🤝 Créditos

Projeto desenvolvido pelas equipes do **Fábrica de Software** — disciplina Desenvolvimento para Servidores II (IDS002), sob orientação do **Prof. James Campos**, com apoio da **Associação de Produtores Locais**.

## 📄 Licença

Projeto acadêmico de extensão — uso educacional e comunitário.