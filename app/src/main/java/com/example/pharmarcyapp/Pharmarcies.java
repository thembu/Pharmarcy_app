package com.example.pharmarcyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pharmarcies extends AppCompatActivity {

    private static final String TAG = "Pharmacies";
    private OkHttpClient client;
    private EditText locationEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmarcies);

        client = new OkHttpClient();
        locationEditText = findViewById(R.id.locationEditText);
        resultTextView = findViewById(R.id.resultTextView);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationName = locationEditText.getText().toString().trim();
                if (!locationName.isEmpty()) {
                    searchPharmacies(locationName);
                }
            }
        });
    }
    private void searchPharmacies(String locationName) {
        // Clear previous results
        resultTextView.setText("");

        // Build the URL for the text search request
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/textsearch/json").newBuilder();
        urlBuilder.addQueryParameter("query", "pharmacy in " + locationName);
        urlBuilder.addQueryParameter("key", "AIzaSyCCxkJSJvdtLyGkZe3v-vPV7WrnagacMKk");

        String url = urlBuilder.build().toString();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the JSON response and get place_id for each pharmacy
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray results = jsonObject.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject place = results.getJSONObject(i);
                            String placeId = place.getString("place_id");
                            getPlaceDetails(placeId);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Error: " + response.code() + " " + response.message());
                }
            }
        });
    }


    private void getPlaceDetails(String placeId) {
        // Build the URL for the Place Details request
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/details/json").newBuilder();
        urlBuilder.addQueryParameter("place_id", placeId);
        urlBuilder.addQueryParameter("fields", "name,formatted_address,formatted_phone_number");
        urlBuilder.addQueryParameter("key", "AIzaSyCCxkJSJvdtLyGkZe3v-vPV7WrnagacMKk");

        String url = urlBuilder.build().toString();

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the JSON response and update UI
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONObject result = jsonObject.getJSONObject("result");
                        final String name = result.getString("name");
                        final String address = result.getString("formatted_address");
                        final String phoneNumber = result.optString("formatted_phone_number", "Phone number not available");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTextView.append("Name: " + name + "\n");
                                resultTextView.append("Address: " + address + "\n");
                                resultTextView.append("Phone: " + phoneNumber + "\n\n");
                            }
                        });
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Error: " + response.code() + " " + response.message());
                }
            }
        });
    }
}
