package com.ygn.ygn_store_management.Models.Dtos;

import java.io.Serializable;

public class ItemSelectionDto implements Serializable {
    private String ItemCode;
    private String ItemName;
    private double ItemPrice;
    private int ItemAmount;

    public ItemSelectionDto(String name, String code, double price) {
        this.ItemCode = code;
        this.ItemName = name;
        this.ItemPrice = price;
        this.ItemAmount = 0;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public int getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(int amount) {
        this.ItemAmount = amount;
    }
}
