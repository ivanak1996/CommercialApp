package com.example.commercialapp.roomDatabase.products;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.commercialapp.roomDatabase.CommercialDatabase;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application) {
        CommercialDatabase database = CommercialDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(final Product product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                productDao.insert(product);
            }
        }).start();
    }

    public void update(final Product product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                productDao.update(product);
            }
        }).start();
    }

    public void delete(final Product product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                productDao.delete(product);
            }
        }).start();
    }

    public LiveData<List<Product>> getAllProductsForOrder(long orderRowId) {
        return productDao.getAllProductsForOrder(orderRowId);
    }

}
