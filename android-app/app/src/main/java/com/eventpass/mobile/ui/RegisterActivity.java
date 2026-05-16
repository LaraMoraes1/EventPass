package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.*;
import com.eventpass.mobile.model.User;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_auth);
        EditText name = findViewById(R.id.nameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText pass = findViewById(R.id.passwordInput);
        MaterialButton primary = findViewById(R.id.primaryButton);
        MaterialButton secondary = findViewById(R.id.secondaryButton);
        primary.setText("Cadastrar participante");
        secondary.setText("Já tenho conta");
        primary.setOnClickListener(v -> ApiClient.get().register(new RegisterRequest(name.getText().toString(), email.getText().toString(), pass.getText().toString(), "PARTICIPANTE"))
                .enqueue(new Ui<User>(this) {
                    @Override public void onOk(User user) {
                        if (user.id == null) {
                            android.widget.Toast.makeText(RegisterActivity.this, "Cadastro sem ID de usuario. Verifique a API.", android.widget.Toast.LENGTH_LONG).show();
                            return;
                        }
                        Session.save(RegisterActivity.this, user);
                        startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                        finish();
                    }
                }));
        secondary.setOnClickListener(v -> finish());
    }
}
