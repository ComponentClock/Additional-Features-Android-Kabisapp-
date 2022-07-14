package com.radjago.drivergo.models;

public class FcmDriver {
    public String id;
    public String status;
    public Double lat;
    public Double lon;
    public Float bearing;
    public String estimasi;
    public String jarak;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public FcmDriver() {
    }

    public FcmDriver(String id, String status, Double lat, Double lon, Float bearing, String estimasi, String jarak) {
        this.id = id;
        this.status = status;
        this.lat = lat;
        this.lon = lon;
        this.bearing = bearing;
        this.estimasi = estimasi;
        this.jarak = jarak;
    }
}
