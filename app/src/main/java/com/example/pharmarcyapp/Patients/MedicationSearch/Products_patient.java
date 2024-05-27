package com.example.pharmarcyapp.Patients.MedicationSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmarcyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Products_patient extends AppCompatActivity {

    public OkHttpClient client;
    public LinearLayout container;
    public EditText searchEditText;
    public Button searchButton;
    public Button orderButton;

    public Button viewOrdersButton;

    public String url = "https://lamp.ms.wits.ac.za/home/s2669308/Products.php";
    public String orderUrl = "https://lamp.ms.wits.ac.za/home/s2669308/Orders.php";
    public JSONArray allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        client = new OkHttpClient();
        container = findViewById(R.id.container);
        searchEditText = findViewById(R.id.editTextText2);
        searchButton = findViewById(R.id.Search);
        orderButton = findViewById(R.id.buttonOD);
        viewOrdersButton = findViewById(R.id.viewOrd);

        fetchProducts();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                filterProducts(query);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Products_patient.this, Orders.class);
                startActivity(intent);
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
                String productDescription = item.getString("ProductDescription");

                LinearLayout productLayout = new LinearLayout(this);
                productLayout.setOrientation(LinearLayout.VERTICAL);
                productLayout.setPadding(8, 8, 8, 8);

                // Create a LinearLayout for product details
                LinearLayout detailsLayout = new LinearLayout(this);
                detailsLayout.setOrientation(LinearLayout.HORIZONTAL);
                detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                TextView productTextView = new TextView(this);
                productTextView.setText("ProductName: " + productName);
                productTextView.setPadding(16, 16, 16, 16);
                productTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                CheckBox checkBox = new CheckBox(this);
                checkBox.setTag(item); // Store the item JSON object in the checkbox tag
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                checkBox.setGravity(Gravity.END);

                detailsLayout.addView(productTextView);
                detailsLayout.addView(checkBox);

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

                LinearLayout buttonsLayout = new LinearLayout(this);
                buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                buttonsLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                Button reviewButton = new Button(this);
                reviewButton.setText("View Reviews");
                reviewButton.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                reviewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Products_patient.this, Reviews.class);
                        intent.putExtra("ProductName", productName); // Pass the product name to the Reviews activity
                        startActivity(intent);
                    }
                });

                Button descriptionButton = new Button(this);
                descriptionButton.setText("Product Description");
                descriptionButton.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                descriptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(Products_patient.this)
                                .setTitle(productName + " Description")
                                .setMessage(productDescription)
                                .setPositiveButton("OK", null)
                                .show();
                    }
                });

                buttonsLayout.addView(reviewButton);
                buttonsLayout.addView(descriptionButton);

                // Add detailsLayout, CheckBox, and buttonsLayout to productLayout
                productLayout.addView(detailsLayout);
                productLayout.addView(categoryTextView);
                productLayout.addView(priceTextView);
                productLayout.addView(availableAtTextView);
                productLayout.addView(noOfItemsLeftTextView);
                productLayout.addView(buttonsLayout);
                container.addView(productLayout);

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

    public void placeOrder() {
        JSONArray selectedItems = new JSONArray();

        for (int i = 0; i < container.getChildCount(); i++) {
            View productLayout = container.getChildAt(i);
            if (productLayout instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) productLayout;
                for (int j = 0; j < layout.getChildCount(); j++) {
                    View view = layout.getChildAt(j);
                    if (view instanceof LinearLayout) {
                        LinearLayout detailsLayout = (LinearLayout) view;
                        for (int k = 0; k < detailsLayout.getChildCount(); k++) {
                            View detailsView = detailsLayout.getChildAt(k);
                            if (detailsView instanceof CheckBox) {
                                CheckBox checkBox = (CheckBox) detailsView;
                                if (checkBox.isChecked()) {
                                    JSONObject item = (JSONObject) checkBox.getTag();

                                    try {
                                        item.put("NoOfItems", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    selectedItems.put(item);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (selectedItems.length() > 0) {
            System.out.println("Selected Items: " + selectedItems.toString()); // Log selected items
            showDeliveryOptionsDialog(selectedItems);
        } else {
            Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show();
        }
    }



    public void showDeliveryOptionsDialog(JSONArray selectedItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Delivery Option")
                .setMessage("Please choose your delivery option:")
                .setPositiveButton("In-Store Pickup", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            for (int i = 0; i < selectedItems.length(); i++) {
                                selectedItems.getJSONObject(i).put("DeliveryOption", "In-Store Pickup");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sendOrderToServer(selectedItems);
                    }
                })
                .setNegativeButton("Home Delivery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            for (int i = 0; i < selectedItems.length(); i++) {
                                selectedItems.getJSONObject(i).put("DeliveryOption", "Home Delivery");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sendOrderToServer(selectedItems);
                    }
                })
                .show();
    }

    public void sendOrderToServer(JSONArray selectedItems) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(selectedItems.toString(), JSON);

        Request request = new Request.Builder()
                .url(orderUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Products_patient.this, "Order failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string(); // Capture the response body
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(Products_patient.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        System.out.println("Success: " + responseBody); // Print the successful response
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(Products_patient.this, "Order failed", Toast.LENGTH_SHORT).show();
                        System.out.println("Error: " + responseBody); // Print the error response
                    });
                }
            }
        });
    }
}
