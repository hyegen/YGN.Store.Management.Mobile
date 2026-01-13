package com.ygn.ygn_store_management.Models.ReportViews;

import com.google.gson.annotations.SerializedName;

public class OrderFilterByDate {
    @SerializedName("OrderFicheNumber")
    public int orderFicheNumber;

    @SerializedName("ClientName")
    public String clientName;

    @SerializedName("ClientSurname")
    public String clientSurname;

    @SerializedName("FirmDescription")
    public String firmDescription;

    @SerializedName("Date_")
    public String date;

    @SerializedName("TotalPrice")
    public double totalPrice;
}
