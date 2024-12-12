package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.SalesDetailByClientDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReportSalesDetailByClientDetailService {
    @GET("/api/GetSalesByClientDetail")
    Call<List<SalesDetailByClientDetail>> GetSalesByClientDetail(@Header("Authorization") String token);
}
