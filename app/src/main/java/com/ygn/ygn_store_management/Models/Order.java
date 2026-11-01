package com.ygn.ygn_store_management.Models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public int Id;
    public String OrderNumber;
    public List<OrderLine> OrderLines = new ArrayList<>();
    public String CreatedDate_;
    public int IOCode;
    public int TransactionCode;
    public int ClientId;
    public int PaymentType;

    public boolean HasTax;
    public double TaxPercentage;
    public double TotalPrice;

    public String OrderNote;
    public boolean isCompleted;

    public int OrderCreatedUserId;
    public Integer OrderUpdatedLastUserId;
    public Double Discount;
}
