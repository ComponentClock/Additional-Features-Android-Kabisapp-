package com.radjago.drivergo.models;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DataDriver {

    public String id;
    public Double latitude;
    public Double longitude;
    public Float bearing;
    public String status;

    public DataDriver() {
    }

    public DataDriver(String id, Double latitude, Double longitude, Float bearing, String status) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.status = status;
    }
}