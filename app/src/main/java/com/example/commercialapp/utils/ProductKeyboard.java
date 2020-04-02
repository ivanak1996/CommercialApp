package com.example.commercialapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.commercialapp.R;
import com.example.commercialapp.roomDatabase.products.Product;
import com.example.commercialapp.roomDatabase.products.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class ProductKeyboard {

    //private LinearLayout container;

    private LinearLayout titleRow;
    private TextView titleTextView;

    private LinearLayout firstRowContainer;
    private List<Button> numericButtons = new ArrayList<>();
    private Button clearButton;

    private LinearLayout shortcutsContainer;
    private ImageButton minusButton;
    private ImageButton plusButton;
    private ImageButton xButton;
    private ImageButton infoButton;
    private TextView resultTextView;

    private Product product = null;

    public ProductKeyboard(final Context context, LinearLayout container) {
        //this.container = container;

        titleRowSetup(context);
        firstRowSetup(context);
        secondRowSetup(context);
        container.addView(titleRow);
        container.addView(firstRowContainer);
        container.addView(shortcutsContainer);
    }

    public void setProduct(Product product) {
        this.product = product;
        titleTextView.setText(product.getC());
        resultTextView.setText("" + product.getQuantity());

    }

    public void saveProductState(ProductViewModel productViewModel, long orderId) {
        if (product != null)
            productViewModel.insert(product, orderId);
    }

    private void titleRowSetup(Context context) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        titleRow = new LinearLayout(context);
        titleTextView = generateTextView(context);
        titleTextView.setText("Title");
        titleRow.addView(titleTextView);
    }

    private void secondRowSetup(Context context) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        p.weight = 1;
        shortcutsContainer = new LinearLayout(context);
        shortcutsContainer.setLayoutParams(p);
        shortcutsContainer.setOrientation(LinearLayout.HORIZONTAL);

        minusButton = generateImageButton(context, R.drawable.ic_remove_circle);
        resultTextView = generateTextView(context);
        plusButton = generateImageButton(context, R.drawable.ic_add_circle);
        xButton = generateImageButton(context, R.drawable.ic_multi);
        infoButton = generateImageButton(context, R.drawable.ic_info);

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

        shortcutsContainer.addView(minusButton);
        shortcutsContainer.addView(resultTextView);
        shortcutsContainer.addView(plusButton);
        shortcutsContainer.addView(xButton);
        shortcutsContainer.addView(infoButton);

    }

    private void firstRowSetup(Context context) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        p.weight = 1;
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

        // add clear button
        clearButton = generateNewButton(context, "X");
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    product.setQuantity(0);
                    resultTextView.setText("");
                }
            }
        });
        firstRowContainer.addView(clearButton);
    }

    private TextView generateTextView(Context context) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, WRAP_CONTENT);
        p.weight = 1;
        textView.setLayoutParams(p);
        textView.setText("res");
        return textView;
    }

    private ImageButton generateImageButton(Context context, int resourceId) {
        ImageButton imageButton = new ImageButton(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, WRAP_CONTENT);
        p.weight = 1;
        imageButton.setLayoutParams(p);
        imageButton.setImageResource(resourceId);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        return imageButton;
    }

    private Button generateNewButton(Context context, String label) {
        Button button = new Button(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, WRAP_CONTENT);
        p.weight = 1;
        button.setLayoutParams(p);
        button.setText(label);
        return button;
    }

}
