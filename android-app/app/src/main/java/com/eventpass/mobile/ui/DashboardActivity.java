package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eventpass.mobile.R;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_dashboard);

        ((TextView) findViewById(R.id.title)).setText("EventPass");
        ((TextView) findViewById(R.id.subtitle)).setText("Ola, " + Session.name(this));

        LinearLayout stats = findViewById(R.id.statsBox);
        stats.removeAllViews();

        MaterialButton one = findViewById(R.id.actionOne);
        MaterialButton two = findViewById(R.id.actionTwo);
        MaterialButton three = findViewById(R.id.actionThree);
        if (Session.isAdmin(this)) {
            addStat(stats, "Administrador do evento\nGerencie equipe, eventos e acessos");
            addStat(stats, "Credenciamento em tempo real\nScanner pronto para entrada e saida");
            addStat(stats, "Certificados por permanencia\nLiberacao automatica a partir de 60 min");
            one.setText("Scanner de QR Code");
            two.setText("Dashboard administrativo");
            three.setText("Gerenciar eventos");
            one.setOnClickListener(v -> startActivity(new Intent(this, ScannerActivity.class)));
            two.setOnClickListener(v -> startActivity(new Intent(this, AdminDashboardActivity.class)));
            three.setOnClickListener(v -> startActivity(new Intent(this, EventListActivity.class)));
        } else {
            addStat(stats, "Perfil: Participante");
            addStat(stats, "QR unico, online e validado em tempo real");
            addStat(stats, "Badge digital pronto para eventos");
            one.setText("Ver eventos");
            two.setText("Meu QR Code");
            three.setText("Perfil");
            one.setOnClickListener(v -> startActivity(new Intent(this, EventListActivity.class)));
            two.setOnClickListener(v -> startActivity(new Intent(this, QrCodeActivity.class)));
            three.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        }
    }

    private void addStat(LinearLayout parent, String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(0xFFF5F7FA);
        view.setTextSize(16);
        view.setBackgroundResource(R.drawable.metric_bg);
        view.setPadding(18, 14, 18, 14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 10);
        view.setLayoutParams(params);
        parent.addView(view);
    }
}
