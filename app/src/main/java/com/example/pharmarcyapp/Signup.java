package com.example.pharmarcyapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Signup extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextMedicalAidNo, editTextAge;
    private RadioGroup radioGroupGender;
    private Button buttonSignupPatient, buttonSignupPharmacist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextMedicalAidNo = findViewById(R.id.editTextMedicalAidNo);
        editTextAge = findViewById(R.id.editTextAge);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonSignupPatient = findViewById(R.id.buttonSignupPatient);
        buttonSignupPharmacist = findViewById(R.id.buttonSignupPharmacist);

        // Set click listeners
        buttonSignupPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup("Patient");
            }
        });

        buttonSignupPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup("Pharmacist");
            }
        });
    }

    private void signup(String userType) {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String medicalAidNo = editTextMedicalAidNo.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || medicalAidNo.isEmpty() || age.isEmpty() || gender.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        String[] params = {username, password, medicalAidNo, age, gender, userType};
        new SignupTask().execute(params);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class SignupTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = "https://lamp.ms.wits.ac.za/home/s2695831/patients.php";
            String postData = "username=" + URLEncoder.encode(params[0]) +
                    "&password=" + URLEncoder.encode(params[1]) +
                    "&medicalAidNo=" + URLEncoder.encode(params[2]) +
                    "&age=" + URLEncoder.encode(params[3]) +
                    "&gender=" + URLEncoder.encode(params[4]) +
                    "&userType=" + URLEncoder.encode(params[5]);

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Signup successful";
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            showToast(result);
        }
    }
}
