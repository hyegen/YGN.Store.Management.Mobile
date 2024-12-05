package com.example.ygn_store_management.Interfaces;

import com.example.ygn_store_management.Models.Dtos.ItemSelectionDto;
import com.example.ygn_store_management.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemSelectionService {
    @POST("/api/GetProductByItemCodeTest")
    Call<ItemSelectionDto> GetItemByItemCode(@Query("itemCode") String itemCode);
}
