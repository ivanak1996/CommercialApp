package com.example.commercialapp.roomDatabase.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private String password;
    private String regId;
    private String serverUrl;

    public User(String username, String password, String regId, String serverUrl) {
        this.username = username;
        this.password = password;
        this.regId = regId;
        this.serverUrl = serverUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRegId() {
        return regId;
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
