package com.example.ygn_store_management.Models.ReportViews;

import java.util.List;

public class OrderInformation {
    private String OrderFicheNumber;
    private String ClientName;
    private String ClientSurname;
    private String FirmDescription;
    private String Date_;
    private double TotalPrice;
    private String HasTax;
    private double TaxPercentage;
    private String OrderNote;
    private List<ReportOrderInformationLines> OrderLines;

    // Getters ve Setters

    public String getOrderFicheNumber() {
        return OrderFicheNumber;
    }

    public void setOrderFicheNumber(String orderFicheNumber) {
        OrderFicheNumber = orderFicheNumber;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientSurname() {
        return ClientSurname;
    }

    public void setClientSurname(String clientSurname) {
        ClientSurname = clientSurname;
    }

    public String getFirmDescription() {
        return FirmDescription;
    }

    public void setFirmDescription(String firmDescription) {
        FirmDescription = firmDescription;
    }

    public String getDate_() {
        return Date_;
    }

    public void setDate_(String date_) {
        Date_ = date_;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getHasTax() {
        return HasTax;
    }

    public void setHasTax(String hasTax) {
        HasTax = hasTax;
    }

    public double getTaxPercentage() {
        return TaxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public String getOrderNote() {
        return OrderNote;
    }

    public void setOrderNote(String orderNote) {
        OrderNote = orderNote;
    }

    public List<ReportOrderInformationLines> getOrderLines() {
        return OrderLines;
    }

    public void setOrderLines(List<ReportOrderInformationLines> orderLines) {
        OrderLines = orderLines;
    }
}
