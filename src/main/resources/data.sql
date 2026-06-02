-- Seed inicial de propriedades de exemplo (issue #5).
-- Executa a cada start (banco H2 em memória é recriado). Não informamos o id:
-- a coluna é IDENTITY e o valor é gerado automaticamente.

INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('João Silva',      'Soja',    -21.1767, -47.8208, 120.5, 'Ribeirão Preto',  'SP');

INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Maria Oliveira',  'Milho',   -16.6869, -49.2648, 340.0, 'Goiânia',         'GO');

INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Carlos Pereira',  'Café',    -21.7642, -43.3503,  85.2, 'Juiz de Fora',    'MG');

INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Ana Souza',       'Algodão', -12.6819, -56.0949, 500.0, 'Sorriso',         'MT');

-- ===== Pontos extras para testar o endpoint /clima =====
-- id 5: semiárido irrigado (Petrolina) — quente; clima real sem alerta (exemplo normal)
INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Fazenda Velho Chico', 'Manga', -9.3891, -40.5030,  60.0, 'Petrolina', 'PE');

-- id 6: deserto do Atacama — precipitação quase nula → dispara AlertaSeca
INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Teste Seca (Atacama)',  'Teste', -24.5000, -69.2500, 10.0, 'Atacama',   'EX');

-- id 7: latitude polar — temperatura média negativa → dispara AlertaGeada
INSERT INTO propriedade (produtor, cultura, latitude, longitude, area_ha, municipio, uf)
VALUES ('Teste Geada (Polar)',   'Teste', -62.0000, -58.4000, 10.0, 'Antártica', 'EX');
