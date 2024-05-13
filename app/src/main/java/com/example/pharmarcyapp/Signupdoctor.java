package com.example.pharmarcyapp;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmarcyapp.backend.SignUpWorker;

public class Signupdoctor extends AppCompatActivity {

    private EditText editTextDoctorFullName, editTextDoctorPassword, editTextDoctorID, editTextDoctorAge, editTextDoctorEmail;
    private RadioGroup radioGroupGender;
    private Button submit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_signup);

        // Initialize UI elements
        editTextDoctorFullName = findViewById(R.id.editTextDoctorFullName);
        editTextDoctorPassword = findViewById(R.id.editTextDoctorPassword);
        editTextDoctorID = findViewById(R.id.editTextDoctorID);
        editTextDoctorAge = findViewById(R.id.editTextDoctorAge);
        editTextDoctorEmail = findViewById(R.id.editTextDoctorEmail);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        submit = findViewById(R.id.buttonSignupDoctor);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReg(v);
            }
        });
    }

    public void onReg(View view) {
        String fullName = editTextDoctorFullName.getText().toString();
        String password = editTextDoctorPassword.getText().toString();
        String doctorID = editTextDoctorID.getText().toString();
        String ageStr = editTextDoctorAge.getText().toString();
        String email = editTextDoctorEmail.getText().toString();

        int age = Integer.parseInt(ageStr);

        int radioButtonID = radioGroupGender.getCheckedRadioButtonId();
        View radioButton = radioGroupGender.findViewById(radioButtonID);
        int idx = radioGroupGender.indexOfChild(radioButton);

        RadioButton r = (RadioButton) radioGroupGender.getChildAt(idx);
        String gender = r.getText().toString();

        // Construct the postData string with gender included
        String postData = "fullName=" + fullName + "&password=" + password + "&doctorID=" +  doctorID + "&age=" + age + "&gender=" + gender + "&email=" + email;

        // Create and execute the BackgroundWorker
        SignUpWorker bgworker = new SignUpWorker(this, "https://lamp.ms.wits.ac.za/home/s2695831/doctors.php", postData, true);
        bgworker.execute();
    }
}
