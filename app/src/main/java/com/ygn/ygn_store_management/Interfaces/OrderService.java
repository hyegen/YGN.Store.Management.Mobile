package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.Item;
import com.ygn.ygn_store_management.Models.Order;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderService {
    @POST("api/saveOrder")
    Call<ResponseBody> saveOrder(@Header("Authorization") String token,
            @Body Order order
    );

    @GET("api/getOrderFicheNumber")
    Call<String> getOrderFicheNumber(@Header("Authorization") String token);
}
