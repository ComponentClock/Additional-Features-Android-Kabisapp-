package com.radjago.drivergo.gmap;

import com.google.android.gms.maps.model.PolylineOptions;

public interface DirectionPointListener {
    public void onPath(PolylineOptions polyLine);
}