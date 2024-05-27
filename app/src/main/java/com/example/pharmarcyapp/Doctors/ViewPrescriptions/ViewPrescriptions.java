package com.example.pharmarcyapp.Doctors.ViewPrescriptions;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pharmarcyapp.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class ViewPrescriptions extends AppCompatActivity {

    private LinearLayout prescriptionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprescriptions); // Make sure to set the correct layout

        prescriptionsContainer = findViewById(R.id.prescriptionsContainer);  // Find the LinearLayout inside the ScrollView

        OkHttpClient client = new OkHttpClient();
        Intent intent = getIntent();
        int patientID = 0; // Default value in case patientID is not found
        if (intent != null && intent.hasExtra("PATIENT_ID")) {
            patientID = intent.getIntExtra("PATIENT_ID", 0);
        }

        // Make network request to retrieve prescription data
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2695831/viewprescriptions.php?patient_id=" + patientID)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Handle failure, maybe show an error message
                        Toast.makeText(ViewPrescriptions.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray prescriptionsArray = new JSONArray(responseData);
                                for (int i = 0; i < prescriptionsArray.length(); i++) {
                                    JSONObject prescription = prescriptionsArray.getJSONObject(i);
                                    // Inflate layout for each prescription
                                    LayoutInflater inflater = LayoutInflater.from(ViewPrescriptions.this);
                                    ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_viewprescriptions_item, null);

                                    TextView date = layout.findViewById(R.id.editTextDate);
                                    date.setText("Date: " + prescription.getString("date"));

                                    TextView patient_id = layout.findViewById(R.id.textViewPatient_id);
                                    patient_id.setText("Patient ID: " + prescription.getString("patient_id"));

                                    TextView id = layout.findViewById(R.id.textViewPrescription_id);
                                    id.setText("Prescription ID: " + prescription.getString("id"));

                                    TextView medicationName = layout.findViewById(R.id.textViewMedicationName);
                                    medicationName.setText("Medication Name: " + prescription.getString("medication_name"));

                                    TextView dosage = layout.findViewById(R.id.textViewDosage);
                                    dosage.setText("Dosage: " + prescription.getString("dosage"));

                                    TextView instructions = layout.findViewById(R.id.textViewInstructions);
                                    instructions.setText("Instructions: " + prescription.getString("instructions"));

                                    // Add layout to parent view
                                    prescriptionsContainer.addView(layout);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ViewPrescriptions.this, "Error parsing prescription data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Handle unsuccessful response, maybe show an error message
                            Toast.makeText(ViewPrescriptions.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
