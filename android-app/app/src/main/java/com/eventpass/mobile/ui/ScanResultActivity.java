package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eventpass.mobile.R;
import com.google.android.material.button.MaterialButton;

public class ScanResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_scan_result);

        boolean success = getIntent().getBooleanExtra("success", false);
        String status = text("status", "INVALIDO");
        String message = text("message", "QR invalido");
        String person = text("person", "Participante nao encontrado");
        String event = text("event", "Evento nao identificado");
        String entry = formatDate(text("entry", null));
        String exit = formatDate(text("exit", null));
        String stay = text("stay", "-");

        LinearLayout resultCard = findViewById(R.id.resultCard);
        TextView title = findViewById(R.id.resultTitle);
        TextView statusText = findViewById(R.id.statusText);
        TextView personText = findViewById(R.id.personText);
        TextView eventText = findViewById(R.id.eventText);
        TextView timeText = findViewById(R.id.timeText);
        MaterialButton scanAgain = findViewById(R.id.scanAgainButton);
        MaterialButton dashboard = findViewById(R.id.dashboardButton);

        resultCard.setBackgroundResource(success ? R.drawable.status_success_bg : R.drawable.status_error_bg);
        title.setText(success ? "Acesso confirmado" : "Acesso negado");
        statusText.setText(message);
        personText.setText(person);
        eventText.setText(event);
        timeText.setText(buildTimeText(status, entry, exit, stay));

        scanAgain.setOnClickListener(v -> {
            startActivity(new Intent(this, ScannerActivity.class));
            finish();
        });
        dashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        });
    }

    private String text(String key, String fallback) {
        String value = getIntent().getStringExtra(key);
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private String buildTimeText(String status, String entry, String exit, String stay) {
        if ("SAIDA".equals(status)) {
            return "Entrada: " + entry
                    + "\nSaida: " + exit
                    + "\nPermanencia: " + stay + " min"
                    + "\n\n" + certificateMessage(stay);
        }
        if ("ENTRADA".equals(status)) {
            return "Entrada: " + entry + "\nStatus atual: presente no evento";
        }
        return "Nenhum acesso foi registrado para este QR Code.";
    }

    private String certificateMessage(String stay) {
        int minutes = parseMinutes(stay);
        if (minutes >= 60) {
            return "Certificado liberado: permanencia minima atingida.";
        }
        int missing = 60 - Math.max(minutes, 0);
        return "Certificado nao liberado: faltaram " + missing + " min para atingir 60 min.";
    }

    private int parseMinutes(String stay) {
        try {
            return Integer.parseInt(stay);
        } catch (Exception e) {
            return 0;
        }
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
