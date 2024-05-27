package com.example.pharmarcyapp.backend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pharmarcyapp.Patients.Menu.Patient_home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ProfileHelper extends AsyncTask<Void, Void, String> {
     String url ,username , password ,  medical_aid_no ,    email ,  conditions;
     int age;
    @SuppressLint("StaticFieldLeak")
    private final Context context;


      /*

     NOTE I DO NOT KNOW HOW THE FUCK THIS WORKS .
     DO NOT TOUCH !!!
     WE CANNOT FIX ANY ISSUES YOU FUCK UP BECAUSE YOU THINK YOUR SMART

     */


    public ProfileHelper(Context context, String url, String username, String password , String medical_aid_no , int age  , String email , String conditions) {
        this.context = context;
        this.url = url;
        this.username = username;
        this.password = password;
        this.age = age;
        this.conditions = conditions;
        this.medical_aid_no = medical_aid_no;
        this.email = email;

    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder response = new StringBuilder();
        try {
            // Create URL object
            URL url = new URL(this.url);

            // Create connection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method for POST
            connection.setRequestMethod("POST");

            // Enable output and set output stream
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();

            // Construct data to be sent to the server
            String postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8") +  "&medical_aid_no=" + URLEncoder.encode(medical_aid_no, "UTF-8") +  "&age=" + age +  "&email=" + URLEncoder.encode(email, "UTF-8") + "&conditions=" + URLEncoder.encode(conditions, "UTF-8");

            System.out.println(postData);

            // Write data to output stream
            os.write(postData.getBytes());
            os.flush();
            os.close();

            // Get response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // Close connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // You can implement further handling based on the server response, such as navigating to another activity.
        // Handle the response from the server
        System.out.println("Response: " + result);
        // Check if login is successful
        if (result.equals("Update successful")) {
            // If login is successful, move to the home screen
            Intent intent = new Intent(context, Patient_home.class);
            context.startActivity(intent);
            // Finish the current activity to prevent going back to the login screen using the back button
            ((Activity) context).finish();
        } else {
            // If login fails, you can display an error message or handle it accordingly
            Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
        }


    }
}