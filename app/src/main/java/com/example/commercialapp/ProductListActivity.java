package com.example.commercialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.commercialapp.roomDatabase.deliveryPlaces.DeliveryPlace;
import com.example.commercialapp.roomDatabase.deliveryPlaces.DeliveryPlaceViewModel;
import com.example.commercialapp.roomDatabase.user.UserViewModel;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private DeliveryPlaceViewModel deliveryPlaceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        deliveryPlaceViewModel = ViewModelProviders.of(this).get(DeliveryPlaceViewModel.class);

        final TextView resultTextView = findViewById(R.id.text_view_number_of_places);

        deliveryPlaceViewModel.getAllDeliveryPlaces().observe(this, new Observer<List<DeliveryPlace>>() {
            @Override
            public void onChanged(List<DeliveryPlace> deliveryPlaces) {
                String text = resultTextView.getText().toString();
                text = text + " " + deliveryPlaces.size();
                resultTextView.setText(text);
            }
        });
    }

    public void onClickLogout(View view) {
        deliveryPlaceViewModel.deleteAll();
        userViewModel.deleteAll();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
