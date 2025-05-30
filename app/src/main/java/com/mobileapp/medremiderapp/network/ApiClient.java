package com.mobileapp.medremiderapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://rxnav.nlm.nih.gov/";
    private static Retrofit retrofit = null;

    public static RxNormApi getRxNormApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RxNormApi.class);
    }
}
