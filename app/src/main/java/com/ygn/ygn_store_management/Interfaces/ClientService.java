package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.Client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ClientService {

    @GET("api/getAllClientsByCodeAndNameAndSurname")
    Call<List<Client>> getClients(@Header("Authorization") String token);
}