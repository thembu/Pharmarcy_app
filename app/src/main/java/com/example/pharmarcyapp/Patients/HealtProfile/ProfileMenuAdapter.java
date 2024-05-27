package com.example.pharmarcyapp.Patients.HealtProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pharmarcyapp.Patients.PharmarcyLocation.Pharmarcies;
import com.example.pharmarcyapp.R;

import java.util.ArrayList;

public class ProfileMenuAdapter extends ArrayAdapter<ProfileMenuSetter> {

    Context context;
    public ProfileMenuAdapter(@NonNull Context context, ArrayList<ProfileMenuSetter> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
        this.context = context;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_profile_menu_item, parent, false);
        }

        ProfileMenuSetter courseModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.items);
        ImageView courseIV = listitemView.findViewById(R.id.images);

        courseTV.setText(courseModel.getCourse_name());
        courseIV.setImageResource(courseModel.getImgid());

        if (position == 1) {
            listitemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event for this specific item (Pharmacies near you)
                    Intent intent = new Intent(context, MedicalRecords.class); // Replace YourActivity with the desired activity class
                    context.startActivity(intent);
                }
            });

        }


        if (position == 0) {
            listitemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click event for this specific item (Pharmacies near you)
                    Intent intent = new Intent(context, HealthProfile.class); // Replace YourActivity with the desired activity class
                    context.startActivity(intent);
                }
            });
        }

        return listitemView;
    }


}


