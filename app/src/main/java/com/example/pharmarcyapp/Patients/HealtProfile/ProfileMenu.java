package com.example.pharmarcyapp.Patients.HealtProfile;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmarcyapp.R;

import java.util.ArrayList;

public class ProfileMenu  extends AppCompatActivity {
    GridView coursesGV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);

        coursesGV = findViewById(R.id.items);
        ArrayList<ProfileMenuSetter> courseModelArrayList = new ArrayList<ProfileMenuSetter>();

        courseModelArrayList.add(new ProfileMenuSetter("Edit user information", R.drawable.pill));
        // courseModelArrayList.add(new MainMenu("Search for medication", R.drawable.search));
        // courseModelArrayList.add(new MainMenu("Health profile", R.drawable.profile));
        courseModelArrayList.add(new ProfileMenuSetter("See medical Records", R.drawable.location));




        ProfileMenuAdapter adapter = new ProfileMenuAdapter(this, courseModelArrayList);

        coursesGV.setAdapter(adapter);



    }
}
