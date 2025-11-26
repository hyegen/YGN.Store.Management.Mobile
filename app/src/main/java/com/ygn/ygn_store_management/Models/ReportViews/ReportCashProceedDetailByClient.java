package com.ygn.ygn_store_management.Models.ReportViews;

import java.util.Date;

public class ReportCashProceedDetailByClient {
    public Integer Id;
    public Integer ClientId;
    public String ClientDesc;
    public double CollectionAmount;
    public Date CollectedDate;
    public String Note;
    public String PaymentType;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getClientId() {
        return ClientId;
    }

    public void setClientId(Integer clientId) {
        ClientId = clientId;
    }

    public String getClientDesc() {
        return ClientDesc;
    }

    public void setClientDesc(String clientDesc) {
        ClientDesc = clientDesc;
    }

    public double getCollectionAmount() {
        return CollectionAmount;
    }

    public void setCollectionAmount(double collectionAmount) {
        CollectionAmount = collectionAmount;
    }

    public Date getCollectedDate() {
        return CollectedDate;
    }

    public void setCollectedDate(Date collectedDate) {
        CollectedDate = collectedDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }
}
