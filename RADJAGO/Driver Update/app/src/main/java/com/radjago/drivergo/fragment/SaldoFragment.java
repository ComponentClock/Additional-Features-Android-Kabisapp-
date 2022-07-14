package com.radjago.drivergo.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import com.radjago.drivergo.R;
import com.radjago.drivergo.activity.CreditcardActivity;
import com.radjago.drivergo.activity.MainActivity;
import com.radjago.drivergo.activity.WithdrawActivity;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.constants.Functions;
import com.radjago.drivergo.json.ResponseJson;
import com.radjago.drivergo.json.WithdrawRequestJson;
import com.radjago.drivergo.json.fcm.FCMMessage;
import com.radjago.drivergo.models.Notif;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.SettingPreference;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.FCMHelper;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class SaldoFragment extends Fragment {

    public static final int PAYPAL_REQUEST_CODE = 123;
    public static String Warna = "#1AC463";
    private static PayPalConfiguration configpaypal;
    String disableback;
    LinearLayout banktransfer, creditcard, paypal, payumoney;
    SettingPreference sp;
    ImageView backBtn, ImageBG;
    boolean debug;
    long mBackPressed;
    private View getView;
    private Context context;
    private ImageView backbtn;
    private TextView TxtSaldo;
    private EditText nominal;
    private ImageView text1, text2, text3, text4;
    private RelativeLayout rlnotif, rlprogress;
    private TextView textnotif;
    private Toolbar toolbar;
    private String paymentAmount;
    private long saldosaya;

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte[] messageDigest = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_saldo, container, false);
        context = getContext();
        toolbar = getView.findViewById(R.id.toolbar);
        backbtn = getView.findViewById(R.id.back_btn);
        TxtSaldo = getView.findViewById(R.id.txtsaldo);
        ImageBG = getView.findViewById(R.id.imagebackground);
        // View bottom_sheet = getView.findViewById(R.id.bottom_sheet);
        //  BottomSheetBehavior.from(bottom_sheet);
        sp = new SettingPreference(context);
        configpaypal = new PayPalConfiguration();
        if (sp.getSetting()[12].equals("1")) {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
        } else {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
        }
        configpaypal.clientId(sp.getSetting()[9]);

        Intent intent = new Intent(context, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);

        context.startService(intent);

        nominal = getView.findViewById(R.id.saldo);
        rlnotif = getView.findViewById(R.id.rlnotif);
        textnotif = getView.findViewById(R.id.textnotif);
        rlprogress = getView.findViewById(R.id.rlprogress);
        backBtn = getView.findViewById(R.id.back_btn);
        banktransfer = getView.findViewById(R.id.banktransfer);
        creditcard = getView.findViewById(R.id.creditcard);
        paypal = getView.findViewById(R.id.paypal);
        payumoney = getView.findViewById(R.id.payumoney);
        Warna = "#1AC463";
        String BGColor = Warna;
        int colorCodeDark = Color.parseColor(BGColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundColor(colorCodeDark);
            nominal.setTextColor(colorCodeDark);
            ImageBG.setBackgroundTintList(ColorStateList.valueOf(colorCodeDark));
        }
        nominal.addTextChangedListener(Utility.currencyTW(nominal, context));

        if (sp.getSetting()[10].equals("1")) {
            paypal.setVisibility(View.VISIBLE);
        } else {
            paypal.setVisibility(View.GONE);
        }

        if (sp.getSetting()[11].equals("1")) {
            creditcard.setVisibility(View.VISIBLE);
        } else {
            creditcard.setVisibility(View.GONE);
        }

        if (sp.getSetting()[18].equals("1")) {
            payumoney.setVisibility(View.VISIBLE);
        } else {
            payumoney.setVisibility(View.GONE);
        }


        payumoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    launchPayUMoneyFlow();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        banktransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    sheetlist();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    getPaypal();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    Intent i = new Intent(context, CreditcardActivity.class);
                    i.putExtra("price", convertAngka(nominal.getText().toString()));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });
        disableback = "false";
        getView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideSoftKeyboard(requireActivity());
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return getView;
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }

    @Override
    public void onStart() {
        super.onStart();
        User loginuser = BaseApp.getInstance(context).getLoginUser();
        String IdDriver = loginuser.getId();
        saldosaya = loginuser.getWalletSaldo();
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Utility.currencyTXT(TxtSaldo, String.valueOf(saldosaya), context);
        // TxtSaldo.setText((int) saldosaya);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(IdDriver, BarcodeFormat.QR_CODE, 1024, 1024);
            ImageView imageViewQrCode = (ImageView) getView.findViewById(R.id.qrCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (Exception e) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void sheetlist() {
        Intent i = new Intent(context, WithdrawActivity.class);
        i.putExtra("type", "topup");
        i.putExtra("nominal", convertAngka(nominal.getText().toString()));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void getPaypal() {
        Double Amount = Double.valueOf(convertAngka(nominal.getText().toString().replace(sp.getSetting()[4], "")));
        DecimalFormat formatter = new DecimalFormat("#,############,##");
        paymentAmount = formatter.format(Amount);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount),
                sp.getSetting()[13], "Topup " + getString(R.string.app_name),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("payment", paymentDetails);
                        submit();

                    } catch (JSONException e) {
                        Log.e("payment", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("payment", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("payment", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    Intent i = new Intent(context, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    User user = BaseApp.getInstance(context).getLoginUser();
                    Notif notif = new Notif();
                    notif.title = "Topup";
                    notif.message = "topup has been successful";
                    sendNotif(user.getToken(), notif);
                }


            } else if (resultModel != null && resultModel.getError() != null) {
                Log.e("TAG", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.e("TAG", "Both objects are null!");
            }
        }
    }

    private void submit() {
        progressshow();
        paymentAmount = nominal.getText().toString();
        final User user = BaseApp.getInstance(context).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank("paypal");
        request.setName(user.getFullnama());
        request.setAmount(convertAngka(paymentAmount.replace(sp.getSetting()[4], "")));
        request.setCard("1234");
        request.setNotelepon(user.getNoTelepon());
        request.setEmail(user.getEmail());

        DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
        service.topuppaypal(request).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                        Notif notif = new Notif();
                        notif.title = "Topup";
                        notif.message = "topup has been successful";
                        sendNotif(user.getToken(), notif);

                    } else {
                        notif("error, please check your account data!");
                    }
                } else {
                    notif("error!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {
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

    private void sendNotif(final String regIDTujuan, final Notif notif) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(notif);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void launchPayUMoneyFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setDoneButtonText("done");
        Double Amount = Double.valueOf(convertAngka(nominal.getText().toString().replace(sp.getSetting()[4], "")));
        DecimalFormat formatter = new DecimalFormat("#,############,##");
        paymentAmount = formatter.format(Amount);
        String payment = String.valueOf(new BigDecimal(paymentAmount));

        payUmoneyConfig.setPayUmoneyActivityTitle(getString(R.string.app_name));

        payUmoneyConfig.disableExitConfirmation(false);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(payment);

        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = BaseApp.getInstance(context).getLoginUser();
        String txnId = "INV-" + System.currentTimeMillis();
        String phone = user.getPhone();
        String productName = "topup";
        String firstName = user.getFullnama();
        String email = user.getEmail();
        String udf1 = user.getId();
        String udf2 = "PayuMoney";
        String udf3 = user.getPassword();
        String udf4 = "driver";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";
        debug = sp.getSetting()[14].equals("0");
        Log.e("", String.valueOf(amount));

        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(Constants.CONNECTION + "payumoney/payu")
                .setfUrl(Constants.CONNECTION + "payumoney/payu")
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(debug)
                .setKey(sp.getSetting()[15])
                .setMerchantId(sp.getSetting()[16]);

        try {
            PayUmoneySdkInitializer.PaymentParam mPaymentParams = builder.build();
            calculateServerSideHashAndInitiatePayment1(mPaymentParams);
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, (Activity) Objects.requireNonNull(getContext()), R.style.Payutheme, true);

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String convertAngka(String value) {
        return (((((value + "")
                .replaceAll(sp.getSetting()[4], ""))
                .replaceAll(" ", ""))
                .replaceAll(",", ""))
                .replaceAll("[$.]", ""));
    }

    private void calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5)).append("||||||");

        stringBuilder.append(sp.getSetting()[17]);

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

    }

}
