package com.example.commercialapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<ProductModel> products = new ArrayList<>();

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ProductHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        ProductModel currentProduct = products.get(position);
        holder.textViewTitle.setText(currentProduct.getC());
        holder.textViewDescription.setText(currentProduct.getE());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_productName);
            textViewDescription = itemView.findViewById(R.id.text_view_productDescription);
        }
    }
}
