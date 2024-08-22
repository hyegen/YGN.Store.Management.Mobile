package com.example.ygn_store_management.Models.ReportViews;

public class ItemInformation {
    private int Id;
    private String ItemCode;
    private String ItemName;
    private String Brand;
    private int StockAmount;
    private String ItemImage;

    public int getId() { return Id; }
    public void setId(int id) { Id = id; }

    public String getItemCode() { return ItemCode; }
    public void setItemCode(String itemCode) { ItemCode = itemCode; }

    public String getItemName() { return ItemName; }
    public void setItemName(String itemName) { ItemName = itemName; }

    public String getBrand() { return Brand; }
    public void setBrand(String brand) { Brand = brand; }

    public int getStockAmount() { return StockAmount; }
    public void setStockAmount(int stockAmount) { StockAmount = stockAmount; }

    public String getItemImage() { return ItemImage; }
    public void setItemImage(String itemImage) { ItemImage = itemImage; }
}
