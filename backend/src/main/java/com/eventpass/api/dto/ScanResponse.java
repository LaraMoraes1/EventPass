package com.eventpass.api.dto;

import java.time.LocalDateTime;

public record ScanResponse(
        boolean sucesso,
        String status,
        String mensagem,
        String participante,
        String evento,
        LocalDateTime entradaEm,
        LocalDateTime saidaEm,
        Long permanenciaMinutos
) {}
