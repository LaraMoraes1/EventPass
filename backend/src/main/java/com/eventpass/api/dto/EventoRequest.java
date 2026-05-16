package com.eventpass.api.dto;

import java.time.LocalDate;

public record EventoRequest(
        String nome,
        String descricao,
        String local,
        LocalDate dataEvento,
        String horario,
        Integer limiteParticipantes,
        String bannerUrl,
        Boolean ativo,
        Boolean destaque
) {}
