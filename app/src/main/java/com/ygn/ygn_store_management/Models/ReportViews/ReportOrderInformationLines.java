package com.ygn.ygn_store_management.Models.ReportViews;

public class ReportOrderInformationLines {
    private String ItemCode;
    private String ItemName;
    private Integer Amount;
    private double UnitPrice;
    private double LineTotal;


    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public double getLineTotal() {
        return LineTotal;
    }

    public void setLineTotal(double lineTotal) {
        LineTotal = lineTotal;
    }
}
