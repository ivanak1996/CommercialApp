package com.example.commercialapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.commercialapp.R;
import com.example.commercialapp.roomDatabase.products.Product;

// dialog that allows user not only to change quantity, but also remove item from the basket
public class SavedProductDialogFragment extends ProductDialogFragment {

    public SavedProductDialogFragment(Fragment fragment, Product product) {
        super(fragment, product);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_order_product_preview, null);
        TextView measurementTextView = dialogView.findViewById(R.id.dialog_order_textView_measurement);
        measurementTextView.setText(product.getE());
        editTextQuantity = dialogView.findViewById(R.id.dialog_order_edit_quantity);
        editTextQuantity.setText("" + product.getQuantity());
        ImageButton imageButtonDelete = dialogView.findViewById(R.id.image_button_delete);

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productViewModel.delete(product);
                dismiss();
                Toast toast = Toast.makeText(getContext(), "Product removed", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        builder.setMessage("Add item")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(SavedProductDialogFragment.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(SavedProductDialogFragment.this);
                    }
                }).setView(dialogView);
        return builder.create();
    }
}
