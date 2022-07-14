package com.radjago.drivergo.mwlib.AnimationClass;

import android.animation.ValueAnimator;
import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.radjago.drivergo.mwlib.CallBacks.UpdateLocationCallBack;
import com.radjago.drivergo.mwlib.Utils.Utilities;


public class HRMarkerAnimation {
    private UpdateLocationCallBack updateLocation;
    private ValueAnimator valueAnimator;
    private GoogleMap googleMap;
    private long animationDuration;

    public HRMarkerAnimation(GoogleMap googleMap,long duration , UpdateLocationCallBack updateLocation) {
        this.updateLocation = updateLocation;
        this.googleMap =googleMap;
        this.animationDuration=duration;
    }

    public void animateMarker(final Location destination, final Location oldLocation, final Marker marker,final float bearing) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            if (valueAnimator != null)
                valueAnimator.end();

            final Utilities.LatLngInterpolator latLngInterpolator = new Utilities.LatLngInterpolator.LinearFixed();
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(animationDuration); // duration 1 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        //marker.setRotation(computeRotation(v, startRotation, destination.getBearing()));
                        marker.setRotation(Utilities.computeRotation(v, marker.getRotation(),
                                (float)Utilities.bearingBetweenLocations(startPosition, newPosition)));
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setFlat(true);


                        // add new location into old location
                        updateLocation.onUpdatedLocation(destination);


                        //when marker goes out from screen it automatically move into center

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // handle exception here
                    }
                }

            });
            valueAnimator.start();
        }
    }
}
