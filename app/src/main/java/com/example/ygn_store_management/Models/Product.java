package com.example.ygn_store_management.Models;

public class Product {
    public Product() {
        this.ItemCode = ItemCode;
        this.ItemName = ItemName;
        this.StockAmount = StockAmount;
    }
    public String ItemCode;
    public String ItemName;
    public Integer StockAmount;
    public String getItemCode() {
        return ItemCode;
    }

    public String getItemName() {
        return ItemName;
    }
    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }
    public void setItemName(String itemCode) {
        this.ItemName = itemCode;
    }
    public void setStockAmount(String itemCode) {
        this.StockAmount = Integer.valueOf(itemCode);
    }
}
