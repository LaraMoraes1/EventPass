package com.eventpass.mobile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.AccessLog;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class HistoryActivity extends Activity {
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_list);
        list = findViewById(R.id.listContainer);
        loadHistory();
    }

    private void loadHistory() {
        list.removeAllViews();
        title(list, "Historico de acessos", "Entradas, saidas e criterios de certificado");
        ApiClient.get().history().enqueue(new Ui<List<AccessLog>>(this) {
            @Override public void onOk(List<AccessLog> logs) {
                for (AccessLog log : logs) {
                    addLog(list, log);
                }
            }
        });
    }

    private void title(LinearLayout list, String text, String subtitle) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(0xFFFFFFFF);
        view.setTextSize(28);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setPadding(0, 0, 0, 4);
        list.addView(view);

        TextView sub = new TextView(this);
        sub.setText(subtitle);
        sub.setTextColor(0xFFA8B3BD);
        sub.setTextSize(14);
        sub.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        sub.setPadding(0, 0, 0, 20);
        list.addView(sub);
    }

    private void addLog(LinearLayout list, AccessLog log) {
        String participante = log.inscricao == null || log.inscricao.usuario == null ? "Participante" : log.inscricao.usuario.nome;
        String evento = log.inscricao == null || log.inscricao.evento == null ? "Evento" : log.inscricao.evento.nome;
        String entrada = formatDate(log.entradaEm);
        String saida = log.saidaEm == null ? "Ainda presente" : formatDate(log.saidaEm);
        String tempo = log.permanenciaMinutos == null ? "-" : log.permanenciaMinutos + " min";

        View view = LayoutInflater.from(this).inflate(R.layout.item_access_log, list, false);
        ((TextView) view.findViewById(R.id.statusChip)).setText(statusLabel(log.status));
        ((TextView) view.findViewById(R.id.logPerson)).setText(participante);
        ((TextView) view.findViewById(R.id.logEvent)).setText(evento);
        ((TextView) view.findViewById(R.id.logTimes)).setText("Entrada: " + entrada + "\nSaida: " + saida + "\nPermanencia: " + tempo);
        ((TextView) view.findViewById(R.id.logCertificate)).setText(certificateMessage(log.permanenciaMinutos));
        ((MaterialButton) view.findViewById(R.id.moreButton)).setOnClickListener(v -> confirmRemove(log));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 14);
        view.setLayoutParams(params);
        list.addView(view);
    }

    private void confirmRemove(AccessLog log) {
        new AlertDialog.Builder(this)
                .setTitle("Remover acesso")
                .setMessage("Remover este registro do historico? Use isso apenas para corrigir um teste ou demonstracao.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Remover", (dialog, which) -> removeAccess(log.id))
                .show();
    }

    private void removeAccess(Long id) {
        ApiClient.get().deleteAccess(id).enqueue(new Ui<Void>(this) {
            @Override public void onOk(Void data) {
                Toast.makeText(HistoryActivity.this, "Registro removido", Toast.LENGTH_LONG).show();
                loadHistory();
            }
        });
    }

    private String statusLabel(String status) {
        if ("SAIDA".equals(status)) {
            return "SAIDA REGISTRADA";
        }
        if ("ENTRADA".equals(status)) {
            return "PRESENTE AGORA";
        }
        return "ACESSO";
    }

    private String certificateMessage(Long minutes) {
        if (minutes == null) {
            return "Certificado pendente: saida ainda nao registrada.";
        }
        if (minutes >= 60) {
            return "Certificado liberado.";
        }
        return "Certificado nao liberado: permanencia menor que 60 min.";
    }

    private String formatDate(String value) {
        if (value == null || value.length() < 19) {
            return "-";
        }
        String clean = value.substring(0, 19).replace("T", " ");
        String[] parts = clean.split(" ");
        if (parts.length != 2) {
            return clean;
        }
        String[] date = parts[0].split("-");
        if (date.length != 3) {
            return clean;
        }
        return date[2] + "/" + date[1] + "/" + date[0] + " " + parts[1];
    }
}