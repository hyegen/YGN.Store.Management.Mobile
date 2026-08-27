package com.ygn.ygn_store_management.Models.ReportViews;

public class ReportCashProceedListViews {
    private Integer CashProceedId;
    private String ClientDesc;
    private Integer ClientId;
    private double CashProceedAmount;
    private String CashProceedDate_;
    private String CashProceedType;
    private  String FirmDescription;

    public Integer getCashProceedId() {
        return CashProceedId;
    }

    public void setCashProceedId(Integer cashProceedId) {
        CashProceedId = cashProceedId;
    }

    public String getClientDesc() {
        return ClientDesc;
    }

    public void setClientDesc(String clientDesc) {
        ClientDesc = clientDesc;
    }

    public Integer getClientId() {
        return ClientId;
    }

    public void setClientId(Integer clientId) {
        ClientId = clientId;
    }

    public double getCashProceedAmount() {
        return CashProceedAmount;
    }

    public void setCashProceedAmount(double cashProceedAmount) {
        CashProceedAmount = cashProceedAmount;
    }

    public String getCashProceedDate_() {
        return CashProceedDate_;
    }

    public void setCashProceedDate_(String cashProceedDate_) {
        CashProceedDate_ = cashProceedDate_;
    }

    public String getCashProceedType() {
        return CashProceedType;
    }

    public void setCashProceedType(String cashProceedType) {
        CashProceedType = cashProceedType;
    }

    public String getFirmDescription() {
        return FirmDescription;
    }

    public void setFirmDescription(String firmDescription) {
        FirmDescription = firmDescription;
    }
}
