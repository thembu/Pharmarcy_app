package com.example.pharmarcyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmarcyapp.backend.SignUpWorker;

public class Signup extends AppCompatActivity {

    private EditText editTextusername, editTextpassword, editTextMedicalAidNo, editTextage , editTextemail;
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
        editTextemail = findViewById(R.id.editTextemail);
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
        String str_email = editTextemail.getText().toString();
        // Parse age to integer
        int age = Integer.parseInt(str_age);

        int radioButtonID = radioGroupGender.getCheckedRadioButtonId();
        View radioButton = radioGroupGender.findViewById(radioButtonID);
        int idx = radioGroupGender.indexOfChild(radioButton);


        RadioButton r = (RadioButton) radioGroupGender.getChildAt(idx);
        String str_gender = r.getText().toString();

        // Construct the postData string with gender included
        String postData = "Username=" + str_username + "&password=" + str_password + "&medical_aid_no=" + str_medical_aid_no + "&age=" + age + "&gender=" + str_gender+"&email=" + str_email;

        // Create and execute the BackgroundWorker
        SignUpWorker bgworker = new SignUpWorker(this,"https://lamp.ms.wits.ac.za/home/s2695831/patients.php", postData, true);
        System.out.println(postData);
        bgworker.execute();
    }
}

