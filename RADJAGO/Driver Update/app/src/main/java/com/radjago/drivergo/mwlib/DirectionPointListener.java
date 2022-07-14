package com.radjago.drivergo.mwlib;

import com.google.android.gms.maps.model.PolylineOptions;

public interface DirectionPointListener {
    public void onPath(PolylineOptions polyLine, String distance, String duration);
}