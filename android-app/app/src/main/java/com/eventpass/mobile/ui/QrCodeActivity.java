package com.eventpass.mobile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.model.Registration;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.List;

public class QrCodeActivity extends Activity {
    private long eventId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_qr);
        eventId = getIntent().getLongExtra("eventId", -1);
        loadRegistrations();
    }

    private void loadRegistrations() {
        ApiClient.get().registrations(Session.userId(this)).enqueue(new Ui<List<Registration>>(this) {
            @Override public void onOk(List<Registration> registrations) {
                if (registrations == null || registrations.isEmpty()) {
                    Toast.makeText(QrCodeActivity.this, "Inscreva-se em um evento primeiro", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                if (eventId != -1) {
                    Registration selected = findByEvent(registrations, eventId);
                    if (selected == null) {
                        Toast.makeText(QrCodeActivity.this, "Inscricao nao encontrada para este evento", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    showQr(selected);
                    return;
                }

                if (registrations.size() == 1) {
                    showQr(registrations.get(0));
                } else {
                    chooseRegistration(registrations);
                }
            }
        });
    }

    private Registration findByEvent(List<Registration> registrations, long targetEventId) {
        for (Registration item : registrations) {
            if (item.eventoId != null && item.eventoId == targetEventId) {
                return item;
            }
        }
        return null;
    }

    private void chooseRegistration(List<Registration> registrations) {
        List<String> names = new ArrayList<>();
        for (Registration item : registrations) {
            names.add(item.eventoNome == null ? "Evento" : item.eventoNome);
        }
        new AlertDialog.Builder(this)
                .setTitle("Escolha o evento")
                .setItems(names.toArray(new String[0]), (dialog, which) -> showQr(registrations.get(which)))
                .setOnCancelListener(dialog -> finish())
                .show();
    }

    private void showQr(Registration selected) {
        ((TextView) findViewById(R.id.title)).setText(selected.eventoNome);
        ((TextView) findViewById(R.id.tokenText)).setText(selected.qrToken);
        ((ImageView) findViewById(R.id.qrImage)).setImageBitmap(makeQr(selected.qrToken));
    }

    private Bitmap makeQr(String token) {
        try {
            int size = 760;
            BitMatrix matrix = new MultiFormatWriter().encode(token, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao gerar QR Code", Toast.LENGTH_LONG).show();
            return Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
        }
    }
}