package com.example.commercialapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.roomDatabase.products.Product;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    public interface ProductAdapterItemClickListener {
        void onAddItemClick(int position);
    }

    public void setOnItemClickListener(ProductAdapterItemClickListener listener) {
        this.listener = listener;
    }

    private List<Product> products = new ArrayList<>();
    private ProductAdapterItemClickListener listener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);
        return new ProductHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product currentProduct = products.get(position);
        holder.textViewTitle.setText(currentProduct.getC());
        holder.textViewDescription.setText(currentProduct.getE());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProduct(int position) {
        try {
            return this.products.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageButton imageButtonAddItem;

        public ProductHolder(@NonNull View itemView, final ProductAdapterItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_productName);
            textViewDescription = itemView.findViewById(R.id.text_view_productDescription);
            imageButtonAddItem = itemView.findViewById(R.id.image_button_add_item);

            imageButtonAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAddItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
