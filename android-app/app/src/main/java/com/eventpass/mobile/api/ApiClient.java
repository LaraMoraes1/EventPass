package com.eventpass.mobile.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://127.0.0.1:8080/api/";
    private static EventPassApi api;

    public static EventPassApi get() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL.trim())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(EventPassApi.class);
        }
        return api;
    }
}
