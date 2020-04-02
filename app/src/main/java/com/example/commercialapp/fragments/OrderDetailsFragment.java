package com.example.commercialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.adapters.ProductAdapter;
import com.example.commercialapp.asyncResponses.GetOpenedOrderAsyncResponse;
import com.example.commercialapp.roomDatabase.orders.Order;
import com.example.commercialapp.roomDatabase.orders.OrderViewModel;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;
import com.example.commercialapp.utils.ProductKeyboard;

import java.util.List;

public class OrderDetailsFragment extends Fragment implements GetOpenedOrderAsyncResponse {

    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    private RecyclerView orderDetailsRecyclerView;
    private long orderId;
    private TextView noDataInRecyclerView;

    private LinearLayout keyboardLayout;
    private ProductKeyboard keyboard;

    private Product selectedProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        // display for empty basket
        noDataInRecyclerView = view.findViewById(R.id.empty_view);

        orderDetailsRecyclerView = view.findViewById(R.id.recycler_view_order_details);
        orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderDetailsRecyclerView.setHasFixedSize(true);

        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener(new ProductAdapter.ProductAdapterItemClickListener() {
            @Override
            public void onClick(int position) {
                Product clickedProduct = productAdapter.getProduct(position);
                if (selectedProduct == null || selectedProduct != clickedProduct) {
                    selectedProduct = clickedProduct;
                    keyboardLayout.setVisibility(View.VISIBLE);
                    keyboard.saveProductState(productViewModel, orderId);
                    keyboard.setProduct(selectedProduct);
                } else {
                    selectedProduct = null;
                    keyboard.saveProductState(productViewModel, orderId);
                    keyboardLayout.setVisibility(View.GONE);
                }
            }
        });
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProductsInOpenedOrder().observe(OrderDetailsFragment.this.getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productAdapter.setProducts(products);
                refreshRecyclerViewAppearance();
            }
        });
        orderDetailsRecyclerView.setAdapter(productAdapter);

        // get opened order into which the user shall add the items
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.getOpenedOrder(this);

        Button nextButton = view.findViewById(R.id.button_next_order_details);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.orderExtraFragment);
            }
        });

        // keyboard setup
        keyboardLayout = view.findViewById(R.id.layout_keyboard);
        keyboard = new ProductKeyboard(getContext(), keyboardLayout);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.keyboard.saveProductState(productViewModel, orderId);
    }

    @Override
    public void getOpenedOrderFinish(Order order) {
        this.orderId = order.getRowId();
    }

    private void refreshRecyclerViewAppearance() {
        if (productAdapter.getItemCount() == 0) {
            orderDetailsRecyclerView.setVisibility(View.GONE);
            noDataInRecyclerView.setVisibility(View.VISIBLE);
        } else {
            orderDetailsRecyclerView.setVisibility(View.VISIBLE);
            noDataInRecyclerView.setVisibility(View.GONE);
        }
    }
}
