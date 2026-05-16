package com.eventpass.api.controller;

import com.eventpass.api.dto.*;
import com.eventpass.api.model.Acesso;
import com.eventpass.api.service.AcessoService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class AcessoController {
    private final AcessoService service;

    public AcessoController(AcessoService service) {
        this.service = service;
    }

    @PostMapping("/scan")
    public ScanResponse scan(@RequestBody ScanRequest request) {
        return service.scan(request.qrToken());
    }

    @GetMapping("/history")
    public List<Acesso> history() {
        return service.history();
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return service.dashboard();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }}
