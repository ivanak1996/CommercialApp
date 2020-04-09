package com.example.commercialapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.commercialapp.R;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class ProductKeyboard {

    //private LinearLayout container;

    private LinearLayout firstRowContainer;
    private List<Button> numericButtons = new ArrayList<>();
    private Button decimalPointButton;

    private LinearLayout shortcutsContainer;
    private ImageView minusButton;
    private ImageView plusButton;
    private ImageView xButton;
    private ImageView infoButton;
    private ImageView deleteButton;

    private TextView resultTextView;

    private Product product = null;

    public ProductKeyboard(final Context context, Product product, LinearLayout container) {
        //this.container = container;

        this.product = product;

        firstRowSetup(context);
        secondRowSetup(context);
        container.addView(firstRowContainer);
        container.addView(shortcutsContainer);

        resultTextView.setText("" + product.getQuantity());
    }

    private void setProduct(Product product) {
        this.product = product;
        resultTextView.setText("" + product.getQuantity());
    }

    public void saveProductState(ProductViewModel productViewModel, long orderId) {
        if (product != null)
            productViewModel.insert(product, orderId);
    }

    private void secondRowSetup(Context context) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        p.weight = 2;
        shortcutsContainer = new LinearLayout(context);
        shortcutsContainer.setLayoutParams(p);
        shortcutsContainer.setOrientation(LinearLayout.HORIZONTAL);
        shortcutsContainer.setGravity(Gravity.CENTER_VERTICAL);

        minusButton = generateImageButton(context, R.drawable.ic_remove_circle);
        resultTextView = generateTextView(context);
        plusButton = generateImageButton(context, R.drawable.ic_add_circle);
        xButton = generateImageButton(context, R.drawable.ic_multi);
        infoButton = generateImageButton(context, R.drawable.ic_info);
        deleteButton = generateImageButton(context, R.drawable.ic_delete);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    int quantity = product.getQuantity();
                    if (quantity > 0) {
                        product.setQuantity(quantity - 1);
                        resultTextView.setText("" + (product.getQuantity()));
                    }
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    product.setQuantity(product.getQuantity() + 1);
                    resultTextView.setText("" + (product.getQuantity()));
                }
            }
        });

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    product.setQuantity(product.getQuantity() + 10);
                    resultTextView.setText("" + (product.getQuantity()));
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    product.setQuantity(0);
                    resultTextView.setText("0");
                }
            }
        });

        shortcutsContainer.addView(deleteButton);
        shortcutsContainer.addView(minusButton);
        shortcutsContainer.addView(resultTextView);
        shortcutsContainer.addView(plusButton);
        shortcutsContainer.addView(xButton);
        shortcutsContainer.addView(infoButton);

    }

    private void firstRowSetup(Context context) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        p.weight = 3;
        firstRowContainer = new LinearLayout(context);
        firstRowContainer.setLayoutParams(p);
        firstRowContainer.setOrientation(LinearLayout.HORIZONTAL);

        // add numeric buttons
        for (int i = 0; i <= 9; i++) {
            Button button = generateNewButton(context, "" + i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (product != null) {
                        int buttonValue = Integer.parseInt(((Button) v).getText().toString());
                        int quantity = product.getQuantity();
                        quantity = quantity * 10 + buttonValue;
                        product.setQuantity(quantity);
                        resultTextView.setText("" + quantity);
                    }
                }
            });
            numericButtons.add(new Button(context));
            firstRowContainer.addView(button);
        }

        if (product.getE().equals("KG")) {
            // add clear button
            decimalPointButton = generateNewButton(context, ".");
            decimalPointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });
            firstRowContainer.addView(decimalPointButton);
        }
    }

    private TextView generateTextView(Context context) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, 180);
        p.weight = 1;
        textView.setLayoutParams(p);
        textView.setText("res");
        textView.setTextSize(20.0f);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return textView;
    }

    private ImageView generateImageButton(Context context, int resourceId) {
        ImageView imageButton = new ImageView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, FILL_PARENT);
        p.weight = 1;
        imageButton.setLayoutParams(p);
        imageButton.setImageResource(resourceId);
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        return imageButton;
    }

    private Button generateNewButton(Context context, String label) {
        Button button = new Button(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, FILL_PARENT);
        p.weight = 1;
        p.leftMargin = -10;
        p.rightMargin = -10;
        button.setLayoutParams(p);
        button.setText(label);
        return button;
    }

}
