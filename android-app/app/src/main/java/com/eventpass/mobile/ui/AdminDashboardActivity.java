package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.DashboardStats;
import com.google.android.material.button.MaterialButton;

public class AdminDashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_dashboard);
        ((TextView) findViewById(R.id.title)).setText("Dashboard administrativo");
        ((TextView) findViewById(R.id.subtitle)).setText("Controle de presenca em tempo real");
        ((MaterialButton) findViewById(R.id.actionOne)).setText("Scanner");
        ((MaterialButton) findViewById(R.id.actionTwo)).setText("Historico de acessos");
        ((MaterialButton) findViewById(R.id.actionThree)).setText("Gerenciar eventos");
        MaterialButton actionFour = findViewById(R.id.actionFour);
        actionFour.setVisibility(View.VISIBLE);
        actionFour.setText("Criar evento");
        MaterialButton actionFive = findViewById(R.id.actionFive);
        actionFive.setVisibility(View.VISIBLE);
        actionFive.setText("Cadastrar equipe");
        findViewById(R.id.actionOne).setOnClickListener(v -> startActivity(new Intent(this, ScannerActivity.class)));
        findViewById(R.id.actionTwo).setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.actionThree).setOnClickListener(v -> startActivity(new Intent(this, EventListActivity.class)));
        actionFour.setOnClickListener(v -> startActivity(new Intent(this, EventFormActivity.class)));
        actionFive.setOnClickListener(v -> startActivity(new Intent(this, TeamRegisterActivity.class)));

        ApiClient.get().dashboard().enqueue(new Ui<DashboardStats>(this) {
            @Override public void onOk(DashboardStats s) {
                LinearLayout box = findViewById(R.id.statsBox);
                box.removeAllViews();
                metric(box, String.valueOf(s.participantes), "Participantes cadastrados", "Usuarios registrados no EventPass");
                metric(box, String.valueOf(s.presentes), "Presentes agora", "Pessoas com entrada aberta no evento");
                metric(box, String.valueOf(s.eventosAtivos), "Eventos ativos", "Eventos disponiveis para inscricao");
                metric(box, String.valueOf(s.acessosRegistrados), "Acessos registrados", "Entradas e saidas controladas por QR");
                metric(box, "60 min", "Regra de certificado", "Certificado liberado ao atingir a permanencia minima");
                metric(box, "#1", "Evento em destaque", s.eventoDestaque == null ? "Nenhum evento definido" : s.eventoDestaque);
            }
        });
    }

    private void metric(LinearLayout parent, String value, String label, String hint) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_metric, parent, false);
        ((TextView) view.findViewById(R.id.metricValue)).setText(value);
        ((TextView) view.findViewById(R.id.metricLabel)).setText(label);
        ((TextView) view.findViewById(R.id.metricHint)).setText(hint);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 12);
        view.setLayoutParams(params);
        parent.addView(view);
    }
}
