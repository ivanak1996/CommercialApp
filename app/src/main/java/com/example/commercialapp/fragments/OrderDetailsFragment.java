package com.example.commercialapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.adapters.OrderDetailsAdapter;
import com.example.commercialapp.dialogs.SavedProductDialogFragment;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.List;

public class OrderDetailsFragment extends Fragment {

    private OrderDetailsAdapter orderDetailsAdapter;
    private ProductViewModel productViewModel;
    private RecyclerView orderDetailsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        orderDetailsRecyclerView = view.findViewById(R.id.recycler_view_order_details);
        orderDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderDetailsRecyclerView.setHasFixedSize(true);

        orderDetailsAdapter = new OrderDetailsAdapter();
        orderDetailsAdapter.setOnItemClickListener(new OrderDetailsAdapter.OrderDetailsClickListener() {
            @Override
            public void onItemClick(int position) {
                Product product = orderDetailsAdapter.getProduct(position);
                DialogFragment newFragment = new SavedProductDialogFragment(OrderDetailsFragment.this, product);
                newFragment.show(getActivity().getSupportFragmentManager(), "missiles");
            }
        });
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProductsInOpenedOrder().observe(OrderDetailsFragment.this.getActivity(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                orderDetailsAdapter.setProducts(products);
            }
        });
        orderDetailsRecyclerView.setAdapter(orderDetailsAdapter);

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

}
