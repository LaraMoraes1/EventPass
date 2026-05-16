package com.eventpass.mobile.ui;

import android.app.Activity;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class Ui<T> implements Callback<T> {
    private final Activity activity;

    public Ui(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onOk(response.body());
        } else if (response.isSuccessful()) {
            onOk(null);
        } else {
            onError(response.code());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(activity, "Falha de conexao: " + t.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
    }

    public abstract void onOk(T data);

    public void onError(int code) {
        Toast.makeText(activity, "Erro da API: " + code, Toast.LENGTH_LONG).show();
    }
}
