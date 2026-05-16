package com.eventpass.mobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.Event;

public class EventFormActivity extends Activity {
    private long eventId;
    private EditText name;
    private EditText desc;
    private EditText local;
    private EditText date;
    private EditText time;
    private EditText limit;
    private EditText banner;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_form);
        eventId = getIntent().getLongExtra("eventId", -1);
        name = findViewById(R.id.nameInput);
        desc = findViewById(R.id.descInput);
        local = findViewById(R.id.localInput);
        date = findViewById(R.id.dateInput);
        time = findViewById(R.id.timeInput);
        limit = findViewById(R.id.limitInput);
        banner = findViewById(R.id.bannerInput);
        findViewById(R.id.saveButton).setOnClickListener(v -> save());
        if (eventId != -1) {
            load();
        }
    }

    private void load() {
        ApiClient.get().event(eventId).enqueue(new Ui<Event>(this) {
            @Override public void onOk(Event event) {
                name.setText(event.nome);
                desc.setText(event.descricao);
                local.setText(event.local);
                date.setText(event.dataEvento);
                time.setText(event.horario);
                limit.setText(String.valueOf(event.limiteParticipantes));
                banner.setText(event.bannerUrl);
            }
        });
    }

    private void save() {
        String formattedDate = normalizeDate(date.getText().toString().trim());
        if (isBlank(name) || isBlank(desc) || isBlank(local) || isBlank(time)) {
            Toast.makeText(this, "Preencha nome, descricao, local e horario", Toast.LENGTH_LONG).show();
            return;
        }
        if (formattedDate == null) {
            Toast.makeText(this, "Use a data no formato 20/06/2026 ou 2026-06-20", Toast.LENGTH_LONG).show();
            return;
        }

        Event event = new Event();
        event.nome = name.getText().toString().trim();
        event.descricao = desc.getText().toString().trim();
        event.local = local.getText().toString().trim();
        event.dataEvento = formattedDate;
        event.horario = time.getText().toString().trim();
        event.limiteParticipantes = parseLimit();
        event.bannerUrl = banner.getText().toString().trim();
        event.ativo = true;

        if (eventId == -1) {
            ApiClient.get().createEvent(event).enqueue(done());
        } else {
            ApiClient.get().updateEvent(eventId, event).enqueue(done());
        }
    }

    private Ui<Event> done() {
        return new Ui<Event>(this) {
            @Override public void onOk(Event data) {
                Toast.makeText(EventFormActivity.this, "Evento salvo", Toast.LENGTH_LONG).show();
                finish();
            }
        };
    }

    private boolean isBlank(EditText input) {
        return input.getText().toString().trim().isEmpty();
    }

    private String normalizeDate(String value) {
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return value;
        }
        if (value.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] parts = value.split("/");
            return parts[2] + "-" + parts[1] + "-" + parts[0];
        }
        return null;
    }

    private int parseLimit() {
        try {
            int value = Integer.parseInt(limit.getText().toString().trim());
            return Math.max(value, 1);
        } catch (Exception e) {
            return 50;
        }
    }
}
