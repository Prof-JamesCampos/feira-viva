-- V3__ajustes_indices.sql
-- Índices e constraints adicionais para desempenho

-- Busca por produto ativo
CREATE INDEX idx_produto_ativo ON produtos(ativo);

-- Busca por nome de produto (LIKE prefix)
CREATE INDEX idx_produto_nome ON produtos(nome);

-- Histórico recente de pedidos por cliente
CREATE INDEX idx_pedido_data ON pedidos(data_criacao DESC);

-- Constraint de estoque não-negativo
ALTER TABLE produtos ADD CONSTRAINT ck_produto_estoque CHECK (estoque >= 0);