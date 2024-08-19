package com.example.ygn_store_management.Models.ReportViews;

public class StockAmountInformation {
    public String ItemCode;
    public String ItemName;
    public Integer StockAmount;
    public String getItemCode(){
        return ItemCode;
    }
    public String getItemName(){
        return ItemName;
    }
    public Integer getStockAmount(){
        return StockAmount;
    }
    public void setItemCode(String itemCode){
        this.ItemCode=itemCode;
    }
    public void setItemName(String itemName){
        this.ItemName=itemName;
    }
    public void setStockAmount(Integer stockAmount){
        this.StockAmount=stockAmount;
    }
}
