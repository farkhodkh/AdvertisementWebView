package ru.haknazarovfarkhod.advertisementwebview.services;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 21.02.2018.
 */

public class WebService {
    private static final String WEB_SERVICE_URL = "http://www.505.rs/";
    private Retrofit retrofit;
    private static WebServiceInterface webServiceInterface;

    public WebService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceInterface = retrofit.create(WebServiceInterface.class);

        this.retrofit = retrofit;
    }

    public static WebServiceInterface getApi() {
        return webServiceInterface;
    }
}
