package com.eventpass.api.controller;

import com.eventpass.api.dto.InscricaoResponse;
import com.eventpass.api.service.InscricaoService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class InscricaoController {
    private final InscricaoService service;

    public InscricaoController(InscricaoService service) {
        this.service = service;
    }

    @PostMapping
    public InscricaoResponse subscribe(@RequestParam Long usuarioId, @RequestParam Long eventoId) {
        return service.subscribe(usuarioId, eventoId);
    }

    @GetMapping("/user/{usuarioId}")
    public List<InscricaoResponse> byUser(@PathVariable Long usuarioId) {
        return service.byUser(usuarioId);
    }
}
