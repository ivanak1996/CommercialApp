package com.example.commercialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.commercialapp.roomDatabase.user.UserViewModel;

public class ProductListActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    public void onClickLogout(View view) {
        userViewModel.deleteAll();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
