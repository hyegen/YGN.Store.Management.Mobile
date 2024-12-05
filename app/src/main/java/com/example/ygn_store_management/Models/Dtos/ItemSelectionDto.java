package com.example.ygn_store_management.Models.Dtos;

import java.io.Serializable;

public class ItemSelectionDto implements Serializable {
    private String name;
    private String code;
    private double price;
    private int amount;

    public ItemSelectionDto(String name, String code, double price) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
