package com.example.ygn_store_management.Models.ReportViews;

public class OrderInformationLines {
    public String OrderFicheNumber;
    public String ClientName;
    public String ClientSurname;
    public String FirmDescription;
    public String HasTax;
    public Double TaxPercentage;
    public Double OrderNote;
    public String Date_;
    public Double TotalPrice;

    public String ItemCode ;
    public String ItemName;
    public Integer Amount;
    public Double UnitPrice;
    public Integer LineTotal;

    public void setClientName(String clientName) {
        this.ClientName = clientName;
    }
    public void setOrderFicheNumber(String orderFicheNumber) {
        this.OrderFicheNumber = orderFicheNumber;
    }
    public void setClientSurname(String clientSurname) {
        this.ClientSurname = clientSurname;
    }
    public void setFirmDescription(String firmDescription) {
        this.FirmDescription = firmDescription;
    }
    public void setDate_(String date) {
        this.Date_ = date;
    }
    public void setTotalPrice(String totalPrice) {
        this.TotalPrice = Double.valueOf(totalPrice);
    }
    public void setHasTax(String firmDescription) {
        this.FirmDescription = firmDescription;
    }
    public void setTaxPercentage(String date) {
        this.Date_ = date;
    }
    public void setOrderNote(String totalPrice) {
        this.TotalPrice = Double.valueOf(totalPrice);
    }
    public void setItemCode(String itemCode) {
        this.ItemCode = itemCode;
    }
    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }
    public void setAmount(Integer amount) {
        this.Amount = amount;
    }
    public void setUnitPrice(Double unitPrice) {
        this.UnitPrice = unitPrice;
    }
    public void setLineTotal(Integer lineTotal) {
        this.LineTotal = lineTotal;
    }


    public String getClientName() {
        return ClientName;
    }
    public String getOrderFicheNumber() {
        return OrderFicheNumber;
    }
    public String getClientSurname() {
        return ClientSurname;
    }
    public String getFirmDescription() {
        return FirmDescription;
    }
    public String getDate_() {
        return Date_;
    }
    public String getHasTax() {
        return ClientSurname;
    }
    public String getTaxPercentage() {
        return FirmDescription;
    }
    public String getOrderNote() {
        return Date_;
    }
    public String getItemCode() {
        return ItemCode;
    }
    public String getItemName() {
        return ItemName;
    }
    public Integer getAmount() {
        return Amount;
    }
    public Double getUnitPrice() {
        return UnitPrice;
    }
    public Integer getLineTotal() {
        return LineTotal;
    }

}
