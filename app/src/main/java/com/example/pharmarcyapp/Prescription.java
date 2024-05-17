package com.example.pharmarcyapp;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pharmarcyapp.backend.Prescriptionfill;
import com.google.android.material.textfield.TextInputEditText;

public class Prescription extends AppCompatActivity {

    private TextView textView;
    private EditText medicationNameEditText ;
    private EditText  datei;

    private EditText dosageEditText;
    private TextInputEditText instructionsEditText;
    private Button submitButton;

    private Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);

        // Initialize views
        datei = findViewById(R.id.datei);
        textView = findViewById(R.id.textView);
        medicationNameEditText = findViewById(R.id.medication_name_edit_text);
        dosageEditText = findViewById(R.id.dosage_edit_text);
        instructionsEditText = findViewById(R.id.ins);
        submitButton = findViewById(R.id.submit_button);



        // Set onClick listener for submit button (if needed)
        //submitButton.setOnClickListener(view -> {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReg(v);
               Intent intent = new Intent(Prescription.this, Presidcard.class);
               startActivity(intent);
            }
        });
    }


            public void onReg(View view) {

                // Perform actions when submit button is clicked
                // For example, you can retrieve text from EditText fields
                String medicationName = medicationNameEditText.getText().toString();
                String dosage = dosageEditText.getText().toString();
                String instructions = instructionsEditText.getText().toString();
                String date = datei.getText().toString();

                //String postData = "medication_name=" + medicationName + "dosage=" + dosage + "instructions=" + instructions;
                //Context context = v.getContext();
                Intent intents = getIntent();
                int patientID = 0; // Default value in case patientID is not found
                if (intents != null && intents.hasExtra("PATIENT_ID")) {
                    patientID = intents.getIntExtra("PATIENT_ID", 0);
                }

                // Construct postData string including medication details and patientID
                String postData ="medication_name=" + medicationName + "&dosage=" + dosage + "&instructions=" + instructions + "&patient_id=" + patientID + "&date=" + date;

                // Create and execute the BackgroundWorker
                Prescriptionfill bgworker = new Prescriptionfill(this, "https://lamp.ms.wits.ac.za/home/s2695831/prescriptions.php", postData, true);
                bgworker.execute();
            }

            // Do something with the retrieved information (e.g., submit to a database)
            public void handleResponse(boolean prescriptionSaved) {
                if (prescriptionSaved) {
                    // Prescription saved successfully
                    Intent intentr = new Intent();
                    intentr.putExtra("PRESCRIPTION_SAVED", true);
                    setResult(RESULT_OK, intentr);
                    finish();
                } else {
                    // Prescription not saved
                    // Handle error or display message
                }
            }

    //@Override
    protected void onPostExecute() {
        // Start the Presidcard activity
        Intent intent = new Intent(Prescription.this, Presidcard.class);
        startActivity(intent);
    }

}
