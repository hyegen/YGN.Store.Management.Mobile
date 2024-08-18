package com.example.ygn_store_management.Interfaces;

import com.example.ygn_store_management.Models.ReportViews.OrderInformation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OrderLineInformationService {
    @GET("/api/GetOrderDetailInformation")
    Call<OrderInformation> GetOrderDetailInformation(@Header("Authorization") String token, @Query("orderFicheNumber") String orderFicheNumber);
}
