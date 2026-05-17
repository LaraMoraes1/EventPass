package com.eventpass.api.service;

import com.eventpass.api.dto.InscricaoResponse;
import com.eventpass.api.model.*;
import com.eventpass.api.repository.*;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InscricaoService {
    private final InscricaoRepository inscricoes;
    private final UsuarioRepository usuarios;
    private final EventoRepository eventos;

    public InscricaoService(InscricaoRepository inscricoes, UsuarioRepository usuarios, EventoRepository eventos) {
        this.inscricoes = inscricoes;
        this.usuarios = usuarios;
        this.eventos = eventos;
    }

    public InscricaoResponse subscribe(Long usuarioId, Long eventoId) {
        return inscricoes.findByUsuarioIdAndEventoId(usuarioId, eventoId).map(this::toResponse).orElseGet(() -> {
            Usuario usuario = usuarios.findById(usuarioId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
            Evento evento = eventos.findById(eventoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
            if (inscricoes.countByEventoId(eventoId) >= evento.getLimiteParticipantes()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limite de participantes atingido");
            }
            Inscricao inscricao = new Inscricao();
            inscricao.setUsuario(usuario);
            inscricao.setEvento(evento);
            inscricao.setQrToken("EVP-" + UUID.randomUUID());
            try {
                return toResponse(inscricoes.save(inscricao));
            } catch (DataIntegrityViolationException ex) {
                return inscricoes.findByUsuarioIdAndEventoId(usuarioId, eventoId)
                        .map(this::toResponse)
                        .orElseThrow(() -> ex);
            }
        });
    }

    public List<InscricaoResponse> byUser(Long usuarioId) {
        return inscricoes.findByUsuarioIdOrderByIdDesc(usuarioId).stream().map(this::toResponse).toList();
    }

    private InscricaoResponse toResponse(Inscricao inscricao) {
        return new InscricaoResponse(
                inscricao.getId(),
                inscricao.getUsuario().getId(),
                inscricao.getEvento().getId(),
                inscricao.getEvento().getNome(),
                inscricao.getQrToken()
        );
    }
}
