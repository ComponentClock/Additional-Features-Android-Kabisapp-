package com.radjago.drivergo.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.json.LokasiResponse;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NewApi")
public class MWLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String NOTIFICATION_CHANNEL_ID = "4655";
    private static final String TAG = "LocationService";
    private static final int LOCATION_INTERVAL = 1000;
    private Context mContext;
    private LocationRequest locationRequest;
    private static final long INTERVAL = 2000; //1.5 min
    private static final long FASTEST_INTERVAL = 1000; //1.5 min
    private static final long DISPLACEMENT = 5; //5 meter
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private static final int REQUEST_LOCATION = 0;
    private Location mLastLocation;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        mContext = this;
        createLocationRequest();
        Constants.isBackground = true;
       // getLocation();
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_INTERVAL);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        com.radjago.drivergo.utils.Log.d(TAG, "onConnected - isConnected ...............: " +
                mGoogleApiClient.isConnected());
        startLocationUpdates();
        displayLocation();
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            com.radjago.drivergo.utils.Log.d(TAG, "Location update started ..............: ");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void displayLocation() {

    }
    @Override
    public void onConnectionSuspended(int arg0) {

    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (location != null && location.getLongitude() != 0.0 && location.getLongitude() != 0.0) {
                final User loginUser = BaseApp.getInstance(this).getLoginUser();
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                String bearing = String.valueOf(location.getBearing());
                UpdateLokasi(lat,lng,bearing);
                try {
                    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String desa = addresses.get(0).getSubLocality();
                        String kecamatan = addresses.get(0).getLocality();
                        Posnotif(desa + "," + kecamatan);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                com.radjago.drivergo.utils.Log.d("Lokasiku", loginUser.getId() + " location :"+mLastLocation.getLatitude()+" , "+mLastLocation.getLongitude());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void Posnotif(String Pesan){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = "_channel_01";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(id, "notification", importance);
            mChannel.enableLights(true);
            Notification notification = new Notification.Builder(getApplicationContext(), id)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(Pesan)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(2, notification);
            }

            startForeground(2, notification);
        }
    }
    //-----------------------------------------------------------------------------------------------------
    private void UpdateLokasi(String lat,String lng,String bearing) {
        final User user = BaseApp.getInstance(this).getLoginUser();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<LokasiResponse> update = api.updatelokasi(
                user.getId(),
                lat,lng,bearing);
        update.enqueue(new Callback<LokasiResponse>() {
            @Override
            public void onResponse(Call<LokasiResponse> call, Response<LokasiResponse> response) {
                Log.d("Retro", "Response");
              //  Toast.makeText(mContext, "Update Lokasi [Berhasil].",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LokasiResponse> call, Throwable t) {
                Log.d("Retro", "OnFailure");

            }
        });
    }
    //------------------------------ google service -------------------------------------------------
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
}