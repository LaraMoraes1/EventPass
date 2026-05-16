CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(160) UNIQUE NOT NULL,
    senha VARCHAR(120) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ADMIN', 'PARTICIPANTE')),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS eventos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(160) NOT NULL,
    descricao TEXT NOT NULL,
    local VARCHAR(160) NOT NULL,
    data_evento DATE NOT NULL,
    horario VARCHAR(20) NOT NULL,
    limite_participantes INTEGER NOT NULL,
    banner_url TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    destaque BOOLEAN NOT NULL DEFAULT FALSE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inscricoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    evento_id BIGINT NOT NULL REFERENCES eventos(id) ON DELETE CASCADE,
    qr_token VARCHAR(80) UNIQUE NOT NULL,
    criada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (usuario_id, evento_id)
);

CREATE TABLE IF NOT EXISTS acessos (
    id BIGSERIAL PRIMARY KEY,
    inscricao_id BIGINT NOT NULL REFERENCES inscricoes(id) ON DELETE CASCADE,
    entrada_em TIMESTAMP,
    saida_em TIMESTAMP,
    permanencia_minutos BIGINT,
    status VARCHAR(30) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuarios (nome, email, senha, tipo) VALUES
('Administrador EventPass', 'admin@eventpass.com', '123456', 'ADMIN'),
('Ana Participante', 'ana@eventpass.com', '123456', 'PARTICIPANTE')
ON CONFLICT (email) DO NOTHING;

INSERT INTO eventos (nome, descricao, local, data_evento, horario, limite_participantes, banner_url, destaque) VALUES
('Tech Talks 2026', 'Palestras sobre mobile, cloud e inteligência artificial.', 'Auditório Principal', '2026-06-20', '19:30', 120, 'https://images.unsplash.com/photo-1540575467063-178a50c2df87', TRUE),
('Semana de Inovação', 'Evento com workshops, networking e demonstrações.', 'Campus Bloco B', '2026-07-04', '09:00', 80, 'https://images.unsplash.com/photo-1511578314322-379afb476865', FALSE)
ON CONFLICT DO NOTHING;

