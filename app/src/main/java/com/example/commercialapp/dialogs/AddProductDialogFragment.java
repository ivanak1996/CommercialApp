package com.example.commercialapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.commercialapp.R;
import com.example.commercialapp.models.ProductModel;

public class AddProductDialogFragment extends DialogFragment {

    public interface AddProductDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    private AddProductDialogListener listener;
    private ProductModel product;

    public AddProductDialogFragment(ProductModel product) {
        this.product = product;
        this.listener = new AddProductDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddProductDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
