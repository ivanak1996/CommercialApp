package com.example.commercialapp.dialogs;

import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.commercialapp.R;
import com.example.commercialapp.asyncResponses.GetOpenedOrderAsyncResponse;
import com.example.commercialapp.roomDatabase.orders.Order;
import com.example.commercialapp.roomDatabase.orders.OrderViewModel;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.Date;

class AddProductDialogListener implements DialogListener, GetOpenedOrderAsyncResponse {

    private OrderViewModel orderViewModel;
    private ProductViewModel productViewModel;
    private Product product;

    public AddProductDialogListener(Fragment fragment, Product product) {
        orderViewModel = ViewModelProviders.of(fragment).get(OrderViewModel.class);
        productViewModel = ViewModelProviders.of(fragment).get(ProductViewModel.class);
        this.product = product;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText editTextQuantity = dialog.getView().findViewById(R.id.dialog_edit_quantity);
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        product.setQuantity(quantity);
        orderViewModel.getOpenedOrder(this);

        Toast toast = Toast.makeText(dialog.getContext(), "Product added", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void getOpenedOrderFinish(Order resultOrder) {
        Order order = resultOrder;
        if (order == null) {
            order = new Order(Order.STATUS_OPEN, new Date(System.currentTimeMillis()));
            orderViewModel.insert(order);
        }
        productViewModel.insert(product, order.getRowId());
    }

}
