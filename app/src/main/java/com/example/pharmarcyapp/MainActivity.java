package com.example.pharmarcyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pharmarcyapp.backend.LoginWorker;

public class MainActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);



         Button login = findViewById(R.id.Login);

         Button signup = findViewById(R.id.Signup);

         login.setOnClickListener(new View.OnClickListener() {
             @Override

             public void onClick(View v) {
                 onLogin(v);
             }

         });

       signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Signup activity
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent); // Start the Signup activity
            }
        });

    }

    public void onLogin(View view) {
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();


        // Construct the postData string with gender included

        // Create and execute the BackgroundWorker
       LoginWorker bgworker = new LoginWorker(this,"https://lamp.ms.wits.ac.za/home/s2695831/patients2.php", str_email, str_password);
       bgworker.execute();

    }

}