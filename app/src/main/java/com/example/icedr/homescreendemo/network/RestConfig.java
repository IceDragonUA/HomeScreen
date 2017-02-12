package com.example.icedr.homescreendemo.network;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestConfig {

    public static final String BASE_URL = "http://91.250.82.77:8081/";

    private static RestConfig instance;

    private final Retrofit retrofitInstance;

    public static RestConfig getInstance() {
        if (instance == null) {
            instance = new RestConfig();
        }
        return instance;
    }

    private RestConfig() {
        retrofitInstance = initRetrofit();
    }

    private Retrofit initRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <T> T create(final Class<T> service) {
        return getInstance().retrofitInstance.create(service);
    }

}
