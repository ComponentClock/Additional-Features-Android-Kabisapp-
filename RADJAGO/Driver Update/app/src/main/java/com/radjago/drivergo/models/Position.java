package com.radjago.drivergo.models;

public class Position {
    public String Id;
    public Double Lat;
    public Double Long;
    public Float Bearing;

    public Position(String Id, Double Lat, Double Long, Float Bearing) {
        this.Id = Id;
        this.Lat = Lat;
        this.Long = Long;
        this.Bearing = Bearing;
    }
}