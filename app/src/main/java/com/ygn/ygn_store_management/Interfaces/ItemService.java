package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.Item;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItemService {
    @GET("api/getItemByItemCode")
    Call<Item> getItemByItemCode(
            @Query("itemCode") String itemCode
    );
}
