package com.example.commercialapp.roomDatabase.deliveryPlaces;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "deliveryPlace_table")
public class DeliveryPlace {

    @PrimaryKey(autoGenerate = true)
    private int rowId;
    private int userRowId;

    private String acName2;
    private String acAddress;
    private String acCity;
    private String anQId;
    private String acPost;
    private String acSubject;

    public DeliveryPlace(int userRowId, String acName2, String acAddress, String acCity, String anQId, String acPost, String acSubject) {
        this.userRowId = userRowId;
        this.acName2 = acName2;
        this.acAddress = acAddress;
        this.acCity = acCity;
        this.anQId = anQId;
        this.acPost = acPost;
        this.acSubject = acSubject;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    // region getters

    public int getRowId() {
        return rowId;
    }

    public int getUserRowId() {
        return userRowId;
    }

    public String getAcName2() {
        return acName2;
    }

    public String getAcAddress() {
        return acAddress;
    }

    public String getAcCity() {
        return acCity;
    }

    public String getAnQId() {
        return anQId;
    }

    public String getAcPost() {
        return acPost;
    }

    public String getAcSubject() {
        return acSubject;
    }

    // endregion
}
