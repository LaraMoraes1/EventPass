package com.eventpass.api.service;

import com.eventpass.api.dto.*;
import com.eventpass.api.model.*;
import com.eventpass.api.repository.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {
    private final AcessoRepository acessos;
    private final InscricaoRepository inscricoes;
    private final UsuarioRepository usuarios;
    private final EventoRepository eventos;

    public AcessoService(AcessoRepository acessos, InscricaoRepository inscricoes, UsuarioRepository usuarios, EventoRepository eventos) {
        this.acessos = acessos;
        this.inscricoes = inscricoes;
        this.usuarios = usuarios;
        this.eventos = eventos;
    }

    public ScanResponse scan(String qrToken) {
        return inscricoes.findByQrToken(qrToken).map(inscricao -> {
            LocalDateTime now = LocalDateTime.now();
            return acessos.findFirstByInscricaoIdAndSaidaEmIsNullOrderByEntradaEmDesc(inscricao.getId()).map(aberto -> {
                aberto.setSaidaEm(now);
                aberto.setPermanenciaMinutos(Duration.between(aberto.getEntradaEm(), now).toMinutes());
                aberto.setStatus("SAIDA");
                Acesso salvo = acessos.save(aberto);
                return response(true, "SAIDA", "Saída registrada", inscricao, salvo);
            }).orElseGet(() -> {
                Acesso acesso = new Acesso();
                acesso.setInscricao(inscricao);
                acesso.setEntradaEm(now);
                acesso.setStatus("ENTRADA");
                Acesso salvo = acessos.save(acesso);
                return response(true, "ENTRADA", "Entrada liberada", inscricao, salvo);
            });
        }).orElse(new ScanResponse(false, "INVALIDO", "QR inválido", null, null, null, null, null));
    }

    public List<Acesso> history() {
        return acessos.findTop40ByOrderByCriadoEmDesc();
    }

    public DashboardResponse dashboard() {
        return new DashboardResponse(usuarios.count(), acessos.countPresentes(), eventos.countByAtivoTrue(), acessos.count());
    }

    private ScanResponse response(boolean sucesso, String status, String mensagem, Inscricao inscricao, Acesso acesso) {
        return new ScanResponse(
                sucesso,
                status,
                mensagem,
                inscricao.getUsuario().getNome(),
                inscricao.getEvento().getNome(),
                acesso.getEntradaEm(),
                acesso.getSaidaEm(),
                acesso.getPermanenciaMinutos()
        );
    }
}
