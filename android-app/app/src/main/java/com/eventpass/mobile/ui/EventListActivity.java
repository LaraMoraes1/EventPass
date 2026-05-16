package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.Event;
import java.util.List;

public class EventListActivity extends Activity {
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_list);
        list = findViewById(R.id.listContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list != null) {
            loadEvents();
        }
    }

    private void loadEvents() {
        list.removeAllViews();
        title(list, "Eventos disponiveis");
        ApiClient.get().events().enqueue(new Ui<List<Event>>(this) {
            @Override public void onOk(List<Event> events) {
                for (Event event : events) {
                    if (event.ativo == null || event.ativo) {
                        card(list, event);
                    }
                }
            }
        });
    }

    private void title(LinearLayout list, String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(0xFFFFFFFF);
        view.setTextSize(28);
        view.setPadding(0, 0, 0, 18);
        list.addView(view);
    }

    private void card(LinearLayout list, Event event) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_event, list, false);
        ImageView banner = view.findViewById(R.id.bannerImage);
        TextView name = view.findViewById(R.id.eventName);
        TextView meta = view.findViewById(R.id.eventMeta);
        TextView description = view.findViewById(R.id.eventDescription);

        name.setText(event.nome);
        meta.setText(event.local + " | " + formatDate(event.dataEvento) + " as " + event.horario + " | " + event.limiteParticipantes + " vagas");
        description.setText(event.descricao);
        if (event.bannerUrl != null && !event.bannerUrl.trim().isEmpty()) {
            Glide.with(this).load(event.bannerUrl.trim()).centerCrop().into(banner);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, 14);
        view.setLayoutParams(params);
        view.setOnClickListener(v -> startActivity(new Intent(this, EventDetailActivity.class).putExtra("eventId", event.id)));
        list.addView(view);
    }

    private String formatDate(String value) {
        if (value == null || value.length() < 10) {
            return "-";
        }
        String[] date = value.substring(0, 10).split("-");
        if (date.length != 3) {
            return value;
        }
        return date[2] + "/" + date[1] + "/" + date[0];
    }
}
