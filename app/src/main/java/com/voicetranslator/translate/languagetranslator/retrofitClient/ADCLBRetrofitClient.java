package com.voicetranslator.translate.languagetranslator.retrofitClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voicetranslator.translate.languagetranslator.utils.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ADCLBRetrofitClient {
    private static ADCLBRetrofitClient instance = null;
    private ADCLBRetrofitService myApi;

    private ADCLBRetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.getMain())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        myApi = retrofit.create(ADCLBRetrofitService.class);
    }

    public static synchronized ADCLBRetrofitClient getInstance() {
        if (instance == null) {
            instance = new ADCLBRetrofitClient();
        }
        return instance;
    }

    public ADCLBRetrofitService getMyApi() {
        return myApi;
    }
}