package com.radjago.drivergo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.json.SaldoResponse;
import com.radjago.drivergo.json.WalletRespon;
import com.radjago.drivergo.json.WithdrawRequestJson;
import com.radjago.drivergo.json.WithdrawResponseJson;
import com.radjago.drivergo.json.fcm.FCMMessage;
import com.radjago.drivergo.models.DataCustomer;
import com.radjago.drivergo.models.Notif;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.SdkConfig;
import com.radjago.drivergo.utils.api.FCMHelper;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPembayaran extends AppCompatActivity implements TransactionFinishedCallback {
    public static String Jumlah, Metode, NumberCard,Type;
    LinearLayout clikGopay, clickIndomaret, clickAkulaku, clickTransfer;
    TextView txtnominal;
    EditText saldo;
    RelativeLayout sepuluh, duapuluh, limapuluh, seratusribu;
    ImageView imgnoselect;
    ScrollView selectpay;
    ProgressDialog pd;
    private static String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pembayaran);
        selectpay = (ScrollView) findViewById(R.id.selectpay);
        imgnoselect = (ImageView) findViewById(R.id.imgnoselect);
        txtnominal = (TextView) findViewById(R.id.txtnominal);
        saldo = (EditText) findViewById(R.id.saldo);
        sepuluh = (RelativeLayout) findViewById(R.id.sepuluh);
        duapuluh = (RelativeLayout) findViewById(R.id.duapuluh);
        limapuluh = (RelativeLayout) findViewById(R.id.limapuluh);
        seratusribu = (RelativeLayout) findViewById(R.id.seratusribu);
        clikGopay = (LinearLayout) findViewById(R.id.gopay);
        clickIndomaret = (LinearLayout) findViewById(R.id.indomaret);
        clickAkulaku = (LinearLayout) findViewById(R.id.akulaku);
        clickTransfer = (LinearLayout) findViewById(R.id.banktransfer);
        initMidtranSdk();
        // clickPay();
        if (txtnominal.equals("")) {
            txtnominal.setText("");
            saldo.setText("");
            imgnoselect.setVisibility(View.VISIBLE);
            selectpay.setVisibility(View.GONE);
        }
        pd = new ProgressDialog(this);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        sepuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtnominal.setText("10000");
                Double getprice = Double.valueOf(txtnominal.getText().toString());
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                saldo.setText(ValFormat);
                imgnoselect.setVisibility(View.GONE);
                selectpay.setVisibility(View.VISIBLE);
            }
        });
        duapuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtnominal.setText("20000");
                Double getprice = Double.valueOf(txtnominal.getText().toString());
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                saldo.setText(ValFormat);
                imgnoselect.setVisibility(View.GONE);
                selectpay.setVisibility(View.VISIBLE);
            }
        });
        limapuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtnominal.setText("50000");
                Double getprice = Double.valueOf(txtnominal.getText().toString());
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                saldo.setText(ValFormat);
                imgnoselect.setVisibility(View.GONE);
                selectpay.setVisibility(View.VISIBLE);
            }
        });
        seratusribu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtnominal.setText("100000");
                Double getprice = Double.valueOf(txtnominal.getText().toString());
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                saldo.setText(ValFormat);
                imgnoselect.setVisibility(View.GONE);
                selectpay.setVisibility(View.VISIBLE);
            }
        });

        clikGopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int min = 20;
                final int max = 80;
                final int random = new Random().nextInt((max - min) + 1) + min;
                String iddata = String.valueOf(random);
                String Saldo = txtnominal.getText().toString();
                int price = Integer.parseInt(Saldo);
                int qty = 1;
                String product_name = "Topup Saldo";
                Jumlah = Saldo;
                Metode = "Gopay";
                NumberCard = Metode + iddata;

                MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata, price, qty, product_name));
                MidtransSDK.getInstance().startPaymentUiFlow(ActivityPembayaran.this, PaymentMethod.GO_PAY);
            }
        });
        clickIndomaret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int min = 20;
                final int max = 80;
                final int random = new Random().nextInt((max - min) + 1) + min;
                String iddata = String.valueOf(random);
                String Saldo = txtnominal.getText().toString();
                int price = Integer.parseInt(Saldo);
                int qty = 1;
                String product_name = "Topup Saldo";
                Jumlah = Saldo;
                Metode = "Indomart";
                NumberCard = Metode + iddata;
                MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata, price, qty, product_name));
                MidtransSDK.getInstance().startPaymentUiFlow(ActivityPembayaran.this, PaymentMethod.INDOMARET);
            }
        });
        clickAkulaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int min = 20;
                final int max = 80;
                final int random = new Random().nextInt((max - min) + 1) + min;
                String iddata = String.valueOf(random);
                String Saldo = txtnominal.getText().toString();
                int price = Integer.parseInt(Saldo);
                int qty = 1;
                String product_name = "Topup Saldo";
                Jumlah = Saldo;
                Metode = "Akulaku";
                NumberCard = Metode + iddata;
                MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata, price, qty, product_name));
                MidtransSDK.getInstance().startPaymentUiFlow(ActivityPembayaran.this, PaymentMethod.AKULAKU);
            }
        });
        clickTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int min = 20;
                final int max = 80;
                final int random = new Random().nextInt((max - min) + 1) + min;
                String iddata = String.valueOf(random);
                String Saldo = txtnominal.getText().toString();
                int price = Integer.parseInt(Saldo);
                int qty = 1;
                String product_name = "Topup Saldo";
                Jumlah = Saldo;
                Metode = "Transfer";
                NumberCard = Metode + iddata;
                MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata, price, qty, product_name));
                MidtransSDK.getInstance().startPaymentUiFlow(ActivityPembayaran.this, PaymentMethod.BANK_TRANSFER);
            }
        });

    }

    private void initMidtranSdk() {
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        UIKitCustomSetting uisetting = new
                UIKitCustomSetting();
        uisetting.setShowPaymentStatus(true);
        uisetting.setSaveCardChecked(true);
        uisetting.setSkipCustomerDetailsPages(true);
        uisetting.setEnabledAnimation(true);
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(SdkConfig.MERCHANT_BASE_CHECKOUT_URL)
                .setClientKey(SdkConfig.MERCHANT_CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(false)
                .setUIkitCustomSetting(uisetting)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
        // Set user details
        UserDetail userDetail = new UserDetail();
        userDetail.setUserFullName(loginUser.getFullnama());
        userDetail.setEmail(loginUser.getEmail());
        userDetail.setPhoneNumber(loginUser.getNoTelepon());
        userDetail.setUserId(loginUser.getId());
        ArrayList<UserAddress> userAddresses = new ArrayList<>();
        UserAddress shippingUserAddress = new UserAddress();
        shippingUserAddress.setAddress(loginUser.getAlamat());
        shippingUserAddress.setCity(null);
        shippingUserAddress.setCountry(null);
        shippingUserAddress.setZipcode("0000");
        shippingUserAddress.setAddressType(Constants.ADDRESS_TYPE_SHIPPING);
        userAddresses.add(shippingUserAddress);
    }

    private void clickPay() {
        final int min = 20;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String iddata = String.valueOf(random);
        int price = 50000;
        int qty = 1;
        String product_name = "Topup";
        MidtransSDK.getInstance().setTransactionRequest(DataCustomer.transactionRequest(iddata, price, qty, product_name));
        MidtransSDK.getInstance().startPaymentUiFlow(this);
    }


    @Override
    public void onTransactionFinished(TransactionResult result) {
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                //    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                 //   SendSaldo();
                    Type = result.getResponse().getPaymentType();
                //    input_saldo(Jumlah, "midtrans", Type);
                        TransferWallet(Jumlah);
                 //   addwallet(loginUser.getId(),Jumlah,result.getResponse().getBank(),loginUser.getFullnama(), String.valueOf(result.getResponse().getAccountNumbers()),result.getResponse().getPaymentType());
                  //  finish();
                    break;
                case TransactionResult.STATUS_PENDING:
                 //   Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    submit(Jumlah, result.getResponse().getPaymentType(), result.getResponse().getBank());
                   // finish();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                  //  finish();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
            txtnominal.setText("");
            saldo.setText("");
            imgnoselect.setVisibility(View.VISIBLE);
            selectpay.setVisibility(View.GONE);
        } else {
            if (result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))) {
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void submit(String jumlah, String bank, String Rek) {
        final User user = BaseApp.getInstance(this).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank(bank);
        request.setName(user.getFullnama());
        request.setAmount(jumlah);
        request.setCard(Rek);
        request.setNotelepon(user.getNoTelepon());
        request.setEmail(user.getEmail());
        request.setType("topup");
        DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
        service.withdraw(request).enqueue(new Callback<WithdrawResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<WithdrawResponseJson> call, @NonNull Response<WithdrawResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        Notif notif = new Notif();
                        notif.title = "Topup";
                        notif.message = "Permintaan Isi Ulang Berhasil\nTunggu Sampai Staff Kami Mengkonfirmasi Permintaan Anda.";
                        sendNotif(user.getToken(), notif);
                        Intent intent = new Intent(ActivityPembayaran.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityPembayaran.this, "error, please check your account data!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ActivityPembayaran.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WithdrawResponseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(ActivityPembayaran.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void input_saldo(String jumlah, String bank, String Rek) {
        final User user = BaseApp.getInstance(this).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank(bank);
        request.setName(user.getFullnama());
        request.setAmount(jumlah);
        request.setCard(Rek);
        request.setEmail(user.getEmail());
        request.setNotelepon(user.getNoTelepon());
        request.setType("topup");
        DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
        service.midtranspay(request).enqueue(new Callback<WithdrawResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<WithdrawResponseJson> call, @NonNull Response<WithdrawResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        Notif notif = new Notif();
                        notif.title = "Topup";
                        notif.message = "Permintaan Isi Ulang Saldo Berhasil.";
                        sendNotif(user.getToken(), notif);

                        Intent intent = new Intent(ActivityPembayaran.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityPembayaran.this, "error, please check your account data!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ActivityPembayaran.this, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WithdrawResponseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(ActivityPembayaran.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendNotif(final String regIDTujuan, final Notif notif) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(notif);

        FCMHelper.sendMessage(com.radjago.drivergo.constants.Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void SendSaldo() {
        final User user = BaseApp.getInstance(this).getLoginUser();
        long SaldoDriver = user.getWalletSaldo();
        long Tambah = Long.parseLong(Jumlah);
        long HasilTips = SaldoDriver + Tambah;
        String KasihTips = String.valueOf(HasilTips);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<SaldoResponse> update = api.updateSaldoUser(user.getId(),
                KasihTips);
        update.enqueue(new Callback<SaldoResponse>() {
            @Override
            public void onResponse(Call<SaldoResponse> call, Response<SaldoResponse> response) {
                Log.d("Retro", "Response");
                Toast.makeText(ActivityPembayaran.this, "Topup Saldo [Berhasil].",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(ActivityPembayaran.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<SaldoResponse> call, Throwable t) {
                Log.d("Retro", "OnFailure");

            }
        });
    }
    //-------------------- add to histori --------------------------------------------------------
    private void TransferWallet(String Jumlah){
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        pd.setMessage("Tunggu Ya ....");
        pd.setCancelable(false);
        pd.show();
        String sid = loginUser.getId();
        String sbank = "Midtrans";
        String spemilik = loginUser.getFullnama();
        String sRekening = "-";
        String sTipe = "Topup+";
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<WalletRespon> sendbio = api.sendwallet(sid,Jumlah,sbank,loginUser.getFullnama(),sRekening,spemilik,sTipe,"1");
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
    //-------------------------------- xendit -----------------------------------------------
}