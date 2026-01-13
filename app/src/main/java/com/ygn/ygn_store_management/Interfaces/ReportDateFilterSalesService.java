package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.ReportViews.OrderFilterByDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReportDateFilterSalesService {
    @POST("api/GetOrderDetailByDateFilter")
    Call<List<OrderFilterByDate>> getOrdersByDate(
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );
}
