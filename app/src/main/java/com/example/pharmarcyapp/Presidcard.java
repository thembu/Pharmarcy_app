package com.example.pharmarcyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Presidcard extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    private TextInputEditText patientID;
    private static final int PRESCRIPTION_REQUEST_CODE = 1;

    private Button buttonSearch;
    private LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);

        patientID = new TextInputEditText(this);
        buttonSearch = new Button(this);
        buttonSearch.setText("Search");

        l.addView(patientID);
        l.addView(buttonSearch);
        setContentView(l);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPatient();
            }
        });
    }

    private void searchPatient() {
        // Clear previous results
        l.removeViews(2, l.getChildCount() - 2); // keep patientID and buttonSearch

        String patientIdStr = patientID.getText().toString();
        post("https://lamp.ms.wits.ac.za/home/s2695831/patientSearch.php", patientIdStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray alls = new JSONArray(responseStr);
                                for (int i = 0; i < alls.length(); i++) {
                                    JSONObject all = alls.getJSONObject(i);
                                    LayoutInflater inflater = getLayoutInflater();
                                    ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.activity_prescription1, null);

                                    TextView username = layout.findViewById(R.id.textViewPatientName);
                                    username.setText("Fullname: " + all.getString("username"));

                                    TextView age = layout.findViewById(R.id.textViewPatientAge);
                                    age.setText("Age: " + all.getString("age"));

                                    TextView gender = layout.findViewById(R.id.textViewPatientGender);
                                    gender.setText("Gender: " + all.getString("gender"));

                                    TextView patient_id = layout.findViewById(R.id.patient_id);
                                    patient_id.setText("Patient ID: " + all.getString("patient_id"));

                                    TextView medical_aid_no = layout.findViewById(R.id.medical_aid);
                                    medical_aid_no.setText("Medical Aid Number: " + all.getString("medical_aid_no"));

                                    TextView email = layout.findViewById(R.id.email);
                                    email.setText("Email: " + all.getString("email"));

                                    TextView conditions = layout.findViewById(R.id.conditions);
                                    conditions.setText("Conditions: " + all.getString("conditions"));

                                    l.addView(layout);

                                    Button addPrescriptionButton = layout.findViewById(R.id.AddPrescription);
                                    Button viewPrescription = layout.findViewById(R.id.viewPrescription);

                                    addPrescriptionButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Presidcard.this, Prescription.class);
                                            String patientIDValue = patientID.getText().toString();
                                            int patientIDInt = Integer.parseInt(patientIDValue);
                                            intent.putExtra("PATIENT_ID", patientIDInt);
                                            startActivity(intent);

                                            addPrescriptionButton.setText("Prescription Filled");
                                            addPrescriptionButton.setEnabled(false);
                                        }
                                    });

                                    viewPrescription.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Presidcard.this, ViewPrescriptions.class);
                                            String patientIDValue = patientID.getText().toString();
                                            int patientIDInt = Integer.parseInt(patientIDValue);
                                            intent.putExtra("PATIENT_ID", patientIDInt);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView t = new TextView(Presidcard.this);
                            t.setText("Enter the correct patient ID");
                            l.addView(t);
                        }
                    });
                }
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Call post(String url, String patientId, Callback callback) {
        String urlWithPatientId = url + "?patient_id=" + patientId;
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url(urlWithPatientId)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
