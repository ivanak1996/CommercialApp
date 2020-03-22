package com.example.commercialapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.JsonParser;
import com.example.commercialapp.asyncResponses.GetUserFromDbAsyncResponse;
import com.example.commercialapp.asyncResponses.ProductListAsyncResponse;
import com.example.commercialapp.R;
import com.example.commercialapp.adapters.ProductAdapter;
import com.example.commercialapp.models.ProductModel;
import com.example.commercialapp.roomDatabase.user.User;
import com.example.commercialapp.roomDatabase.user.UserViewModel;

import java.util.List;

public class ProductListFragment extends Fragment implements ProductListAsyncResponse, GetUserFromDbAsyncResponse {

    private RecyclerView productListRecyclerView;
    private ProductAdapter productListAdapter;

    private LinearLayout loading;
    private LinearLayout loaded;

    private UserViewModel userViewModel;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        productListRecyclerView = view.findViewById(R.id.recycler_view_productsList);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productListRecyclerView.setHasFixedSize(true);

        productListAdapter = new ProductAdapter();
        productListRecyclerView.setAdapter(productListAdapter);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser(this);

        SearchView searchView = view.findViewById(R.id.search_view_products);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GetProductListFromApiAsyncTask(ProductListFragment.this, user.getEmail(), user.getPassword(), query).execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        loading = view.findViewById(R.id.product_list_loading);
        loaded = view.findViewById(R.id.product_list_loading);
        loading.setVisibility(View.VISIBLE);
        loaded.setVisibility(View.GONE);

        return view;

    }

    @Override
    public void processFinish(List<ProductModel> models) {
        productListAdapter.setProducts(models);
    }

    @Override
    public void processFinish(User output) {
        user = output;
        loaded.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private static class GetProductListFromApiAsyncTask extends AsyncTask<Void, Void, List<ProductModel>> {

        private ProductListAsyncResponse delegate;
        private String username;
        private String password;
        private String keyword;

        public GetProductListFromApiAsyncTask(ProductListAsyncResponse delegate, String username, String password, String keyword) {
            this.delegate = delegate;
            this.username = username;
            this.password = password;
            this.keyword = keyword;
        }

        @Override
        protected List<ProductModel> doInBackground(Void... voids) {
            return JsonParser.getProductsFromApi(username, password, keyword);
        }

        @Override
        protected void onPostExecute(List<ProductModel> productModels) {
            delegate.processFinish(productModels);
        }
    }
}
