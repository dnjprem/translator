package com.google.mlkit.samples.nl.translate.java.retrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ADCLBRetrofitService
{
    @POST("update.php")
    @FormUrlEncoded
    Call<ResponseBody> data(@Field("data") String requestBody);
}
