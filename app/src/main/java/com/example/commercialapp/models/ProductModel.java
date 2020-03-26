package com.example.commercialapp.models;

class ProductModel {

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

    public ProductModel(String a, String b, String c, String d, String e, String f, String v, String p, String r, String i) {
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

    // region getters

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
