package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.Dtos.ItemSelectionDto;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemSelectionService {
    @POST("/api/GetProductByItemCodeTest")
    Call<ItemSelectionDto> GetItemByItemCode(@Query("itemCode") String itemCode);
}