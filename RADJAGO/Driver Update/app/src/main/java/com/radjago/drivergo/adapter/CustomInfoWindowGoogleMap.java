package com.radjago.drivergo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import com.radjago.drivergo.R;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.custom_marker, null);


        // InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        //   int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
        //           "drawable", context.getPackageName());
        //    img.setImageResource(imageId);

        // hotel_tv.setText(infoWindowData.getHotel());
        //  food_tv.setText(infoWindowData.getFood());
        //  transport_tv.setText(infoWindowData.getTransport());

        return view;
    }
}