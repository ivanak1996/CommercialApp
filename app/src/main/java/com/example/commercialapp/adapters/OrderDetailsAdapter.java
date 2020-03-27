package com.example.commercialapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commercialapp.R;
import com.example.commercialapp.roomDatabase.products.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsHolder> {

    public interface OrderDetailsClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OrderDetailsClickListener listener) {
        this.listener = listener;
    }

    private List<Product> products = new ArrayList<>();
    private OrderDetailsClickListener listener;

    @NonNull
    @Override
    public OrderDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_list_item, parent, false);
        return new OrderDetailsHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsHolder holder, int position) {
        Product currentProduct = products.get(position);
        holder.textViewTitle.setText(currentProduct.getC());
        holder.textViewDescription.setText(currentProduct.getQuantity() + " " + currentProduct.getE());
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

    class OrderDetailsHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageButton imageButtonDetails;

        public OrderDetailsHolder(@NonNull View itemView, final OrderDetailsClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_order_productName);
            textViewDescription = itemView.findViewById(R.id.text_view_order_productDescription);
            imageButtonDetails = itemView.findViewById(R.id.image_button_details);

            imageButtonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
