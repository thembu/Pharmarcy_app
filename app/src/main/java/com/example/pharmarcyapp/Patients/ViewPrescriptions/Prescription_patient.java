package com.example.pharmarcyapp.Patients.ViewPrescriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.pharmarcyapp.MainActivity;
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

public class Prescription_patient extends AppCompatActivity {

    private LinearLayout prescriptionsContainer;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprescriptions);
        prescriptionsContainer = findViewById(R.id.prescriptionsContainer);

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
                runOnUiThread(() -> Toast.makeText(Prescription_patient.this, "Failed to fetch patient ID", Toast.LENGTH_SHORT).show());
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
                                    Toast.makeText(Prescription_patient.this, "No patient data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Prescription_patient.this, "Error parsing patient data", Toast.LENGTH_SHORT).show();
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
                runOnUiThread(() -> Toast.makeText(Prescription_patient.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> processPrescriptionsJSON(responseData));
                } else {
                    runOnUiThread(() -> Toast.makeText(Prescription_patient.this, "Failed to fetch prescription data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void processPrescriptionsJSON(String json) {
        try {
            JSONArray prescriptionsArray = new JSONArray(json);
            for (int i = 0; i < prescriptionsArray.length(); i++) {
                JSONObject prescription = prescriptionsArray.getJSONObject(i);
                LayoutInflater inflater = LayoutInflater.from(Prescription_patient.this);
                ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_viewprescriptions_item, null);

                TextView date = layout.findViewById(R.id.editTextDate);
                date.setText("Date: " + prescription.getString("date"));

                TextView patientIdView = layout.findViewById(R.id.textViewPatient_id);
                patientIdView.setText("Patient ID: " + prescription.getString("patient_id"));

                TextView id = layout.findViewById(R.id.textViewPrescription_id);
                id.setText("Prescription ID: " + prescription.getString("id"));

                TextView medicationName = layout.findViewById(R.id.textViewMedicationName);
                medicationName.setText("Medication Name: " + prescription.getString("medication_name"));

                TextView dosage = layout.findViewById(R.id.textViewDosage);
                dosage.setText("Dosage: " + prescription.getString("dosage"));

                TextView instructions = layout.findViewById(R.id.textViewInstructions);
                instructions.setText("Instructions: " + prescription.getString("instructions"));

                prescriptionsContainer.addView(layout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Prescription_patient.this, "Error parsing prescription data", Toast.LENGTH_SHORT).show();
        }
    }
}
