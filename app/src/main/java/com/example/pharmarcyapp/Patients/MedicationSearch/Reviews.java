package com.example.pharmarcyapp.Patients.MedicationSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Reviews extends AppCompatActivity {

    public String url = "https://lamp.ms.wits.ac.za/home/s2669308/Reviews.php";
    public LinearLayout container;
    public String productName;

    OkHttpClient client;
    public JSONArray allReviews;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);

        client = new OkHttpClient();
        container = findViewById(R.id.containerViewReviews);
        textView = findViewById(R.id.textView);


        productName = getIntent().getStringExtra("ProductName");

        textView.setText("Reviews for " + productName);

        fetchReviews();
    }

    public void fetchReviews() {
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
                                allReviews = new JSONArray(jsonNumbers);
                                filterAndDisplayReviews(allReviews, productName);
                            } catch (IOException | JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void filterAndDisplayReviews(JSONArray reviewsArray, String productName) {
        container.removeAllViews();
        try {
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject item = reviewsArray.getJSONObject(i);
                if (item.getString("ProductName").equalsIgnoreCase(productName)) {
                    String customerName = item.getString("CustomerName");
                    String review = item.getString("Review");


                    LinearLayout reviewLayout = new LinearLayout(this);
                    reviewLayout.setOrientation(LinearLayout.VERTICAL);
                    reviewLayout.setPadding(8, 8, 8, 8);

                    LinearLayout detailsLayout = new LinearLayout(this);
                    detailsLayout.setOrientation(LinearLayout.HORIZONTAL);
                    detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));


                    TextView customerTextView = new TextView(this);
                    customerTextView.setText("Customer: " + customerName);
                    customerTextView.setPadding(16, 16, 16, 16);
                    customerTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                    ));

                    detailsLayout.addView(customerTextView);

                    TextView reviewTextView = new TextView(this);
                    reviewTextView.setText(review);
                    reviewTextView.setPadding(16, 0, 16, 16);


                    reviewLayout.addView(detailsLayout);
                    reviewLayout.addView(reviewTextView);


                    container.addView(reviewLayout);


                    View separator = new View(this);
                    LinearLayout.LayoutParams separatorParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    );
                    separatorParams.setMargins(16, 8, 16, 8);
                    separator.setLayoutParams(separatorParams);
                    separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

                    container.addView(separator);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
