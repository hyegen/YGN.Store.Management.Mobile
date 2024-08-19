package com.example.ygn_store_management.Managers;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ApiUtils {
    public static Retrofit InitRequestWithToken(String apiUrl,String token){

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + token)
                                    .method(original.method(), original.body());

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            return new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }catch (Exception exception){
            Log.e("HATA", "Hata: " + exception.getMessage());
        }
        return  null;
    }
    public static Retrofit InitRequestWithoutToken(String apiUrl){
        try {
            return new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }catch (Exception exception){
            Log.e("HATA", "Hata: " + exception.getMessage());
        }
        return  null;
    }
}
