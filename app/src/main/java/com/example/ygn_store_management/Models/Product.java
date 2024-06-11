package com.example.ygn_store_management.Models;

import java.io.Serializable;

public class Product implements Serializable {
    public Product() {
        this.ItemId=ItemId;
        this.ItemCode = ItemCode;
        this.ItemName = ItemName;
        this.StockAmount = StockAmount;
        this.UnitPrice=UnitPrice;
        this.isSelected=isSelected;
        this.Amount=Amount;
    }
    public Integer ItemId;
    public String ItemCode;
    public String ItemName;
    public Integer StockAmount;
    public Double UnitPrice;
    public Boolean isSelected=false;
    public Integer Amount;
    public String getItemCode() {
        return ItemCode;
    }
    public String getItemName() {
        return ItemName;
    }
    public Double getUnitPrice() {
        return UnitPrice;
    }
    public Integer getAmount() {
        return StockAmount;
    }
    public Integer getItemId() {
        return ItemId;
    }
    public Boolean getSelected() {
        return isSelected;
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
    public void setUnitPrice(String unitPrice) {
        this.UnitPrice = Double.valueOf(unitPrice);
    }
    public void setSelected(Boolean isSelected) {
        this.isSelected = true;
    }

public void setAmount(Integer amount) {
    try {
        if (amount != null) {
            this.Amount = amount;
        } else {
            this.Amount = 0;
        }
    } catch (Exception e) {
        System.err.println("Error while setting amount: " + e.getMessage());
    }
}

}
