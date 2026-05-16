package com.eventpass.api.repository;

import com.eventpass.api.model.Acesso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AcessoRepository extends JpaRepository<Acesso, Long> {
    Optional<Acesso> findFirstByInscricaoIdAndSaidaEmIsNullOrderByEntradaEmDesc(Long inscricaoId);
    List<Acesso> findTop40ByOrderByCriadoEmDesc();

    @Query("select count(a) from Acesso a where a.entradaEm is not null and a.saidaEm is null")
    long countPresentes();
}
