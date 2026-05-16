package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        TextView view = new TextView(this);
        view.setText("EventPass\nAcesso inteligente para eventos");
        view.setGravity(17);
        view.setTextSize(28);
        view.setTextColor(0xFFFFFFFF);
        view.setBackgroundColor(0xFF101418);
        setContentView(view);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 1200);
    }
}
