package com.example.commercialapp;

import com.example.commercialapp.roomDatabase.user.User;

public interface AsyncResponse {
    void processFinish(User output);
}
