package com.example.ygn_store_management.Models;

import java.io.Serializable;

public class Product implements Serializable {
    public Product() {
        this.ItemCode = ItemCode;
        this.ItemName = ItemName;
        this.StockAmount = StockAmount;
        this.UnitPrice=UnitPrice;
        this.isSelected=isSelected;
    }
    public String ItemCode;
    public String ItemName;
    public Integer StockAmount;
    public Double UnitPrice;
    public Boolean isSelected=false;

    public String getItemCode() {
        return ItemCode;
    }
    public String getItemName() {
        return ItemName;
    }
    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }
    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }
    public void setStockAmount(String stockAmount) {
        this.StockAmount = Integer.valueOf(stockAmount);
    }
    public void setUnitPrice(Double unitPrice) {
        this.UnitPrice = Double.valueOf(unitPrice);
    }

}
