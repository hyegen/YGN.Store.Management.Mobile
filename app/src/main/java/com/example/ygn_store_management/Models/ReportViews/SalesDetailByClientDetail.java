package com.example.ygn_store_management.Models.ReportViews;

public class SalesDetailByClientDetail {
    public String ClientCode;
    public String ClientName;
    public String ClientSurname;
    public String FirmDescription;
    public String Date_;
    public Double TotalPrice;
    public void setClientCode(String clientCode) {
        this.ClientCode = clientCode;
    }
    public void setClientName(String clientName) {
        this.ClientName = clientName;
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

    public String getClientCode() {
        return ClientCode;
    }
    public String getClientName() {
        return ClientName;
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

}
