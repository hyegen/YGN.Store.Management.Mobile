package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.ItemInformation;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReportItemInformationService {
    @POST("/api/MobGetItemInformationByItemCode")
    Call<ItemInformation> MobGetItemInformationByItemCode(@Header("Authorization") String token, @Query("itemCode") String itemCode);
}

