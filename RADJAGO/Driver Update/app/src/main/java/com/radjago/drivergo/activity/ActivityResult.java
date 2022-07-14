package com.radjago.drivergo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.json.SaldoResponse;
import com.radjago.drivergo.json.WalletRespon;
import com.radjago.drivergo.json.fcm.FCMMessage;
import com.radjago.drivergo.models.DriverModel;
import com.radjago.drivergo.models.Notif;
import com.radjago.drivergo.models.SaldoModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.ScanView;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.FCMHelper;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.radjago.drivergo.constants.Constants.IMAGESDRIVER;

public class ActivityResult extends AppCompatActivity {
    private static final String TAG = ActivityResult.class.getSimpleName();
    public static String Saldo, Regid,sNama,sBarcode;
    private TextView txtName, txtid, txtJk, txtSaldo, txtreg, txtPrice, mysaldo, txtError,setsaldo;
    private CircleImageView imgPoster;
    private EditText EditSaldo;
    private Button btnKirim;
    private ProgressBar progressBar;
    private ScanView ticketView;
    private List<DriverModel> mDriver = new ArrayList<DriverModel>();
    ProgressDialog pd;
    //------------------------------- Saldo Driver ---------------------------
    private List<SaldoModel> mSaldo = new ArrayList<>();

