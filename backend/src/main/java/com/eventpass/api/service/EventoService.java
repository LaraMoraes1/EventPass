package com.eventpass.api.service;

import com.eventpass.api.dto.EventoRequest;
import com.eventpass.api.model.Evento;
import com.eventpass.api.repository.EventoRepository;
import java.util.List;
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
        return eventos.findAll();
    }

    public Evento get(Long id) {
        return eventos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado"));
    }

    public Evento create(EventoRequest request) {
        return eventos.save(apply(new Evento(), request));
    }

    public Evento update(Long id, EventoRequest request) {
        return eventos.save(apply(get(id), request));
    }

    public void delete(Long id) {
        Evento evento = get(id);
        evento.setAtivo(false);
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
        return evento;
    }
}
