package com.eventpass.api.controller;

import com.eventpass.api.dto.EventoRequest;
import com.eventpass.api.model.Evento;
import com.eventpass.api.service.EventoService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventoController {
    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Evento> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Evento get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public Evento create(@RequestBody EventoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Evento update(@PathVariable Long id, @RequestBody EventoRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/highlight")
    public Evento highlight(@PathVariable Long id) {
        return service.highlight(id);
    }

    @DeleteMapping("/{id}/highlight")
    public Evento removeHighlight(@PathVariable Long id) {
        return service.removeHighlight(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}