    private static String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mysaldo = findViewById(R.id.mysaldo);
        setsaldo = findViewById(R.id.setsaldo);
        txtreg = findViewById(R.id.txtreg);
        txtName = findViewById(R.id.namadriver);
        txtid = findViewById(R.id.txtid);
        txtJk = findViewById(R.id.txtJk);
        imgPoster = findViewById(R.id.poster);
        txtSaldo = findViewById(R.id.txtsaldo);
        EditSaldo = findViewById(R.id.EditSaldo);
        txtError = findViewById(R.id.txt_error);
        btnKirim = findViewById(R.id.btnKirim);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        long tsaldo = loginUser.getWalletSaldo();
        mysaldo.setText(String.valueOf(tsaldo));
        String barcode = getIntent().getStringExtra("code");
        String nama = getIntent().getStringExtra("nama");
        sNama = getIntent().getStringExtra("nama");
        String kelamin = getIntent().getStringExtra("kelamin");
        String token = getIntent().getStringExtra("token");
        String foto = getIntent().getStringExtra("foto");
        txtName.setText(nama);
        txtreg.setText(token);
        if (kelamin.equals("Male")) {
            txtJk.setText("Laki-Laki");
        } else {
            txtJk.setText("Perempuan");
        }
        pd = new ProgressDialog(this);
        String Baseurl = IMAGESDRIVER + foto;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder);
        Glide.with(ActivityResult.this)
                .load(Baseurl)
                .apply(options)
                .into(imgPoster);
        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(getApplicationContext(), "Barcode is empty!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            txtid.setText(barcode);
            final ProgressDialog progressDialog = new ProgressDialog(ActivityResult.this);
            progressDialog.setMessage("Please wait data is Processing");
            progressDialog.show();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        SaldoDriver(barcode);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        EditSaldo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().isEmpty()){
                    EditSaldo.setText("0");
                    setsaldo.setText("0");
                }else{
                    setsaldo.setText(s);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void SaldoDriver(String IdUser) {
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<SaldoResponse> getdata = api.cekSaldo(IdUser);
        getdata.enqueue(new Callback<SaldoResponse>() {
            @Override
            public void onResponse(Call<SaldoResponse> call, Response<SaldoResponse> response) {
                com.radjago.drivergo.utils.Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mSaldo = response.body().getResult();
                String CSaldo = mSaldo.get(0).getSaldo();
                Saldo = CSaldo;
                Long cekSaldo = Long.parseLong(txtSaldo.getText().toString());
                Utility.currencyTXT(txtSaldo, CSaldo, ActivityResult.this);
                btnKirim.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(Integer.parseInt(mysaldo.getText().toString()) < Integer.parseInt(setsaldo.getText().toString()))
                        {
                            Toast .makeText(getApplicationContext(), "Saldo Tidak Mencukupi!", Toast.LENGTH_LONG).show();
                        }else{
                            PotongSaldo(CSaldo);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<SaldoResponse> call, Throwable t) {
                com.radjago.drivergo.utils.Log.d("RETRO", "FAILED : respon gagal");
            }
        });
    }

    //Update Saldo Customers
    private void UpdateSaldoUser(String Id, String Saldo) {
        String iddriver = Id;
        int angka1;
        int angka2;
        int hasil;
        angka1 = Integer.parseInt(Saldo);
        angka2 = Integer.parseInt(EditSaldo.getText().toString());
        hasil = angka1 + angka2;
        String Tukarkan = String.valueOf(hasil);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<SaldoResponse> update = api.updateSaldoUser(iddriver, Tukarkan);
        update.enqueue(new Callback<SaldoResponse>() {
            @Override
            public void onResponse(Call<SaldoResponse> call, Response<SaldoResponse> response) {
                android.util.Log.d("Retro", "Response");
                // Toast .makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SaldoResponse> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");
                // Toast .makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Update Saldo Customers
    private void PotongSaldo(String txtSaldo) {
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        long angka1;
        long angka2;
        long hasil;
        angka1 = loginUser.getWalletSaldo();
        angka2 = Long.parseLong(EditSaldo.getText().toString());
        hasil = angka1 - angka2;
        String Tukarkan = String.valueOf(hasil);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<SaldoResponse> update = api.updateSaldoUser(loginUser.getId(), Tukarkan);
        update.enqueue(new Callback<SaldoResponse>() {
            @Override
            public void onResponse(Call<SaldoResponse> call, Response<SaldoResponse> response) {
                android.util.Log.d("Retro", "Response");

              //  Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                String iduser = txtid.getText().toString();
                UpdateSaldoUser(iduser, txtSaldo);
                String mSaldo = EditSaldo.getText().toString();
                Double getprice = Double.valueOf(mSaldo);
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                Notif notif = new Notif();
                notif.title = "Transfer Saldo";
                notif.message = "Anda Telah Menerima Saldo Sebesar " + ValFormat + " Dari " + loginUser.getFullnama();
                sendNotif(txtreg.getText().toString(), notif);
                SaveWallet(String.valueOf(angka2));
            }

            @Override
            public void onFailure(Call<SaldoResponse> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");
            }
        });
    }

    private void sendNotif(final String regIDTujuan, final Notif notif) {
        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(notif);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                Intent i = new Intent(ActivityResult.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("IsWorking", false);
                startActivity(i);
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }
    //------------------------------------ save to wallet ------------------------------------------
     private void SaveWallet(String Jumlah){
            final User loginUser = BaseApp.getInstance(this).getLoginUser();
             pd.setMessage("Tunggu Ya ....");
             pd.setCancelable(false);
             pd.show();
             String sid = loginUser.getId();
             String sbank = "-";
             String spemilik = loginUser.getFullnama();
             String sRekening = "-";
             String sTipe = "Transfer-";
             MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
             Call<WalletRespon> sendbio = api.sendwallet(sid,Jumlah,sbank,spemilik,sRekening,sNama,sTipe,"1");
             sendbio.enqueue(new Callback<WalletRespon>() {
                 @Override
                 public void onResponse(Call<WalletRespon> call, Response<WalletRespon> response) {
                     pd.hide();
                     Log.d("RETRO", "response : " + response.body().toString());
                     String kode = response.body().getKode();
                     if(kode.equals("1"))
                     {
                         TransferWallet(Jumlah);
                         Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                         Log.d("Transfer", "Data berhasil disimpan");
                     }else
                     {
                         Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_LONG).show();
                         Log.d("Transfer", "Data berhasil gagal");
                     }
                 }

                 @Override
                 public void onFailure(Call<WalletRespon> call, Throwable t) {
                     pd.hide();
                     Log.d("RETRO", "Falure : " + "Gagal Mengirim Request");
                 }
             });
     }
    private void TransferWallet(String Jumlah){
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        pd.setMessage("Tunggu Ya ....");
        pd.setCancelable(false);
        pd.show();
        String sid = txtid.getText().toString();
        String sbank = "-";
        String spemilik = loginUser.getFullnama();
        String sRekening = "-";
        String sTipe = "Transfer+";
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<WalletRespon> sendbio = api.sendwallet(sid,Jumlah,sbank,sNama,sRekening,spemilik,sTipe,"1");
        sendbio.enqueue(new Callback<WalletRespon>() {
            @Override
            public void onResponse(Call<WalletRespon> call, Response<WalletRespon> response) {
                pd.hide();
                Log.d("RETRO", "response : " + response.body().toString());
                String kode = response.body().getKode();
                if(kode.equals("1"))
                {
                    Log.d("Transfer", "Data berhasil disimpan");
                }else
                {
                    Log.d("Transfer", "Data berhasil gagal");
                }
            }

            @Override
            public void onFailure(Call<WalletRespon> call, Throwable t) {
                pd.hide();
                Log.d("RETRO", "Falure : " + "Gagal Mengirim Request");
            }
        });
    }
}