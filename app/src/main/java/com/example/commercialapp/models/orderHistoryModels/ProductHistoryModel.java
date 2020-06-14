package com.example.commercialapp.models.orderHistoryModels;

public class ProductHistoryModel {

    private String acIdent;
    private String anPrice;
    private String acName;
    private String anNo;
    private String anQty;
    private String anVat;
    private String acUM;

    public ProductHistoryModel(String acIdent, String anPrice, String anNo, String acName, String anQty, String anVat, String acUM) {
        this.acIdent = acIdent;
        this.anPrice = anPrice;
        this.anNo = anNo;
        this.acName = acName;
        this.anQty = anQty;
        this.anVat = anVat;
        this.acUM = acUM;
    }


    public String getAcIdent() {
        return acIdent;
    }

    public String getAnPrice() {
        return anPrice;
    }

    public String getAcName() {
        return acName;
    }

    public String getAnNo() {
        return anNo;
    }

    public String getAnQty() {
        return anQty;
    }

    public String getAnVat() {
        return anVat;
    }

    public String getAcUM() {
        return acUM;
    }
}
