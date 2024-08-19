package com.example.ygn_store_management.Interfaces;

import com.example.ygn_store_management.Models.LoginResponse;
import com.example.ygn_store_management.Models.ReportViews.OrderInformation;
import com.example.ygn_store_management.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    @POST("/api/authenticate")
    Call<LoginResponse> Login(@Query("username") String username, @Query("password") String password);
    @GET("/api/getallusers")
    Call<List<User>> GetAllUsers();
}
