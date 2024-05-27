package com.example.pharmarcyapp.Patients.HealtProfile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmarcyapp.MainActivity;
import com.example.pharmarcyapp.R;
import com.example.pharmarcyapp.backend.ProfileHelper;
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

    private EditText username, email, password, medical_aid_no, age;
    private TextInputEditText condition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        medical_aid_no = findViewById(R.id.editTextMedicalAidNo);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        condition = findViewById(R.id.condition);
        Button makeChanges = findViewById(R.id.changes);

        String user_email = MainActivity.email;

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2695831/profile.php?email=" + user_email)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(HealthProfile.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONArray userData = new JSONArray(responseData);
                            for (int i = 0; i < userData.length(); i++) {
                                JSONObject data = userData.getJSONObject(i);

                                username.setText(data.getString("username"));
                                password.setText(data.getString("password"));
                                medical_aid_no.setText(data.getString("medical_aid_no"));
                                age.setText(data.getString("age"));
                                email.setText(data.getString("email"));
                                condition.setText(data.getString("conditions"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HealthProfile.this, "Error parsing user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        makeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = username.getText().toString();
                String str_password = password.getText().toString();
                String str_medical_aid_no = medical_aid_no.getText().toString();
                String str_age = age.getText().toString();
                String str_email = email.getText().toString();
                String str_conditions = condition.getText().toString();

                int int_age = 0;
                try {
                    int_age = Integer.parseInt(str_age);
                } catch (NumberFormatException e) {
                    Toast.makeText(HealthProfile.this, "Invalid age input", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProfileHelper update = new ProfileHelper(HealthProfile.this, "https://lamp.ms.wits.ac.za/home/s2695831/profiletest.php", str_username, str_password, str_medical_aid_no, int_age, str_email, str_conditions);
                update.execute();
            }
        });
    }
}
