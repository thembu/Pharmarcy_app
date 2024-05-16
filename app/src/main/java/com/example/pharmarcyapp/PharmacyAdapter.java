package com.example.pharmarcyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmarcyapp.Pharmacy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private ArrayList<Pharmacy> pharmacyList;

    public PharmacyAdapter(ArrayList<Pharmacy> pharmacyList) {
        this.pharmacyList = pharmacyList;
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmarcy_item, parent, false);
        return new PharmacyViewHolder(view);
    }

    // Inside onBindViewHolder method of PharmacyAdapter
    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
        Pharmacy pharmacy = pharmacyList.get(position);
        holder.bind(pharmacy);
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();

    }

    public static class PharmacyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView addressTextView;
        private TextView phoneTextView;
        private ImageView placeImageView;

        public PharmacyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            placeImageView = itemView.findViewById(R.id.placeImageView);
        }

        public void bind(Pharmacy pharmacy) {
            nameTextView.setText(pharmacy.getName());
            addressTextView.setText(pharmacy.getAddress());
            phoneTextView.setText(pharmacy.getPhoneNumber());

            if (pharmacy.getPhotoReference() != null) {
                String photoUrl = buildPhotoUrl(pharmacy.getPhotoReference());
                Picasso.get().load(photoUrl).into(placeImageView);
            } else {
                // If there's no photo reference, you can set a default placeholder image
                placeImageView.setImageResource(R.drawable.pill);
            }
        }

        private String buildPhotoUrl(String photoReference) {
            return "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=400" +
                    "&photoreference=" + photoReference +
                    "&key=AIzaSyCCxkJSJvdtLyGkZe3v-vPV7WrnagacMKk";
        }
    }

}
