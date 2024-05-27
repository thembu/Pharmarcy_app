package com.example.pharmarcyapp.Patients.HealtProfile;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmarcyapp.MainActivity;
import com.example.pharmarcyapp.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MedicalRecords extends AppCompatActivity {

    private GraphView graph;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = (GraphView) findViewById(R.id.graph);

        String user_email = MainActivity.email;
        fetchPatientId(user_email);
    }

    private void fetchPatientId(String email) {
        Request getId = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2695831/viewprescriptions1.php?email=" + email)
                .build();

        client.newCall(getId).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MedicalRecords.this, "Failed to fetch patient ID", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray userData = new JSONArray(responseData);
                                if (userData.length() > 0) {
                                    JSONObject data = userData.getJSONObject(0);
                                    String str_id = data.getString("patient_id");

                                    int patientId = Integer.parseInt(str_id);
                                    fetchPrescriptions(patientId);

                                } else {
                                    Toast.makeText(MedicalRecords.this, "No patient data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MedicalRecords.this, "Error parsing patient data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void fetchPrescriptions(int patientId) {
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2695831/viewprescriptions.php?patient_id=" + patientId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MedicalRecords.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    System.out.println(responseData);
                    runOnUiThread(() -> processPrescriptionsJSON(responseData));
                } else {
                    runOnUiThread(() -> Toast.makeText(MedicalRecords.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


    private void processPrescriptionsJSON(String json) {
        try {
            JSONArray prescriptionsArray = new JSONArray(json);
            if (prescriptionsArray == null || prescriptionsArray.length() == 0) {
                // Handle case when no prescriptions are returned
                runOnUiThread(() -> Toast.makeText(MedicalRecords.this, "No prescriptions found", Toast.LENGTH_SHORT).show());
                return; // Exit the method
            }

            List<DataPoint> dataPoints = new ArrayList<>();

            for (int i = 0; i < prescriptionsArray.length(); i++) {
                JSONObject prescription = prescriptionsArray.getJSONObject(i);
                String dateString = prescription.getString("date");
                String dosageString = prescription.getString("dosage");

                // Convert date string to timestamp
                long timestamp = convertDateStringToTimestamp(dateString);

                // Add data points for graph
                try {
                    int dosage = Integer.parseInt(dosageString);
                    dataPoints.add(new DataPoint(timestamp, dosage));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Add data points to the graph
            DataPoint[] dataPointArray = dataPoints.toArray(new DataPoint[0]);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointArray);
            graph.addSeries(series);

            graph.setHorizontalScrollBarEnabled(true);

            graph.setTitle("Dosages over time");
            graph.setTitleTextSize(30);
            // Set label formatter for X axis
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        // X axis
                        return convertTimestampToDateString((long) value); // Convert timestamp back to date string
                    } else {
                        // Y axis
                        return super.formatLabel(value, isValueX); // Use default formatting for Y axis
                    }
                }
            });

            // Set number of horizontal labels for X axis
            graph.getGridLabelRenderer().setNumHorizontalLabels(dataPointArray.length);
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
            graph.getGridLabelRenderer().setVerticalAxisTitle("dosage");
            graph.getGridLabelRenderer().setTextSize(12f);
// Enable the display of the title
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MedicalRecords.this, "Error parsing prescription data", Toast.LENGTH_SHORT).show();
        }
    }


    private long convertDateStringToTimestamp(String dateString) {
        try {
            System.out.println(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            java.util.Date date = dateFormat.parse(dateString);
            return date.getTime(); // Returns timestamp in milliseconds
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String convertTimestampToDateString(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        return dateFormat.format(new Date(timestamp));
    }



}
