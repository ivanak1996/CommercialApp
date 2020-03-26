package com.example.commercialapp.roomDatabase.products;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "products_table", indices = {@Index(value = {"orderRowId", "b"}, unique = true)})
public class Product {
    @PrimaryKey(autoGenerate = true)
    private long rowId;

    private int quantity;
    private long orderRowId;

    private String a; //id,
    private String b; //sifra prozivda
    private String c; //naziv
    private String d; //grupa proizvoda
    private String e; //jedinica mere
    private String f; //barkod
    private String v; //pdv stopa
    private String p; //cena
    private String r; //rabat
    private String i; //url do slike

    public Product(long orderRowId, int quantity, String a, String b, String c, String d, String e, String f, String v, String p, String r, String i) {
        this.orderRowId = orderRowId;
        this.quantity = quantity;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.v = v;
        this.p = p;
        this.r = r;
        this.i = i;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public void setOrderRowId(long orderRowId) {
        this.orderRowId = orderRowId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // region getters


    public long getRowId() {
        return rowId;
    }

    public long getOrderRowId() {
        return orderRowId;
    }

    public int getQuantity() {
        return quantity;
    }


    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public String getE() {
        return e;
    }

    public String getF() {
        return f;
    }

    public String getV() {
        return v;
    }

    public String getP() {
        return p;
    }

    public String getR() {
        return r;
    }

    public String getI() {
        return i;
    }

    // endregion
}
