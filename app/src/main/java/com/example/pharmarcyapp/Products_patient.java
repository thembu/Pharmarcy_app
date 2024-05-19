package com.example.pharmarcyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Products_patient extends AppCompatActivity {

    public OkHttpClient client;
    public LinearLayout container;
    public EditText searchEditText;
    public Button searchButton;

    public String url = "https://lamp.ms.wits.ac.za/home/s2669308/Products.php";
    public JSONArray allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_patient);

        client = new OkHttpClient();
        container = findViewById(R.id.container);
        searchEditText = findViewById(R.id.editTextText2);
        searchButton = findViewById(R.id.Search);

        fetchProducts();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                filterProducts(query);
            }
        });
    }

    public void fetchProducts() {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String jsonNumbers = response.body().string();
                                allProducts = new JSONArray(jsonNumbers);
                                processJSON(allProducts);
                            } catch (IOException | JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void processJSON(JSONArray jsonArray) {
        container.removeAllViews();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String productName = item.getString("ProductName");
                String category = item.getString("Category");
                double price = item.getDouble("Price");
                String availableAt = item.getString("AvailableAt");
                int noOfItemsLeft = item.getInt("NoOfItemsLeft");

                // Create a new LinearLayout to hold each product detail and its checkbox
                LinearLayout productLayout = new LinearLayout(this);
                productLayout.setOrientation(LinearLayout.HORIZONTAL);
                productLayout.setPadding(8, 8, 8, 8);

                // Create a LinearLayout for product details
                LinearLayout detailsLayout = new LinearLayout(this);
                detailsLayout.setOrientation(LinearLayout.VERTICAL);
                detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                // Create and configure TextViews for product details
                TextView productTextView = new TextView(this);
                productTextView.setText("ProductName: " + productName);
                productTextView.setPadding(16, 16, 16, 16);

                TextView categoryTextView = new TextView(this);
                categoryTextView.setText("Category: " + category);
                categoryTextView.setPadding(16, 0, 16, 16);

                TextView priceTextView = new TextView(this);
                priceTextView.setText("Price: " + price);
                priceTextView.setPadding(16, 0, 16, 16);

                TextView availableAtTextView = new TextView(this);
                availableAtTextView.setText("AvailableAt: " + availableAt);
                availableAtTextView.setPadding(16, 0, 16, 16);

                TextView noOfItemsLeftTextView = new TextView(this);
                noOfItemsLeftTextView.setText("NoOfItemsLeft: " + noOfItemsLeft);
                noOfItemsLeftTextView.setPadding(16, 0, 16, 16);

                // Add TextViews to the detailsLayout
                detailsLayout.addView(productTextView);
                detailsLayout.addView(categoryTextView);
                detailsLayout.addView(priceTextView);
                detailsLayout.addView(availableAtTextView);
                detailsLayout.addView(noOfItemsLeftTextView);

                // Create and configure CheckBox
                CheckBox checkBox = new CheckBox(this);
                LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                checkBoxParams.setMargins(16, 0, 16, 0);
                checkBox.setLayoutParams(checkBoxParams);

                // Add detailsLayout and CheckBox to productLayout
                productLayout.addView(detailsLayout);
                productLayout.addView(checkBox);

                // Add productLayout to the container
                container.addView(productLayout);

                // Create a separator line
                View separator = new View(this);
                LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                separatorParams.setMargins(16, 8, 16, 8);
                separator.setLayoutParams(separatorParams);
                separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

                // Add the separator line to the container
                container.addView(separator);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void filterProducts(String query) {
        if (allProducts != null) {
            JSONArray filteredProducts = new JSONArray();
            for (int i = 0; i < allProducts.length(); i++) {
                try {
                    JSONObject item = allProducts.getJSONObject(i);
                    String productName = item.getString("ProductName");
                    if (productName.equalsIgnoreCase(query)) {
                        filteredProducts.put(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            processJSON(filteredProducts);
        }
    }

//    public void onViewOrdersButtonClick(View view) {
//        // Create an Intent to start the OrdersActivity
//        Intent intent = new Intent(this, OrdersActivity.class);
//
//        // Start the new activity
//        startActivity(intent);
//    }
}


