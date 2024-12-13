package com.ygn.ygn_store_management.Interfaces;

import com.ygn.ygn_store_management.Models.Dtos.ClientSelectionDto;
import com.ygn.ygn_store_management.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ClientSelectionService {
    @GET("/api/getAllClientsByCodeAndNameAndSurname")
    Call<List<ClientSelectionDto>> GetAllClient();
}
