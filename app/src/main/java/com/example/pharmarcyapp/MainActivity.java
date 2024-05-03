package com.example.pharmarcyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         Button login = findViewById(R.id.Login);
        Button signup = findViewById(R.id.Signup);

       signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Signup activity
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent); // Start the Signup activity
            }
        });

    }

}