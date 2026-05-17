package com.eventpass.api.repository;

import com.eventpass.api.model.Evento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    long countByAtivoTrue();
    Optional<Evento> findFirstByDestaqueTrueAndAtivoTrueOrderByCriadoEmDesc();
}