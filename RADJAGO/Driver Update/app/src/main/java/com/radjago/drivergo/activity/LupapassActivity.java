package com.radjago.drivergo.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import com.radjago.drivergo.R;
import com.radjago.drivergo.json.LoginRequestJson;
import com.radjago.drivergo.json.LoginResponseJson;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LupapassActivity extends AppCompatActivity {

    ImageView backbtn, BgHeader;
    Button submit;
    TextView email, notiftext;
    RelativeLayout rlnotif, rlprogress;
    String disableback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapassword);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int MainBG = Color.parseColor("#7309A8");
        BgHeader = findViewById(R.id.bgheader);
        backbtn = findViewById(R.id.back_btn_verify);
        submit = findViewById(R.id.buttonconfirm);
        email = findViewById(R.id.email);
        notiftext = findViewById(R.id.textnotif2);
        rlnotif = findViewById(R.id.rlnotif2);
        rlprogress = findViewById(R.id.rlprogress);

        disableback = "false";

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BgHeader.setBackgroundTintList(ColorStateList.valueOf(MainBG));
            submit.setBackgroundTintList(ColorStateList.valueOf(MainBG));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    notif(getString(R.string.emailempty));
                } else {
                    get();
                }

            }
        });

    }

    private void get() {
        progressshow();
        LoginRequestJson request = new LoginRequestJson();
        request.setEmail(email.getText().toString());

        DriverService service = ServiceGenerator.createService(DriverService.class, request.getEmail(), "12345");
        service.forgot(request).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseJson> call, @NonNull Response<LoginResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("found")) {
                        notif("email send!");

                    } else {
                        notif(getString(R.string.phoneemailwrong));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseJson> call, @NonNull Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("error");
            }
        });
    }

    public void progressshow() {
        rlprogress.setVisibility(View.VISIBLE);
        disableback = "true";
    }

    public void progresshide() {
        rlprogress.setVisibility(View.GONE);
        disableback = "false";
    }

    @Override
    public void onBackPressed() {
        if (!disableback.equals("true")) {
            finish();
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        notiftext.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }


}
