package com.example.commercialapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.commercialapp.R;
import com.example.commercialapp.asyncResponses.GetOpenedOrderAsyncResponse;
import com.example.commercialapp.asyncResponses.InsertOrderAsyncResponse;
import com.example.commercialapp.roomDatabase.orders.Order;
import com.example.commercialapp.roomDatabase.orders.OrderViewModel;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.Date;

public class AddProductDialogFragment extends DialogFragment {

    public interface AddProductDialogListener extends GetOpenedOrderAsyncResponse, InsertOrderAsyncResponse {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    private EditText editTextQuantity;
    private AddProductDialogListener listener;
    private Product product;
    private OrderViewModel orderViewModel;
    private ProductViewModel productViewModel;

    public AddProductDialogFragment(Fragment fragment, Product product) {
        this.product = product;
        orderViewModel = ViewModelProviders.of(fragment).get(OrderViewModel.class);
        productViewModel = ViewModelProviders.of(fragment).get(ProductViewModel.class);
        this.listener = new AddProductDialogListener() {

            @Override
            public void insertOrderFinish(long id) {
                productViewModel.insert(AddProductDialogFragment.this.product, id);
            }

            @Override
            public void getOpenedOrderFinish(Order resultOrder) {
                Order order = resultOrder;
                if (order == null) {
                    order = new Order(Order.STATUS_OPEN, new Date(System.currentTimeMillis()));
                    orderViewModel.insert(order, this);
                    return;
                }
                productViewModel.insert(AddProductDialogFragment.this.product, order.getRowId());
            }

            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {

                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                AddProductDialogFragment.this.product.setQuantity(quantity);
                orderViewModel.getOpenedOrder(this);

                Toast toast = Toast.makeText(getContext(), "Product added", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_product_picked, null);
        TextView measurementTextView = dialogView.findViewById(R.id.dialog_textView_measurement);
        measurementTextView.setText(product.getE());
        editTextQuantity = dialogView.findViewById(R.id.dialog_edit_quantity);

        builder.setMessage("message")
                .setPositiveButton("positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(AddProductDialogFragment.this);
                    }
                })
                .setNegativeButton("negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(AddProductDialogFragment.this);
                    }
                }).setView(dialogView);
        return builder.create();
    }

}
