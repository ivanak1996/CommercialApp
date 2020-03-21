package com.example.commercialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.example.commercialapp.roomDatabase.user.UserViewModel;

public class MainActivity extends AppCompatActivity implements UsersCountAsyncResponse {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.usersCount(this);
    }

    @Override
    public void processFinish(int output) {
        if(output == 0) {
            // go to login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            // go directly to products
            Intent intent = new Intent(this, ProductListActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
