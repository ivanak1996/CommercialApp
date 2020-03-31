package com.example.commercialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.adapters.ProductAdapter;
import com.example.commercialapp.asyncResponses.GetOpenedOrderAsyncResponse;
import com.example.commercialapp.dialogs.ProductDialogFragment;
import com.example.commercialapp.roomDatabase.orders.Order;
import com.example.commercialapp.roomDatabase.orders.OrderViewModel;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.List;

public class OrderDetailsFragment extends Fragment implements GetOpenedOrderAsyncResponse {

    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private OrderViewModel orderViewModel;
    private RecyclerView orderDetailsRecyclerView;
    private long orderId;
    private TextView noDataInRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        noDataInRecyclerView = view.findViewById(R.id.empty_view);

        orderDetailsRecyclerView = view.findViewById(R.id.recycler_view_order_details);
        orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderDetailsRecyclerView.setHasFixedSize(true);

        productAdapter = new ProductAdapter();
        productAdapter.setOnItemClickListener(new ProductAdapter.ProductAdapterItemClickListener() {
            @Override
            public void onPlusClick(int position) {
                Product product = productAdapter.getProduct(position);
                product.setQuantity(product.getQuantity() + 1);
                productViewModel.insert(product, orderId);
                refreshRecyclerViewAppearance();
            }

            @Override
            public void onMinusClick(int position) {
                Product product = productAdapter.getProduct(position);
                int quantity = product.getQuantity();
                if (quantity <= 1) {
                    product.setQuantity(0);
                    productViewModel.delete(product);
                } else {
                    product.setQuantity(quantity - 1);
                    productViewModel.insert(product, orderId);
                }
                refreshRecyclerViewAppearance();
            }

            @Override
            public void onPlusLongClick(int position) {
                Product product = productAdapter.getProduct(position);
                DialogFragment newFragment = new ProductDialogFragment(OrderDetailsFragment.this, product, orderId);
                newFragment.show(getActivity().getSupportFragmentManager(), "missiles");
                refreshRecyclerViewAppearance();
            }

            @Override
            public void onMinusLongClick(int position) {
                Product product = productAdapter.getProduct(position);
                product.setQuantity(0);
                productViewModel.delete(product);
                refreshRecyclerViewAppearance();
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

        return view;
    }

    @Override
    public void getOpenedOrderFinish(Order order) {
        this.orderId = order.getRowId();
    }

    private void refreshRecyclerViewAppearance() {
        if(productAdapter.getItemCount() == 0) {
            orderDetailsRecyclerView.setVisibility(View.GONE);
            noDataInRecyclerView.setVisibility(View.VISIBLE);
        } else {
            orderDetailsRecyclerView.setVisibility(View.VISIBLE);
            noDataInRecyclerView.setVisibility(View.GONE);
        }
    }
}
