package com.ygn.ygn_store_management.Models;

public class Client {
    public Client() {
        this.ClientId = ClientId;
        this.ClientCodeAndNameAndSurname = ClientCodeAndNameAndSurname;

    }
    public Integer ClientId;
    public String ClientCodeAndNameAndSurname;
    public Integer getClientId() {
        return ClientId;
    }
    public String getClientCodeAndNameAndSurname() {
        return ClientCodeAndNameAndSurname;
    }
    public void setClientId(Integer ClientId) {
        this.ClientId = ClientId;
    }
    public void setClientCodeAndNameAndSurname(String ClientCodeAndNameAndSurname) {
        this.ClientCodeAndNameAndSurname = ClientCodeAndNameAndSurname;
    }
}
