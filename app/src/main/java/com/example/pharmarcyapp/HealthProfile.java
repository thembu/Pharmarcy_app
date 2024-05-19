package com.example.pharmarcyapp;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HealthProfile extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    private EditText usernmame , email , password, medical_aid_no , age ;
    private TextInputEditText condition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_profile);


         usernmame = findViewById(R.id.usernameEditText);
         password = findViewById(R.id.passwordEditText);
         medical_aid_no = findViewById(R.id.editTextMedicalAidNo);
         age = findViewById(R.id.age);
         email = findViewById(R.id.email);
         condition = findViewById(R.id.condition);

         String user_email = MainActivity.email;
        System.out.println(email);
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2695831/profile.php?email=" + user_email)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Handle failure, maybe show an error message
                        Toast.makeText(HealthProfile.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()) {
                    final  String  responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray userData = null;
                            try {
                                userData = new JSONArray(responseData);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            for (int i = 0; i < userData.length(); i++) {
                                try {
                                    JSONObject data = userData.getJSONObject(i);

                                    usernmame.setText(data.getString("username"));
                                    password.setText(data.getString("password"));
                                    medical_aid_no.setText(data.getString("medical_aid_no"));
                                    age.setText(data.getString("age"));
                                    email.setText(data.getString("email"));
                                    condition.setText(data.getString("conditions"));

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    }

                });



                }
            }
        });


    }






}




