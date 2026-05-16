package com.eventpass.api.repository;

import com.eventpass.api.model.Evento;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    long countByAtivoTrue();
    List<Evento> findByAtivoTrue();
    Optional<Evento> findFirstByDestaqueTrueAndAtivoTrueOrderByCriadoEmDesc();
}