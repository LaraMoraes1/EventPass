package com.eventpass.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "acessos")
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "inscricao_id")
    private Inscricao inscricao;
    private LocalDateTime entradaEm;
    private LocalDateTime saidaEm;
    private Long permanenciaMinutos;
    private String status;
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Inscricao getInscricao() { return inscricao; }
    public void setInscricao(Inscricao inscricao) { this.inscricao = inscricao; }
    public LocalDateTime getEntradaEm() { return entradaEm; }
    public void setEntradaEm(LocalDateTime entradaEm) { this.entradaEm = entradaEm; }
    public LocalDateTime getSaidaEm() { return saidaEm; }
    public void setSaidaEm(LocalDateTime saidaEm) { this.saidaEm = saidaEm; }
    public Long getPermanenciaMinutos() { return permanenciaMinutos; }
    public void setPermanenciaMinutos(Long permanenciaMinutos) { this.permanenciaMinutos = permanenciaMinutos; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
