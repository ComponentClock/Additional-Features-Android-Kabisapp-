package com.radjago.drivergo.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 24/02/2019.
 */

public class LokasiDriverModel {

    @SerializedName("id_driver")
    @Expose

    private String idDriver;
    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("bearing")
    @Expose
    private float bearing;

    @SerializedName("status")
    @Expose
    private String status;

    public String getIdDriver() {
        return idDriver;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public String getStatus() {
        return status;
    }
}
