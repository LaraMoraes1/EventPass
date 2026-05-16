package com.eventpass.mobile.ui;

import android.app.Activity;
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
import java.util.List;

public class QrCodeActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_qr);
        long eventId = getIntent().getLongExtra("eventId", -1);
        ApiClient.get().registrations(Session.userId(this)).enqueue(new Ui<List<Registration>>(this) {
            @Override public void onOk(List<Registration> registrations) {
                Registration selected = null;
                for (Registration item : registrations) {
                    if (eventId == -1 || item.eventoId != null && item.eventoId == eventId) {
                        selected = item;
                        break;
                    }
                }
                if (selected == null) {
                    Toast.makeText(QrCodeActivity.this, "Inscreva-se em um evento primeiro", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                ((TextView) findViewById(R.id.title)).setText(selected.eventoNome);
                ((TextView) findViewById(R.id.tokenText)).setText(selected.qrToken);
                ((ImageView) findViewById(R.id.qrImage)).setImageBitmap(makeQr(selected.qrToken));
            }
        });
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
