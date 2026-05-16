package com.eventpass.mobile.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://eventpass-api.onrender.com/api/";
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
