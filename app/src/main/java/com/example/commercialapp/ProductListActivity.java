package com.example.commercialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.*;
import androidx.navigation.ui.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.commercialapp.roomDatabase.deliveryPlaces.DeliveryPlaceViewModel;
import com.example.commercialapp.roomDatabase.user.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Set;

public class ProductListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String KEY_ORDER_ID = "KEY_ORDER_ID";

    private UserViewModel userViewModel;
    private DeliveryPlaceViewModel deliveryPlaceViewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        deliveryPlaceViewModel = ViewModelProviders.of(this).get(DeliveryPlaceViewModel.class);

        init();

    }

    private void init() {
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.productListScreenFragment);
        topLevelDestinations.add(R.id.anotherFragment);

        appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
                .setDrawerLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void logout() {
        deliveryPlaceViewModel.deleteAll();
        userViewModel.deleteAll();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_logout: {
                logout();
                break;
            }
            case R.id.nav_products: {
                // nav options to clear backstack
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main, false)
                        .build();
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.productListScreenFragment, null, navOptions);
                break;
            }
            case R.id.nav_orders: {

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main, true)
                        .build();
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(R.id.anotherFragment, null, navOptions);
                break;
            }
        }
        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    return false;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration);
    }

}
