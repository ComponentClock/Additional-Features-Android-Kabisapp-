package com.radjago.drivergo.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.radjago.drivergo.R;
import com.radjago.drivergo.adapter.PointAdapter;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.json.PointRespon;
import com.radjago.drivergo.models.PointModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPoint extends AppCompatActivity {
    public static String Warna = "#7309A8";
    public static String sisReward;
    ProgressDialog pd;
    private ImageView swLayout;
    private RecyclerView mRecycler;
    private PointAdapter mAdapter;
    private PointAdapter pointAdapter;
    private ScrollView sview;
    private TextView TxtPoint, TPoint, DStatus;
    private RecyclerView.LayoutManager mManager;
    private Toolbar toolbar;
    private List<PointModel> mItems = new ArrayList<>();
    private List<PointModel> mPoint = new ArrayList<PointModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        pd = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        swLayout = (ImageView) findViewById(R.id.swlayout);
        mRecycler = (RecyclerView) findViewById(R.id.recyclerTemp);
        TxtPoint = (TextView) findViewById(R.id.TxtPoint);
        TPoint = (TextView) findViewById(R.id.TPoint);
        DStatus = (TextView) findViewById(R.id.DStatus);
        sview = (ScrollView) findViewById(R.id.adpPoint);
        sview.setScrollbarFadingEnabled(false);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mManager);
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        swLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                swLayout.startAnimation(rotate);
                TampilPoint();
                PointDriver();
            }
        });

        TampilPoint();
        PointDriver();
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            int dNomor = (int) b.get("nomor");
            String dNama = (String) b.get("nama");
            String dReward = (String) b.get("reward");
            String dPoint = (String) b.get("point");
            String dTipe = (String) b.get("tipe");
            String nomor = String.valueOf(dNomor);
            TPoint.setText(dPoint);
            sisReward = dReward;
            TukarPoint();

        }
        //tema
        Warna = "#7309A8";
        String BGColor = Warna;
        int colorCodeDark = Color.parseColor(BGColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundColor(Color.parseColor(Warna));
        }

    }

    private void PointDriver() {
        User loginUser = BaseApp.getInstance(ActivityPoint.this).getLoginUser();
        String iddriver = loginUser.getId();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<PointRespon> getdata = api.cekPoint(iddriver);
        getdata.enqueue(new Callback<PointRespon>() {
            @Override
            public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mPoint = response.body().getResult();
                String CPoint = mPoint.get(0).getPoint();
                TxtPoint.setText(CPoint + " POINT");
                //  TPoint.setText(CPoint);
            }

            @Override
            public void onFailure(Call<PointRespon> call, Throwable t) {
                Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }

    private void TampilPoint() {
        pd.setMessage("Loading ...");
        pd.setCancelable(false);
        pd.show();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<PointRespon> getdata = api.getPoint("point");
        getdata.enqueue(new Callback<PointRespon>() {
            @Override
            public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                pd.hide();
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mItems = response.body().getResult();
                if (mItems != null && mItems.size() > 0) {
                    mAdapter = new PointAdapter(ActivityPoint.this, mItems);
                    mRecycler.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<PointRespon> call, Throwable t) {
                pd.hide();
                Log.d("RETRO", "FAILED : respon gagal");
            }
        });
    }

    private void TukarPoint() {
        DStatus.setVisibility(View.VISIBLE);
        pd.setMessage("update ....");
        pd.setCancelable(false);
        pd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                User loginUser = BaseApp.getInstance(ActivityPoint.this).getLoginUser();
                String iddriver = loginUser.getId();
                int angka1;
                int angka2;
                int hasil;
                String GPoint = TxtPoint.getText().toString();
                String JPoint = GPoint.replaceAll(" POINT", "");
                angka1 = Integer.parseInt(JPoint);
                angka2 = Integer.parseInt(TPoint.getText().toString());
                hasil = angka1 - angka2;
                String Tukarkan = String.valueOf(hasil);
                int CPoint = Integer.parseInt(TPoint.getText().toString());
                int PointSaya = Integer.parseInt(JPoint);
                if (PointSaya < CPoint) {
                    Toast.makeText(ActivityPoint.this, "Mohon Maaf Point Anda Tidak Cukup.", Toast.LENGTH_SHORT).show();
                    pd.hide();
                    DStatus.setVisibility(View.GONE);
                } else {
                    MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
                    Call<PointRespon> update = api.tukarPoint(iddriver, Tukarkan);
                    update.enqueue(new Callback<PointRespon>() {
                        @Override
                        public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                            Log.d("Retro", "Response");
                            Toast.makeText(ActivityPoint.this, response.body().getPesan(), Toast.LENGTH_SHORT).show();
                            UpdateSaldo(sisReward);
                            pd.hide();
                            DStatus.setVisibility(View.GONE);
                            PointDriver();
                        }

                        @Override
                        public void onFailure(Call<PointRespon> call, Throwable t) {
                            pd.hide();
                            DStatus.setVisibility(View.GONE);
                            Log.d("Retro", "OnFailure");

                        }
                    });
                    pd.dismiss();
                }

            }
        }, 3000); // 3000 milliseconds delay

    }

    //Update Saldo
    private void UpdateSaldo(String Reward) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                User loginUser = BaseApp.getInstance(ActivityPoint.this).getLoginUser();
                String iddriver = loginUser.getId();
                String Saldo = String.valueOf(loginUser.getWalletSaldo());
                int angka1;
                int angka2;
                int hasil;
                angka1 = Integer.parseInt(Saldo);
                angka2 = Integer.parseInt(Reward);
                hasil = angka1 + angka2;
                String Tukarkan = String.valueOf(hasil);
                MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
                Call<PointRespon> update = api.updateSaldo(iddriver, Tukarkan);
                update.enqueue(new Callback<PointRespon>() {
                    @Override
                    public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                        Log.d("Retro", "Response");
                        // Toast.makeText(ActivityPoint.this,response.body().getPesan(),Toast.LENGTH_SHORT).show();
                        PointDriver();
                        Locale localeID = new Locale("in", "ID");
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                        Double TotReward = Double.valueOf(Reward);
                        String Pesan = "Anda Mendapat Tambahan Saldo Sebesar " + formatRupiah.format((double) TotReward) + "\nDari Penukaran Point Anda.";
                        SendNotif(Pesan);
                    }

                    @Override
                    public void onFailure(Call<PointRespon> call, Throwable t) {
                        Log.d("Retro", "OnFailure");

                    }
                });
                pd.dismiss();
            }
        }, 3000); // 3000 milliseconds delay

    }

    //notifikasi
    private void SendNotif(String Pesan) {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notifpoint);
        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel("notify_002", "Point", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(Pesan);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(soundUri, audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "notify_002")
                .setSmallIcon(R.drawable.walletimg)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.walletimg))
                .setTicker("POINT")
                .setContentTitle("Point")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Pesan))
                .setContentText(Pesan)
                .setAutoCancel(true)
                .setLights(0xff0000ff, 300, 1000) // blue color
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mBuilder.setSound(soundUri);
        }

        int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}