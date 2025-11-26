package com.ygn.ygn_store_management.Models.ReportViews;

import java.util.Date;

public class ReportSpending {

    public Integer Id;
    public String SpendingDetail;
    public String  PaymentType;
    public double SpendingAmount;
    public Date AddedDate;


    public Integer getId() {
        return Id;
    }

    public String getSpendingDetail() {
        return SpendingDetail;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public double getSpendingAmount() {
        return SpendingAmount;
    }

    public Date getAddedDate() {
        return AddedDate;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setSpendingDetail(String spendingDetail) {
        SpendingDetail = spendingDetail;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public void setSpendingAmount(double spendingAmount) {
        SpendingAmount = spendingAmount;
    }

    public void setAddedDate(Date addedDate) {
        AddedDate = addedDate;
    }
}
