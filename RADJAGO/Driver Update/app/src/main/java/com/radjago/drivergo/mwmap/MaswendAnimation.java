package com.radjago.drivergo.mwmap;
import android.animation.ValueAnimator;
import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
public class MaswendAnimation {
    private UpdateLocationCallBack updateLocation;
    private ValueAnimator valueAnimator;
    private GoogleMap googleMap;
    private long animationDuration;
    public MaswendAnimation(GoogleMap googleMap,long duration , UpdateLocationCallBack updateLocation) {
        this.updateLocation = updateLocation;
        this.googleMap =googleMap;
        this.animationDuration=duration;
    }

    public void animateMarker(final Location destination, final Location oldLocation, final Marker marker) {
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
                        marker.setRotation(Utilities.computeRotation(v, marker.getRotation(),
                                (float)Utilities.bearingBetweenLocations(startPosition, newPosition)));
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setFlat(true);


                        // add new location into old location
                        updateLocation.onUpdatedLocation(destination);


                        //when marker goes out from screen it automatically move into center
                        if (googleMap!=null){
                            if (!Utilities.isMarkerVisible(googleMap,newPosition)){
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newCameraPosition(new CameraPosition.Builder()
                                                .target(newPosition)
                                                .bearing(Utilities.computeRotation(v, marker.getRotation(),
                                                        (float)Utilities.bearingBetweenLocations(startPosition, newPosition)))
                                                .zoom(googleMap.getCameraPosition().zoom)
                                                .tilt(45)
                                                .build()));
                            }else {
                                try {
                                    googleMap.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(new CameraPosition.Builder()
                                                    .target(newPosition)
                                                    .bearing(Utilities.computeRotation(v, marker.getRotation(),
                                                            (float)Utilities.bearingBetweenLocations(startPosition, newPosition)))
                                                    .tilt(45)
                                                    .zoom(googleMap.getCameraPosition().zoom)
                                                    .build()));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                        }
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
