package com.example.commercialapp.asyncResponses;

import com.example.commercialapp.models.ProductModel;

import java.util.List;

public interface ProductListAsyncResponse {
    void processFinish(List<ProductModel> models);
}
