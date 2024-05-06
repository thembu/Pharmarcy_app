package com.example.pharmarcyapp;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Home  extends AppCompatActivity {
    GridView coursesGV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        coursesGV = findViewById(R.id.items);
        ArrayList<MainMenu> courseModelArrayList = new ArrayList<MainMenu>();

        courseModelArrayList.add(new MainMenu("Prescription Management", R.drawable.pill));
        courseModelArrayList.add(new MainMenu("Search for medication", R.drawable.search));
        courseModelArrayList.add(new MainMenu("Health profile", R.drawable.profile));
        courseModelArrayList.add(new MainMenu("Pharmacies near you", R.drawable.location));

        MainMenuAdapter adapter = new MainMenuAdapter(this, courseModelArrayList);
        coursesGV.setAdapter(adapter);

    }
}
