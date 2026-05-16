package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.Event;
import com.eventpass.mobile.model.Registration;
import com.google.android.material.button.MaterialButton;

public class EventDetailActivity extends Activity {
    private Long eventId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_dashboard);
        eventId = getIntent().getLongExtra("eventId", -1);
        configureActions();
        loadEvent();
    }

    private void configureActions() {
        MaterialButton one = findViewById(R.id.actionOne);
        MaterialButton two = findViewById(R.id.actionTwo);
        MaterialButton three = findViewById(R.id.actionThree);

        if (Session.isAdmin(this)) {
            one.setText("Editar evento");
            two.setText("Excluir evento");
            three.setText("Voltar");
            one.setOnClickListener(v -> startActivity(new Intent(this, EventFormActivity.class).putExtra("eventId", eventId)));
            two.setOnClickListener(v -> deleteEvent());
            three.setOnClickListener(v -> finish());
        } else {
            one.setText("Inscrever-se e gerar QR");
            two.setText("Abrir QR Code");
            three.setText("Voltar");
            one.setOnClickListener(v -> subscribe());
            two.setOnClickListener(v -> startActivity(new Intent(this, QrCodeActivity.class).putExtra("eventId", eventId)));
            three.setOnClickListener(v -> finish());
        }
    }

    private void loadEvent() {
        ApiClient.get().event(eventId).enqueue(new Ui<Event>(this) {
            @Override public void onOk(Event event) {
                ((TextView) findViewById(R.id.title)).setText(event.nome);
                ((TextView) findViewById(R.id.subtitle)).setText(event.local + " - " + event.dataEvento + " as " + event.horario);

                ImageView hero = findViewById(R.id.heroImage);
                if (event.bannerUrl != null && !event.bannerUrl.trim().isEmpty()) {
                    hero.setVisibility(View.VISIBLE);
                    hero.getLayoutParams().height = 180;
                    hero.requestLayout();
                    Glide.with(EventDetailActivity.this).load(event.bannerUrl.trim()).centerCrop().into(hero);
                }

                LinearLayout stats = findViewById(R.id.statsBox);
                stats.removeAllViews();
                add(stats, event.descricao);
                add(stats, "Limite: " + event.limiteParticipantes + " participantes");
                if (Session.isAdmin(EventDetailActivity.this)) {
                    add(stats, "Admin: use os botoes abaixo para editar ou desativar este evento.");
                }
            }
        });
    }

    private void deleteEvent() {
        ApiClient.get().deleteEvent(eventId).enqueue(new Ui<Void>(this) {
            @Override public void onOk(Void data) {
                Toast.makeText(EventDetailActivity.this, "Evento excluido", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override public void onError(int code) {
                Toast.makeText(EventDetailActivity.this, "Nao foi possivel excluir. Erro: " + code, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribe() {
        ApiClient.get().subscribe(Session.userId(this), eventId).enqueue(new Ui<Registration>(this) {
            @Override public void onOk(Registration data) {
                Toast.makeText(EventDetailActivity.this, "Inscricao confirmada", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EventDetailActivity.this, QrCodeActivity.class).putExtra("eventId", eventId));
            }
        });
    }

    private void add(LinearLayout parent, String text) {
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
