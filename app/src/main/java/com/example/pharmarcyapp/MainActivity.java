package com.example.pharmarcyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.pharmarcyapp.backend.LoginWorker;
import com.example.pharmarcyapp.backend.Logindoctor;


public class MainActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);

        Button login = findViewById(R.id.Login);
        Button signupdoctor = findViewById(R.id.signupdoctor);
        Button signuppatient = findViewById(R.id.signuppatient);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(v);
            }
        });

        signuppatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the patient signup activity
                Intent intent = new Intent(MainActivity.this, Signuppatient.class);
                startActivity(intent);
            }
        });

        signupdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the doctor signup activity
                Intent intent = new Intent(MainActivity.this, Signupdoctor.class);
                startActivity(intent);
            }
        });
    }

//    public void onLogin(View view) {
//        String str_username = username.getText().toString();
//        String str_password = password.getText().toString();
//
//        // Determine if the username represents a doctor or a patient
//        boolean isDoctor = str_username.matches("\\d+");
//
//        // Construct the URL based on whether it's a doctor or patient login
//        String url;
//        String postData;
//
//        if (isDoctor) {
//            url = "https://lamp.ms.wits.ac.za/home/s2695831/doctors.php";
//            postData = "doctorID=" + str_username + "," + "&password=" + str_password;
//        } else {
//            url = "https://lamp.ms.wits.ac.za/home/s2695831/patients.php";
//            postData = "Username=" + str_username + "," + "&password=" + str_password;
//        }
//        String[] parts = postData.split(",");
//        // Create and execute the BackgroundWorker
//        LoginWorker bgworker = new LoginWorker(MainActivity.this, url, parts[0], parts[1]);
//        bgworker.execute();
//    }



    public void onLogin(View view) {
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();

        // Determine if the username represents a doctor or a patient
        boolean isDoctor = str_username.matches("\\d+");

        // Construct the URL based on whether it's a doctor or patient login
        String url;
        String postData;

        if (isDoctor) {
            url = "https://lamp.ms.wits.ac.za/home/s2695831/doctors2.php";
           // postData = "doctorID=" + str_username + "&password=" + str_password;
            Logindoctor bgworker = new Logindoctor(this, url, str_username, str_password);
            bgworker.execute();
        } else {
            url = "https://lamp.ms.wits.ac.za/home/s2695831/patients2.php";
           // postData = "Username=" + str_username + "&password=" + str_password;
//            LoginWorker bgworker = new LoginWorker(MainActivity.this, url, str_username, str_password);
//            bgworker.execute();
            LoginWorker bgworker = new LoginWorker(this, url, str_username, str_password);
            bgworker.execute();
        }

        // Create and execute the BackgroundWorker

    }
}
