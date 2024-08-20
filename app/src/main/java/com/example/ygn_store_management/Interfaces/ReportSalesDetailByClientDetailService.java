package com.example.ygn_store_management.Interfaces;

import com.example.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;
import com.example.ygn_store_management.Models.ReportViews.StockAmountInformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReportSalesDetailByClientDetailService {
    @GET("/api/GetSalesByClientDetail")
    Call<List<SalesDetailByClientDetail>> GetSalesByClientDetail(@Header("Authorization") String token);
}
