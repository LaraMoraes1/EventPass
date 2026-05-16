package com.eventpass.api.dto;

public record DashboardResponse(long participantes, long presentes, long eventosAtivos, long acessosRegistrados, String eventoDestaque) {}