package com.example.jatcool.zno_on_math;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworService {
    private static  NetworService mInstance;
    public static final String BASE_URL = "https://shocking-asylum-01285.herokuapp.com";
    private Retrofit retrofit;
    private NetworService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }
    public  static  NetworService getInstance(){
        if(mInstance==null){
            mInstance = new NetworService();
        }
        return mInstance;
    }
    public ZnoApi getJSONApi() {
        return retrofit.create(ZnoApi.class);
    }
}

