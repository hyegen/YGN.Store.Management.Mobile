package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.StockAmountInformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
public interface StockAmountInformationService {
    @GET("/api/GetStockAmount")
    Call<List<StockAmountInformation>> GetStockAmount(@Header("Authorization") String token);
}
