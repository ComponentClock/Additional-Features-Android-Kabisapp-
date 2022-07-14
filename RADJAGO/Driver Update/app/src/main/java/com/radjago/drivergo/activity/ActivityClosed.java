package com.radjago.drivergo.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

import com.radjago.drivergo.R;

public class ActivityClosed extends AppCompatActivity {
    Timer timer = new Timer();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed);
    }
}
