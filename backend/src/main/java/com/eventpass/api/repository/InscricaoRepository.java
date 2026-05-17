package com.eventpass.api.repository;

import com.eventpass.api.model.Inscricao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    Optional<Inscricao> findByQrToken(String qrToken);
    Optional<Inscricao> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);
    List<Inscricao> findByUsuarioIdOrderByIdDesc(Long usuarioId);
    long countByEventoId(Long eventoId);
}
