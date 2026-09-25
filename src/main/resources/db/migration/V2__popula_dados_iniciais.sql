-- V2__popula_dados_iniciais.sql
-- Substitui o DataSeeder.java: seed de categorias, produtos e cliente de teste

-- Categorias raiz
INSERT INTO categorias (nome, descricao) VALUES
   ('Horta e Orgânicos', 'Verduras, legumes e temperos');
INSERT INTO categorias (nome, descricao) VALUES
   ('Laticínios', 'Queijos, iogurtes e manteigas');

-- Subcategorias (Composite)
INSERT INTO categorias (nome, descricao, categoria_pai_id) VALUES
   ('Verduras', 'Folhosas e legumes', 1);
INSERT INTO categorias (nome, descricao, categoria_pai_id) VALUES
   ('Temperos', 'Ervas e especiarias', 1);
INSERT INTO categorias (nome, descricao, categoria_pai_id) VALUES
   ('Queijos', 'Frescos e curados', 2);

-- Produtos (associados às folhas)
INSERT INTO produtos (nome, descricao, preco, sku, estoque, categoria_id) VALUES
    ('Mel orgânico',   'Pote de 500ml — apiário da serra',35.00, 'MEL-001', 12, 1),
    ('Alface crespa',  'Hidropônica — 1 maço',4.50, 'ALF-004', 30, 3),
    ('Queijo minas',   'Frescal 500g',28.50, 'QUE-002',  8, 5),
    ('Iogurte natural','Pote de 200g',9.90, 'IOG-005', 18, 5),
    ('Café da serra',  'Torrado 500g',42.00, 'CAF-003', 20, 1);

-- Cliente de teste (senha provisória — Aula 15 fará hash BCrypt)
INSERT INTO clientes (nome, email, senha_hash, telefone, papel) VALUES
    ('Maria Souza', 'maria@email.com', '123456', '(11) 98888-7777', 'CLIENTE');

-- Endereço do cliente de teste
INSERT INTO enderecos (cep, logradouro, numero, complemento, bairro, cidade, uf, cliente_id) VALUES
    ('01310100', 'Av. Paulista', '1000', 'Apto 42', 'Bela Vista', 'São Paulo', 'SP', 1);