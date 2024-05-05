package com.example.pharmarcyapp.backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.pharmarcyapp.Home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginWorker extends AsyncTask<Void, Void, String> {
    private String url;
    private String username;
    private String password;

    private  Context context;


      /*

     NOTE I DO NOT KNOW HOW THE FUCK THIS WORKS .
     DO NOT TOUCH !!!
     WE CANNOT FIX ANY ISSUES YOU FUCK UP BECAUSE YOU THINK YOUR SMART

     */


    public LoginWorker(Context context, String url, String username, String password) {
        this.context = context;
        this.url = url;
        this.username = username;
        this.password = password;
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
                    "&password=" + URLEncoder.encode(password, "UTF-8");

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
        if (result.equals("Login successful")) {
            // If login is successful, move to the home screen
            Intent intent = new Intent(context, Home.class);
            context.startActivity(intent);
            // Finish the current activity to prevent going back to the login screen using the back button
            ((Activity) context).finish();
        } else {
            // If login fails, you can display an error message or handle it accordingly
            Toast.makeText(context, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
    }

