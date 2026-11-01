package com.ygn.ygn_store_management.Models;




public class Client {

    public int ClientId;
    public String ClientNameAndSurname;

    public int getId() {
        return ClientId;
    }

    public String getNameAndSurname() {
        return ClientNameAndSurname;
    }

    @Override
    public String toString() {
        return ClientNameAndSurname == null
                ? ""
                : ClientNameAndSurname;
    }
}


