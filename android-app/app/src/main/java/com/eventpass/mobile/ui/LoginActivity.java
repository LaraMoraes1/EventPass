package com.eventpass.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.*;
import com.eventpass.mobile.model.User;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_auth);
        findViewById(R.id.nameBox).setVisibility(View.GONE);
        EditText email = findViewById(R.id.emailInput);
        EditText pass = findViewById(R.id.passwordInput);
        MaterialButton primary = findViewById(R.id.primaryButton);
        MaterialButton secondary = findViewById(R.id.secondaryButton);
        primary.setText("Entrar no EventPass");
        secondary.setText("Criar minha conta");
        primary.setOnClickListener(v -> ApiClient.get().login(new LoginRequest(email.getText().toString(), pass.getText().toString()))
                .enqueue(new Ui<User>(this) {
                    @Override public void onOk(User user) {
                        if (user.id == null) {
                            Toast.makeText(LoginActivity.this, "Login sem ID de usuario. Verifique a API.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Session.save(LoginActivity.this, user);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }

                    @Override public void onError(int code) {
                        if (code == 401) {
                            Toast.makeText(LoginActivity.this, "E-mail ou senha incorretos", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Nao foi possivel entrar. Erro: " + code, Toast.LENGTH_LONG).show();
                        }
                    }
                }));
        secondary.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
