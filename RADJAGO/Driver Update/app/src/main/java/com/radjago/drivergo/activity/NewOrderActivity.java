package com.radjago.drivergo.activity;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.json.AcceptRequestJson;
import com.radjago.drivergo.json.AcceptResponseJson;
import com.radjago.drivergo.json.fcm.FCMMessage;
import com.radjago.drivergo.models.OrderFCM;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.PicassoTrustAll;
import com.radjago.drivergo.utils.SettingPreference;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.FCMHelper;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NewOrderActivity extends AppCompatActivity {
    @BindView(R.id.layanan)
    TextView layanantext;
    @BindView(R.id.layanandes)
    TextView layanandesctext;
    @BindView(R.id.pickUpText)
    TextView pickuptext;
    @BindView(R.id.destinationText)
    TextView destinationtext;
    @BindView(R.id.fitur)
    TextView estimatetext;
    @BindView(R.id.distance)
    TextView distancetext;
    @BindView(R.id.cost)
    TextView costtext;
    @BindView(R.id.price)
    TextView pricetext;
    @BindView(R.id.totaltext)
    TextView totaltext;
    @BindView(R.id.image)
    ImageView icon;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.distancetext)
    TextView distancetextes;
    @BindView(R.id.costtext)
    TextView costtextes;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.order)
    Button order;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.lldistance)
    LinearLayout lldistance;
    @BindView(R.id.linearfitur)
    LinearLayout LinFitur;
    @BindView(R.id.potongan)
    TextView Potongan;
    String waktuorder, iconfitur, diskon, layanan, layanandesc, alamatasal, alamattujuan, estimasitime, hargatotal, cost, distance, idtrans, regid, orderfitur, tokenmerchant, idpelanggan, idtransmerchant;
    String wallett;
    MediaPlayer BG;
    Vibrator v;
    SettingPreference sp;
    CountDownTimer timerplay = new CountDownTimer(20000, 1000) {

        @SuppressLint("SetTextI18n")
        public void onTick(long millisUntilFinished) {
            timer.setText("" + millisUntilFinished / 1000);
        }


        public void onFinish() {
            if (BG.isPlaying()) {
                BG.stop();
                timer.setText("0");
                Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
                toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toOrder);
            }

        }
    }.start();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        removeNotif();
        setScreenOnFlags();
        sp = new SettingPreference(this);
        sp.updateNotif("ON");
        Intent intent = getIntent();
        iconfitur = intent.getStringExtra("icon");
        layanan = intent.getStringExtra("layanan");
        layanandesc = intent.getStringExtra("layanandesc");
        alamatasal = intent.getStringExtra("alamat_asal");
        alamattujuan = intent.getStringExtra("alamat_tujuan");
        estimasitime = intent.getStringExtra("estimasi_time");
        hargatotal = intent.getStringExtra("harga");
        cost = intent.getStringExtra("biaya");
        diskon = intent.getStringExtra("kredit_promo");
        distance = intent.getStringExtra("distance");
        idtrans = intent.getStringExtra("id_transaksi");
        regid = intent.getStringExtra("reg_id");
        wallett = intent.getStringExtra("pakai_wallet");
        orderfitur = intent.getStringExtra("order_fitur");
        tokenmerchant = intent.getStringExtra("token_merchant");
        idpelanggan = intent.getStringExtra("id_pelanggan");
        idtransmerchant = intent.getStringExtra("id_trans_merchant");
        waktuorder = intent.getStringExtra("waktu_order");
        playSound();
        if (orderfitur.equals("15") | orderfitur.equals("16") | orderfitur.equals("17")) {
            LinFitur.setVisibility(View.GONE);
        } else {
            LinFitur.setVisibility(View.VISIBLE);
        }
        if (orderfitur.equalsIgnoreCase("3")) {
            lldestination.setVisibility(View.GONE);
            lldistance.setVisibility(View.GONE);

        }
        if (orderfitur.equalsIgnoreCase("4")) {

            estimatetext.setText(estimasitime);
            time.setText("Merchant");
            distancetextes.setText("Ongkir");
            costtextes.setText("Biaya");
            Utility.currencyTXT(distancetext, distance, this);
            Utility.currencyTXT(costtext, cost, this);
        } else {

            estimatetext.setText(estimasitime);
            distancetext.setText(distance);
            costtext.setText(cost);
        }
        Utility.currencyTXT(Potongan, String.valueOf(diskon), this);
        layanantext.setText(layanan);
        layanandesctext.setText(layanandesc);
        pickuptext.setText(alamatasal);
        destinationtext.setText(alamattujuan);
        long StrPotongan = Long.parseLong(diskon);
        long HargaTotal = Long.parseLong(hargatotal);
        long HasilPotongan = HargaTotal - StrPotongan;
        Utility.currencyTXT(pricetext, String.valueOf(HasilPotongan), this);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        long Saldoku = loginUser.getWalletSaldo();

        if (wallett.equalsIgnoreCase("true")) {
            totaltext.setText("Total (SALDO)");
            if (Saldoku < HasilPotongan) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Mohon Maaf Saldo Anda Tidak Mencukupi\nUntuk Mengambil Pesanan ini.!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (BG.isPlaying()) {
                                    BG.stop();
                                }
                                timerplay.cancel();
                                Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
                                toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(toOrder);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        } else {
            totaltext.setText("Total (TUNAI)");
        }
        PicassoTrustAll.getInstance(this)
                .load(Constants.IMAGESFITUR + iconfitur)
                .placeholder(R.drawable.logo)
                .resize(100, 100)
                .into(icon);

        timerplay.start();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BG.isPlaying()) {
                    BG.stop();
                }
                timerplay.cancel();
                Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
                toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toOrder);

            }
        });

        if (new SettingPreference(this).getSetting()[0].equals("OFF")) {
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getaccept();

                }
            });
        } else {
            getaccept();
        }


    }

    private void playSound() {
        BG = MediaPlayer.create(this, R.raw.orderan);
        final float maxVolume = 100.0f;
        float currentVolume = 100.0f;
        BG.setVolume(currentVolume / maxVolume, currentVolume / maxVolume);
        BG.setLooping(true);
        BG.start();
    }

    @Override
    public void onBackPressed() {
    }

    @SuppressLint("MissingPermission")
    private void getaccept() {
        if (BG.isPlaying()) {
            BG.stop();
        }
        timerplay.cancel();
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.accept(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {
                    sp.updateNotif("OFF");
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "2";
                        if (orderfitur.equalsIgnoreCase("4")) {
                            orderfcm.desc = "the driver is buying an order";
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-" + idtrans + idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(regid, orderfcm);
                    } else {
                        sp.updateNotif("OFF");
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(NewOrderActivity.this, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(NewOrderActivity.this, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
                sp.updateNotif("OFF");
                rlprogress.setVisibility(View.GONE);
                Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }

    private void sendMessageToDriver(final String regIDTujuan, final OrderFCM response) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(response);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).cancel(0);
    }

    private void setScreenOnFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            Objects.requireNonNull(keyguardManager).requestDismissKeyguard(this, null);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }
}
