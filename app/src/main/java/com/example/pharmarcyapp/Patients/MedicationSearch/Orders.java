package com.example.pharmarcyapp.Patients.MedicationSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pharmarcyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Orders extends AppCompatActivity {

    OkHttpClient client;

    public LinearLayout container;
    public JSONArray allProducts;

    public String url = "https://lamp.ms.wits.ac.za/home/s2669308/ViewOrders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        client = new OkHttpClient();
        container = findViewById(R.id.containerViewOrders);

        fetchProducts();
    }

    public void fetchProducts() {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        try {
                            assert response.body() != null;
                            String jsonNumbers = response.body().string();
                            allProducts = new JSONArray(jsonNumbers);
                            processJSON(allProducts);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void processJSON(JSONArray jsonArray) {
        container.removeAllViews();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String productName = item.getString("ProductName");
                double price = item.getDouble("Price");
                double noOfItemsLeft = item.getDouble("NoOfItems");
                String deliveryOption = item.getString("DeliveryOption");

                // Create a new LinearLayout to hold each product detail and its checkbox
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

                // Create and configure TextViews for product details
                TextView productTextView = new TextView(this);
                productTextView.setText("ProductName: " + productName);
                productTextView.setPadding(16, 16, 16, 16);
                productTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));

                detailsLayout.addView(productTextView);


                TextView priceTextView = new TextView(this);
                priceTextView.setText("Price: " + price);
                priceTextView.setPadding(16, 0, 16, 16);

                TextView noOfItemsLeftTextView = new TextView(this);
                noOfItemsLeftTextView.setText("NoOfItems: " + noOfItemsLeft);
                noOfItemsLeftTextView.setPadding(16, 0, 16, 16);

                TextView deliveryTextView = new TextView(this);
                deliveryTextView.setText("DeliveryOption: " + deliveryOption);
                deliveryTextView.setPadding(16, 0, 16, 16);


                productLayout.addView(detailsLayout);
                productLayout.addView(priceTextView);
                productLayout.addView(noOfItemsLeftTextView);
                productLayout.addView(deliveryTextView);

                // Add productLayout to the container
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
}
