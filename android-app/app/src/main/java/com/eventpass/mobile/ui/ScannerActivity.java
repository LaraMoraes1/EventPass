package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Toast;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.api.ScanRequest;
import com.eventpass.mobile.model.ScanResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        openScanner();
    }

    private void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Aponte para o QR Code EventPass");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null || result.getContents() == null) {
            finish();
            return;
        }
        ApiClient.get().scan(new ScanRequest(result.getContents())).enqueue(new Ui<ScanResponse>(this) {
            @Override public void onOk(ScanResponse response) {
                int tone = response.sucesso ? ToneGenerator.TONE_PROP_ACK : ToneGenerator.TONE_PROP_NACK;
                new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90).startTone(tone, 220);
                Intent intent = new Intent(ScannerActivity.this, ScanResultActivity.class);
                intent.putExtra("success", response.sucesso);
                intent.putExtra("status", response.status);
                intent.putExtra("message", response.mensagem);
                intent.putExtra("person", response.participante);
                intent.putExtra("event", response.evento);
                intent.putExtra("entry", response.entradaEm);
                intent.putExtra("exit", response.saidaEm);
                intent.putExtra("stay", response.permanenciaMinutos == null ? "-" : String.valueOf(response.permanenciaMinutos));
                startActivity(intent);
                finish();
            }
        });
    }
}
