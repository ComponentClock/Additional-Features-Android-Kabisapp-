package com.radjago.drivergo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import com.radjago.drivergo.R;
import com.radjago.drivergo.json.DriverResponse;
import com.radjago.drivergo.models.DriverModel;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityScan extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    BarcodeReader barcodeReader;
    private List<DriverModel> mDriver = new ArrayList<DriverModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {

        barcodeReader.playBeep();
        if (barcode.displayValue != null) {
            ResultDriver(barcode.displayValue);
        }

    }
    private void ResultDriver(String id) {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<DriverResponse> getdata = api.cekDriver(id);
        getdata.enqueue(new Callback<DriverResponse>() {
            @Override
            public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mDriver = response.body().getResult();
                Intent intent = new Intent(ActivityScan.this, ActivityResult.class);
                intent.putExtra("code", id);
                intent.putExtra("nama", mDriver.get(0).getFullnama());
                intent.putExtra("kelamin", mDriver.get(0).getJenis());
                intent.putExtra("token", mDriver.get(0).getToken());
                intent.putExtra("foto", mDriver.get(0).getFotodriver());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<DriverResponse> call, Throwable t) {
                Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }
    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
