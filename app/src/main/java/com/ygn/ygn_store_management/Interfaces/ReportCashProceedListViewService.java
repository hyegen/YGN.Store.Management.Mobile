package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.ReportCashProceedListViews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReportCashProceedListViewService {
    @GET("/api/GetCashProceedListView")
    Call<List<ReportCashProceedListViews>> GetCashProceedListView(@Header("Authorization") String token);
}
