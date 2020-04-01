package com.example.commercialapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.JsonParser;
import com.example.commercialapp.R;
import com.example.commercialapp.dialogs.ProductDialogFragment;
import com.example.commercialapp.fragments.ProductListFragment;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    public interface ProductAdapterItemClickListener {
        void onPlusClick(int position);

        void onMinusClick(int position);

        void onPlusLongClick(int position);

        void onMinusLongClick(int Position);
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
        int quantity = currentProduct.getQuantity();
        holder.textViewTitle.setText(currentProduct.getC());
        holder.textViewDescription.setText(currentProduct.getE());

        if (quantity == 0) {
            holder.imageButtonMinusItem.setVisibility(View.GONE);
            holder.editTextProductQuantity.setVisibility(View.GONE);
        } else {
            holder.imageButtonMinusItem.setVisibility(View.VISIBLE);
            holder.editTextProductQuantity.setVisibility(View.VISIBLE);
            holder.editTextProductQuantity.setText(
                    currentProduct.getOrderRowId() == JsonParser.ID_PRODUCT_NOT_SAVED
                            ? "0" : quantity + "");
        }
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
        private ImageButton imageButtonPlusItem;
        private ImageButton imageButtonMinusItem;
        private TextView editTextProductQuantity;

        public ProductHolder(@NonNull View itemView, final ProductAdapterItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_productName);
            textViewDescription = itemView.findViewById(R.id.text_view_productDescription);
            imageButtonMinusItem = itemView.findViewById(R.id.image_button_minus_item);
            imageButtonPlusItem = itemView.findViewById(R.id.image_button_plus_item);
            editTextProductQuantity = itemView.findViewById(R.id.edit_text_quantity);

            imageButtonPlusItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPlusClick(position);
                        }
                    }
                }
            });

            imageButtonPlusItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPlusLongClick(position);
                        }
                    }
                    return false;
                }
            });

            imageButtonMinusItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMinusClick(position);
                        }
                    }
                }
            });

            imageButtonMinusItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMinusLongClick(position);
                        }
                    }
                    return false;
                }
            });

        }
    }

}
