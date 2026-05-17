package com.eventpass.api.service;

import com.eventpass.api.dto.EventoRequest;
import com.eventpass.api.model.Evento;
import com.eventpass.api.repository.EventoRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventoService {
    private final EventoRepository eventos;

    public EventoService(EventoRepository eventos) {
        this.eventos = eventos;
    }

    public List<Evento> list() {
        List<Evento> ativos = eventos.findAll().stream()
                .filter(Evento::isAtivo)
                .sorted(Comparator.comparing(Evento::isDestaque).reversed().thenComparing(Evento::getId))
                .toList();
        List<Evento> unicos = new ArrayList<>();
        Set<String> nomes = new HashSet<>();
        for (Evento evento : ativos) {
            String chave = evento.getNome() == null ? "evento-" + evento.getId() : evento.getNome().trim().toLowerCase();
            if (nomes.add(chave)) {
                unicos.add(evento);
            }
        }
        return unicos;
    }

    public Evento get(Long id) {
        return eventos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado"));
    }

    public Evento create(EventoRequest request) {
        Evento evento = apply(new Evento(), request);
        if (evento.isDestaque()) {
            clearHighlights(null);
        }
        return eventos.save(evento);
    }

    public Evento update(Long id, EventoRequest request) {
        Evento evento = apply(get(id), request);
        if (evento.isDestaque()) {
            clearHighlights(id);
        }
        return eventos.save(evento);
    }

    public Evento highlight(Long id) {
        Evento evento = get(id);
        clearHighlights(id);
        evento.setDestaque(true);
        evento.setAtivo(true);
        return eventos.save(evento);
    }

    public Evento removeHighlight(Long id) {
        Evento evento = get(id);
        evento.setDestaque(false);
        return eventos.save(evento);
    }

    public void delete(Long id) {
        Evento evento = get(id);
        evento.setAtivo(false);
        evento.setDestaque(false);
        eventos.save(evento);
    }

    private Evento apply(Evento evento, EventoRequest request) {
        evento.setNome(request.nome());
        evento.setDescricao(request.descricao());
        evento.setLocal(request.local());
        evento.setDataEvento(request.dataEvento());
        evento.setHorario(request.horario());
        evento.setLimiteParticipantes(request.limiteParticipantes());
        evento.setBannerUrl(request.bannerUrl());
        if (request.ativo() != null) {
            evento.setAtivo(request.ativo());
        }
        if (request.destaque() != null) {
            evento.setDestaque(request.destaque());
        }
        return evento;
    }

    private void clearHighlights(Long keepId) {
        for (Evento evento : eventos.findAll()) {
            if (evento.isDestaque() && (keepId == null || !evento.getId().equals(keepId))) {
                evento.setDestaque(false);
                eventos.save(evento);
            }
        }
    }
}