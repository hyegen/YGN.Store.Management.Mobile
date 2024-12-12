package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.PurchasingDetailByClientDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReportPurchasingDetailByClientDetailService {
    @GET("/api/GetPurchasingByClientDetail")
    Call<List<PurchasingDetailByClientDetail>> GetPurchasingByClientDetail(@Header("Authorization") String token);
}
