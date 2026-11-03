package com.ygn.ygn_store_management.Models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int Id;
    private String OrderNumber;
    private List<OrderLine> OrderLines = new ArrayList<>();
    private String CreatedDate_;
    private int IOCode;
    private int TransactionCode;
    private int ClientId;
    private int PaymentType;

    private boolean HasTax;
    private double TaxPercentage;
    private double TotalPrice;

    private String OrderNote;
    private boolean isCompleted;

    private int OrderCreatedUserId;
    private Integer OrderUpdatedLastUserId;
    private Double Discount;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public List<OrderLine> getOrderLines() {
        return OrderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        OrderLines = orderLines;
    }

    public String getCreatedDate_() {
        return CreatedDate_;
    }

    public void setCreatedDate_(String createdDate_) {
        CreatedDate_ = createdDate_;
    }

    public int getIOCode() {
        return IOCode;
    }

    public void setIOCode(int IOCode) {
        this.IOCode = IOCode;
    }

    public int getTransactionCode() {
        return TransactionCode;
    }

    public void setTransactionCode(int transactionCode) {
        TransactionCode = transactionCode;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public boolean isHasTax() {
        return HasTax;
    }

    public void setHasTax(boolean hasTax) {
        HasTax = hasTax;
    }

    public double getTaxPercentage() {
        return TaxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getOrderNote() {
        return OrderNote;
    }

    public void setOrderNote(String orderNote) {
        OrderNote = orderNote;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getOrderCreatedUserId() {
        return OrderCreatedUserId;
    }

    public void setOrderCreatedUserId(int orderCreatedUserId) {
        OrderCreatedUserId = orderCreatedUserId;
    }

    public Integer getOrderUpdatedLastUserId() {
        return OrderUpdatedLastUserId;
    }

    public void setOrderUpdatedLastUserId(Integer orderUpdatedLastUserId) {
        OrderUpdatedLastUserId = orderUpdatedLastUserId;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }
}
