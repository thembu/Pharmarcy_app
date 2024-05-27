package com.example.pharmarcyapp.Patients.PharmarcyLocation;

public class Pharmacy {
    private String name;
    private String address;
    private String phoneNumber;

    private String photoReference;
    public Pharmacy(String name, String address, String phoneNumber, String photoReference) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photoReference = photoReference;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhotoReference() {
        return photoReference;
    }

}
