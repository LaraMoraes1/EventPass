package com.eventpass.api.dto;

public record InscricaoResponse(Long id, Long usuarioId, Long eventoId, String eventoNome, String qrToken) {}
