
package com.ygn.ygn_store_management.Models;

public class Item {

    private int Id;
    private String ItemCode;
    private String ItemName;
    private Double PurchasingPrice;
    private Double SalesPrice;
    private int PackageAmount;

    public int getId() {
        return Id;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public Double getPurchasingPrice() {
        return PurchasingPrice;
    }

    public Double getSalesPrice() {
        return SalesPrice;
    }

    public int getPackageAmount() {
        return PackageAmount;
    }
}
