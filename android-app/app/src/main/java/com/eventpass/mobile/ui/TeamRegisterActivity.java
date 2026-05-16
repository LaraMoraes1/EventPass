package com.eventpass.mobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.eventpass.mobile.R;
import com.eventpass.mobile.api.ApiClient;
import com.eventpass.mobile.api.RegisterRequest;
import com.eventpass.mobile.model.User;
import com.google.android.material.button.MaterialButton;

public class TeamRegisterActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.screen_auth);

        ((TextView) findViewById(R.id.title)).setText("Equipe EventPass");
        ((TextView) findViewById(R.id.subtitle)).setText("Cadastre administradores para operar scanner e acessos");

        EditText name = findViewById(R.id.nameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText pass = findViewById(R.id.passwordInput);
        MaterialButton primary = findViewById(R.id.primaryButton);
        MaterialButton secondary = findViewById(R.id.secondaryButton);

        primary.setText("Cadastrar admin");
        secondary.setText("Voltar");
        primary.setOnClickListener(v -> registerAdmin(name, email, pass));
        secondary.setOnClickListener(v -> finish());
    }

    private void registerAdmin(EditText name, EditText email, EditText pass) {
        String nameText = name.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String passText = pass.getText().toString().trim();
        if (nameText.isEmpty() || emailText.isEmpty() || passText.isEmpty()) {
            Toast.makeText(this, "Preencha nome, email e senha", Toast.LENGTH_LONG).show();
            return;
        }

        ApiClient.get().register(new RegisterRequest(nameText, emailText, passText, "ADMIN"))
                .enqueue(new Ui<User>(this) {
                    @Override public void onOk(User user) {
                        Toast.makeText(TeamRegisterActivity.this, "Admin cadastrado: " + user.nome, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
