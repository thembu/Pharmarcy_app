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

    private EditText editTextusername, editTextpassword, editTextMedicalAidNo, editTextage;
    private Button buttonSignupPatient;
    private RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        editTextusername = findViewById(R.id.editTextUsername);
        editTextpassword = findViewById(R.id.editTextPassword);
        editTextMedicalAidNo = findViewById(R.id.editTextMedicalAidNo);
        editTextage = findViewById(R.id.editTextAge);
        buttonSignupPatient = findViewById(R.id.buttonSignupPatient);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        buttonSignupPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReg(v);
            }
        });


    }

    public void onReg(View view) {
        String str_username = editTextusername.getText().toString();
        String str_password = editTextpassword.getText().toString();
        String str_age = editTextage.getText().toString();
        String str_medical_aid_no = editTextMedicalAidNo.getText().toString();

        // Parse age to integer
        int age = Integer.parseInt(str_age);

        int radioButtonID = radioGroupGender.getCheckedRadioButtonId();
        View radioButton = radioGroupGender.findViewById(radioButtonID);
        int idx = radioGroupGender.indexOfChild(radioButton);

        RadioButton r = (RadioButton) radioGroupGender.getChildAt(idx);
        String str_gender = r.getText().toString();

        // Construct the postData string with gender included
        String postData = "Username=" + str_username + "&password=" + str_password + "&medical_aid_no=" + str_medical_aid_no + "&age=" + age + "&gender=" + str_gender;

        // Create and execute the BackgroundWorker
        BackgroundWorker bgworker = new BackgroundWorker("https://lamp.ms.wits.ac.za/home/s2695831/patients.php", postData, true);
        bgworker.execute();
    }
}

