package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eventpass.mobile.R;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_dashboard);
        ((TextView) findViewById(R.id.title)).setText("Perfil");
        ((TextView) findViewById(R.id.subtitle)).setText("Conta conectada ao EventPass");

        LinearLayout box = findViewById(R.id.statsBox);
        box.removeAllViews();
        addInfo(box, "NOME DO USUARIO", Session.name(this));
        addInfo(box, "E-MAIL", Session.email(this));
        addInfo(box, "TIPO DE ACESSO", friendlyType(Session.type(this)));
        addInfo(box, "STATUS", "Conta ativa e sincronizada");

        ((MaterialButton) findViewById(R.id.actionOne)).setText("Eventos");
        ((MaterialButton) findViewById(R.id.actionTwo)).setText("Meu QR Code");
        ((MaterialButton) findViewById(R.id.actionThree)).setText("Sair");
        findViewById(R.id.actionOne).setOnClickListener(v -> startActivity(new Intent(this, EventListActivity.class)));
        findViewById(R.id.actionTwo).setOnClickListener(v -> startActivity(new Intent(this, QrCodeActivity.class)));
        findViewById(R.id.actionThree).setOnClickListener(v -> {
            Session.clear(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void addInfo(LinearLayout parent, String label, String value) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_profile_info, parent, false);
        ((TextView) view.findViewById(R.id.profileLabel)).setText(label);
        ((TextView) view.findViewById(R.id.profileValue)).setText(value);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 12);
        view.setLayoutParams(params);
        parent.addView(view);
    }

    private String friendlyType(String type) {
        if ("ADMIN".equals(type)) {
            return "Administrador / equipe do evento";
        }
        return "Participante";
    }
}
