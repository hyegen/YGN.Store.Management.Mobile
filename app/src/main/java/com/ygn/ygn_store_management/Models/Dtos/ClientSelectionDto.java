package com.ygn.ygn_store_management.Models.Dtos;

public class ClientSelectionDto {
    private Integer ClientId;
    private String ClientCodeAndNameAndSurname;
    public ClientSelectionDto(Integer clientId, String clientDesciption) {
        ClientId = clientId;
        ClientCodeAndNameAndSurname = clientDesciption;
    }
    public Integer getClientId(){return ClientId;}
    public String getClientDescription() {
        return ClientCodeAndNameAndSurname;
    }
}
