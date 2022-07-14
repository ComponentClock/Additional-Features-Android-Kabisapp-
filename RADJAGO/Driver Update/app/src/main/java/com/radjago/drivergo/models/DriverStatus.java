package com.radjago.drivergo.models;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DriverStatus {
    public String id;
    public String status;

    public DriverStatus() {
    }

    public DriverStatus(String id, String status) {
        this.status = id;
        this.status = status;
    }
}