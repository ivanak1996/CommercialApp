package com.example.commercialapp.fragments.orderHistoryFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.JsonParser;
import com.example.commercialapp.ProductListActivity;
import com.example.commercialapp.R;
import com.example.commercialapp.adapters.OrderedProductsAdapter;
import com.example.commercialapp.asyncResponses.GetOrderItemsAsyncResponse;
import com.example.commercialapp.models.orderHistoryModels.ProductHistoryModel;
import com.example.commercialapp.roomDatabase.user.User;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryDetailsFragment extends Fragment implements GetOrderItemsAsyncResponse {

    private User user;
    private String orderId;
    private OrderedProductsAdapter productsAdapter;
    private RecyclerView ordersHistoryRecyclerView;
    private TextView noDataInRecyclerView;

    public OrderHistoryDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_details, container, false);
        orderId = getArguments().getString("acKey");
        user = ((ProductListActivity) getActivity()).getUser();
        new GetOrderHistoryDetailsFromApiAsyncTask(this, user.getEmail(), user.getPassword(), orderId).execute();

        // display for empty basket
        noDataInRecyclerView = view.findViewById(R.id.empty_order_history_details);

        ordersHistoryRecyclerView = view.findViewById(R.id.recycler_view_order_history_details);
        ordersHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersHistoryRecyclerView.setHasFixedSize(true);

        productsAdapter = new OrderedProductsAdapter();
        ordersHistoryRecyclerView.setAdapter(productsAdapter);
        return view;
    }

    @Override
    public void finish(List<ProductHistoryModel> products) {
        productsAdapter.setProducts(products);
        productsAdapter.notifyDataSetChanged();
        if (products.size() == 0) {
            ordersHistoryRecyclerView.setVisibility(View.GONE);
            noDataInRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noDataInRecyclerView.setVisibility(View.GONE);
            ordersHistoryRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private static class GetOrderHistoryDetailsFromApiAsyncTask extends AsyncTask<Void, Void, List<ProductHistoryModel>> {

        private GetOrderItemsAsyncResponse delegate;
        private String username;
        private String password;
        private String orderId;

        public GetOrderHistoryDetailsFromApiAsyncTask(GetOrderItemsAsyncResponse delegate, String username, String password, String orderId) {
            this.delegate = delegate;
            this.username = username;
            this.password = password;
            this.orderId = orderId;
        }

        @Override
        protected List<ProductHistoryModel> doInBackground(Void... voids) {
            return JsonParser.getOrderHistoryDetailsFromApi(username, password, orderId);
        }

        @Override
        protected void onPostExecute(List<ProductHistoryModel> serverModels) {
            delegate.finish((serverModels != null) ? serverModels : new ArrayList<ProductHistoryModel>());
        }
    }
}
