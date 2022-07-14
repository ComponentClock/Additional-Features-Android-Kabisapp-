package com.radjago.drivergo.mwlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import com.radjago.drivergo.R;

import static java.lang.Math.atan;

public class MapUtils {
    @NotNull
    public static ArrayList<LatLng> getListOfLocations(LatLng asal,LatLng tujuan) {
        ArrayList<LatLng> locationList = new ArrayList<>();
        locationList.add(new LatLng(asal.latitude, asal.longitude));
        locationList.add(new LatLng(tujuan.latitude, tujuan.longitude));
        return locationList;
    }
    public static ArrayList<LatLng> getdriver(LatLng latLng) {
        ArrayList<LatLng> locationList = new ArrayList<>();
        locationList.add(new LatLng(latLng.latitude, latLng.longitude));
        return locationList;
    }
    public static Bitmap getOriginDestinationMarkerBitmap() {
        int height = 20;
        int width = 20;
        Bitmap bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawRect(0F, 0F, width, height, paint);
        return bitmap;
    }

    public static Bitmap getCarBitmap(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.compass);
        return Bitmap.createScaledBitmap(bitmap, 150, 150, true);
    }

    public static Float getRotation(LatLng start, LatLng end) {
        Double latDifference = Math.abs(start.latitude - end.latitude);
        Double lngDifference = Math.abs(start.longitude - end.longitude);
        float rotation = -1F;
        if (start.latitude < end.latitude && start.longitude < end.longitude) {
            rotation = (float) Math.toDegrees(atan(lngDifference / latDifference));
        } else if (start.latitude >= end.latitude && start.longitude < end.longitude) {
            rotation = (float) (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90);
        } else if (start.latitude >= end.latitude && start.longitude >= end.longitude) {
            rotation = (float) (Math.toDegrees(atan(lngDifference / latDifference)) + 180);
        } else if (start.latitude < end.latitude && start.longitude >= end.longitude) {
            rotation = (float) (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270);
        }
        return rotation;
    }

}