package com.radjago.drivergo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;
import com.transferwise.sequencelayout.SequenceStep;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import com.radjago.drivergo.R;
import com.radjago.drivergo.activity.ChatActivity;
import com.radjago.drivergo.activity.MainActivity;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.item.ItemPesananItem;
import com.radjago.drivergo.json.AcceptRequestJson;
import com.radjago.drivergo.json.AcceptResponseJson;
import com.radjago.drivergo.json.DetailRequestJson;
import com.radjago.drivergo.json.DetailTransResponseJson;
import com.radjago.drivergo.json.JobResponse;
import com.radjago.drivergo.json.KomisiResponse;
import com.radjago.drivergo.json.PointRespon;
import com.radjago.drivergo.json.ResponseJson;
import com.radjago.drivergo.json.SaldoResponse;
import com.radjago.drivergo.json.TransaksiRespon;
import com.radjago.drivergo.json.UpdateLocationRequestJson;
import com.radjago.drivergo.json.UpdateStatusRequest;
import com.radjago.drivergo.json.VerifyRequestJson;
import com.radjago.drivergo.json.fcm.CancelBookRequestJson;
import com.radjago.drivergo.json.fcm.CancelBookResponseJson;
import com.radjago.drivergo.json.fcm.DriverResponse;
import com.radjago.drivergo.json.fcm.FCMMessage;
import com.radjago.drivergo.models.DataDriver;
import com.radjago.drivergo.models.FcmDriver;
import com.radjago.drivergo.models.FiturModel;
import com.radjago.drivergo.models.ItemPesananModel;
import com.radjago.drivergo.models.JobModels;
import com.radjago.drivergo.models.Notif;
import com.radjago.drivergo.models.OrderFCM;
import com.radjago.drivergo.models.PelangganModel;
import com.radjago.drivergo.models.PointModel;
import com.radjago.drivergo.models.SaldoModel;
import com.radjago.drivergo.models.TransaksiModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.mwmap.MaswendMarker;
import com.radjago.drivergo.mwmap.UpdateLocationCallBack;
import com.radjago.drivergo.mwmap.model.DirectionResponses;
import com.radjago.drivergo.service.MWService;
import com.radjago.drivergo.utils.BackgroundColorTransform;
import com.radjago.drivergo.utils.Log;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.FCMHelper;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.radjago.drivergo.constants.Constants.BASE_JOB;
import static com.radjago.drivergo.json.fcm.FCMType.ORDER;

@SuppressLint("Registered")
public class OrderFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_PERMISSION_CALL = 992;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    private JobScheduler jobScheduler;
    private ComponentName componentName;
    private JobInfo jobInfo;
    Runnable runnable;
    Handler handler;

    //------------------------------------------------------------------------------------------
    public static String LinkIkon;
    public static String mEstimasi;
    public static String mJarak;
    public static String mCustomer;
    public static String mBiaya;
    public static String mHome;
    public static String InitPoint;
    public static boolean iswallet;
    public static double lokasiA = 0;
    public static double lokasiB = 0;
    //-------------------------- Kebutuhan Marker --------------
    Location mLastLocation;
    private GoogleMap mMap;
    private Context context;
    private LatLng driverlatlng;
    private float driverbearing = 0;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 2000; //1.5 min
    private static final long FASTEST_INTERVAL = 1000; //1.5 min
    private static final long DISPLACEMENT = 5; //5 meter
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location oldLocation;
    private int markerCount=0;
    private SupportMapFragment mapFragment;
    private static final int REQUEST_LOCATION = 0;
    private boolean isFirstTime = true;
    private Marker marker;

    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    public static String AsalA = null;
    public static String AsalB = null;
    //----------------------------------------------------------
    public static String Estimasi;
    public static String Apikey;
    public static String Status;
    public static String Komisi;
    public static String CekRute;
    public static String Warna;
    public static String OnStatus;
    public static String SaldoUser;
    public static String TokenMerchant;
    public static String tokenPelanggan, tokenmerchant;
    public static String CekHarga;
    public static int Screen;
    Bitmap BitMapMarker;
    //------------------ Service ----------------------
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude,longitude;
    Float bearings;
    Geocoder geocoder;
    private Location mLocation;
    //--------------------- uber var -----------------------------
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private LatLng tujuanLatLng;
    private boolean isWorking;
    private boolean IsWallet;
    private String idtrans, idpelanggan, response, fitur, onsubmit;
    private boolean isCancelable = true;
    private ItemPesananItem itemPesananItem;
    private TextView totaltext;
    private String type;
    private double AsalLat, AsalLng, TujuanLat, TujuanLng;
    private List<JobModels> mJob = new ArrayList<>();
    private List<ItemPesananModel> iPesanan = new ArrayList<ItemPesananModel>();
    //-------------------------------- cek komisi admin ---------------------------------------------
    private List<FiturModel> mFitur = new ArrayList<FiturModel>();
    private List<SaldoModel> mSaldo = new ArrayList<>();
    //------------------------------------------------- Status CS -------------------------------------------------------
    private List<PointModel> mPoint = new ArrayList<PointModel>();
    //---------------- step -------------------------------------
    @BindView(R.id.step1)
    SequenceStep step1;
    @BindView(R.id.step2)
    SequenceStep step2;
    @BindView(R.id.step3)
    SequenceStep step3;
    @BindView(R.id.step4)
    SequenceStep step4;
    //-----------------------------------------------------------

    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
    @BindView(R.id.verifycation)
    TextView verify;
    @BindView(R.id.namamerchant)
    TextView namamerchant;
    @BindView(R.id.orderid)
    TextView OrderID;
    @BindView(R.id.llchat)
    LinearLayout llchat;
    @BindView(R.id.background)
    CircleImageView foto;
    @BindView(R.id.pickUpText)
    TextView pickUpText;
    @BindView(R.id.destinationText)
    TextView destinationText;

    @BindView(R.id.navigation)
    Button Navigasi;

    @BindView(R.id.fitur)
    TextView StatusLayanan;
    @BindView(R.id.txtwaktu)
    TextView TxtWaktu;
    @BindView(R.id.idlayanan)
    TextView fiturtext;
    @BindView(R.id.distance)
    TextView distanceText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.deliveryfee)
    TextView deliveryfee;
    @BindView(R.id.phonenumber)
    ImageView phone;
    @BindView(R.id.ratingView)
    RatingView RatingView;
    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.phonemerchant)
    ImageView phonemerchant;
    @BindView(R.id.chatmerchant)
    ImageView chatmerchant;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.orderdetail)
    LinearLayout llorderdetail;
    @BindView(R.id.lldistance)
    LinearLayout lldistance;
    @BindView(R.id.senddetail)
    LinearLayout lldetailsend;
    @BindView(R.id.produk)
    TextView produk;
    @BindView(R.id.txtbiaya)
    TextView TxtBiaya;
    @BindView(R.id.sendername)
    TextView sendername;
    @BindView(R.id.receivername)
    TextView receivername;
    @BindView(R.id.senderphone)
    Button senderphone;
    @BindView(R.id.receiverphone)
    Button receiverphone;
    @BindView(R.id.shimmerlayanan)
    ShimmerFrameLayout shimmerlayanan;
    @BindView(R.id.shimmerpickup)
    ShimmerFrameLayout shimmerpickup;
    @BindView(R.id.shimmerdestination)
    ShimmerFrameLayout shimmerdestination;
    @BindView(R.id.shimmerfitur)
    ShimmerFrameLayout shimmerfitur;
    @BindView(R.id.shimmerdistance)
    ShimmerFrameLayout shimmerdistance;
    @BindView(R.id.shimmerprice)
    ShimmerFrameLayout shimmerprice;
    @BindView(R.id.order)
    Button submit;
    @BindView(R.id.merchantdetail)
    LinearLayout llmerchantdetail;
    @BindView(R.id.merchantinfo)
    LinearLayout llmerchantinfo;
    @BindView(R.id.llbutton)
    LinearLayout llbutton;
    @BindView(R.id.merchantnear)
    RecyclerView rvmerchantnear;
    @BindView(R.id.textrespon)
    TextView TextRespon;
    @BindView(R.id.TxtTotal)
    TextView TxtTotal;
    @BindView(R.id.TxtWallet)
    TextView TxtWallet;
    @BindView(R.id.btncancel)
    Button CancelOrder;
    @BindView(R.id.bar)
    ProgressBar pbar;
    @BindView(R.id.potongan)
    TextView Potongan;
    @BindView(R.id.txtSaldoUser)
    TextView TxtSaldoUser;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = Objects.requireNonNull(inflater).inflate(R.layout.activity_progress, container, false);
        context = getContext();
        ButterKnife.bind(this, getView);
      //  BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
       // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        totaltext = getView.findViewById(R.id.totaltext);
        StatusLayanan = getView.findViewById(R.id.idlayanan);
        fitur = "0";
        type = "0";
        Komisi = "0";
        OnStatus = "0";
        SaldoUser = "0";
        Estimasi = "1";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idpelanggan = bundle.getString("id_pelanggan");
            idtrans = bundle.getString("id_transaksi");
            response = bundle.getString("response");
            AsalLat = bundle.getDouble("AsalLat");
            AsalLng = bundle.getDouble("AsalLng");
            TujuanLat = bundle.getDouble("TujuanLat");
            TujuanLng = bundle.getDouble("TujuanLng");
            isWorking = bundle.getBoolean("IsWorking");
            pickUpLatLng = new LatLng(AsalLat, AsalLng);
            destinationLatLng = new LatLng(TujuanLat, TujuanLng);
            SaldoDriver(idpelanggan);

        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        createLocationRequest();
        markerCount = 0;
        getView.post(new Runnable() {
            @Override
            public void run() {
                Screen = getView.getMeasuredHeight();
            }
        });
        PointDriver();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JobDriver();
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.iconmobil);
        Bitmap b = bitmapdraw.getBitmap();
        BitMapMarker = Bitmap.createScaledBitmap(b, 110, 60, false);
        CekRute = "0";
        RatingView.setVisibility(View.GONE);
        //map
        String Apikeys = getString(R.string.google_maps_key);
        if (MainActivity.apikey == null) {
            Places.initialize(context, Apikeys);
            if (!Places.isInitialized()) {
                Places.initialize(context, Apikeys);
            }
        } else {
            Places.initialize(context, MainActivity.apikey);
            if (!Places.isInitialized()) {
                Places.initialize(context, MainActivity.apikey);
            }
        }
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        String DriverID = loginUser.getId();
        //end map
        switch (response) {
            case "2":
                onsubmit = "2";
                // OnNavigasi("2");
                llchat.setVisibility(View.VISIBLE);
                break;
            case "3":
                onsubmit = "3";
                //  OnNavigasi("3");
                llchat.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
                submit.setText("Selesai");
                break;
            case "4":
                // OnNavigasi("4");
                llchat.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_finish));
                break;
            case "5":
                llchat.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_cancel));
                break;
        }
        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rlprogress.setVisibility(View.GONE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));
        textprogress.setText(getString(R.string.waiting_pleaseWait));
        Warna = "#7309A8";
        String BGColor = Warna;
        int colorCodeDark = Color.parseColor(BGColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pbar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
            verify.setTextColor(Color.parseColor(Warna));
            submit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
        }
        // Set up a standard MapBox map

        getData(idtrans, idpelanggan);

        return getView;
    }

    private void getData(final String idtrans, final String idpelanggan) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdPelanggan(idpelanggan);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NonNull Call<DetailTransResponseJson> call, @NonNull Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    shimmertutup();
                    Log.e("", String.valueOf(Objects.requireNonNull(responsedata.body()).getData().get(0)));
                    final TransaksiModel transaksi = responsedata.body().getData().get(0);
                    final PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);
                    Constants.mPelanggan = pelanggan;
                    Constants.IDTRANS = idtrans;
                    Constants.mHome = transaksi.getHome();
                    Constants.mToken = transaksi.token_merchant;
                    Constants.mFitur = transaksi.getOrderFitur();
                    Constants.mWallet = transaksi.isPakaiWallet();
                    mHome = transaksi.getHome();
                    final String getFitur = transaksi.getOrderFitur();
                    iswallet = transaksi.isPakaiWallet();
                    StatusLayanan.setText(getFitur);
                    type = transaksi.getHome();
                    TxtTotal.setText(transaksi.getBiaya_akhir());
                    String Job = loginUser.getJob();
                    tokenPelanggan = pelanggan.getToken();
                    tokenmerchant = transaksi.getNama_merchant();
                    OnStatus = String.valueOf(transaksi.status);
                    Constants.mStatus = String.valueOf(transaksi.status);
                    CekKomisi(transaksi.getOrderFitur());
                    IsWallet = transaksi.isPakaiWallet();
                    TokenMerchant = transaksi.token_merchant;
                    String IDOrder = transaksi.getId();
                    OrderID.setText(IDOrder);
                    if (transaksi.isPakaiWallet()) {
                        totaltext.setText("Total (Wallet)");
                    } else {
                        totaltext.setText("Total (Cash)");
                        TxtWallet.setText("1");
                    }
                    pickUpText.setMovementMethod(new ScrollingMovementMethod());
                    pickUpText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String AsalA = String.valueOf(pickUpLatLng.latitude);
                            String AsalB = String.valueOf(pickUpLatLng.longitude);
                            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + AsalA + "," + AsalB);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                    destinationText.setMovementMethod(new ScrollingMovementMethod());
                    destinationText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String AsalA = String.valueOf(destinationLatLng.latitude);
                            String AsalB = String.valueOf(destinationLatLng.longitude);
                            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + AsalA + "," + AsalB);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });

                    if (onsubmit.equals("2")) {
                        Status = "2";
                        OnProgress(Status);
                        PerbaruiStatus(0, idtrans, idpelanggan, transaksi.getBiaya_akhir(), String.valueOf(IsWallet), fitur, "2");
                        CancelOrder.setVisibility(View.GONE);
                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("Pemesananan");
                            SendNotif("Pemesananan");
                        } else {
                            layanandesk.setText(getString(R.string.notification_start));
                            if (fitur.equals("17")) {
                                SendNotif("Ambil Paket / Dokumen");
                            } else if (fitur.equals("15") | fitur.equals("16")) {
                                SendNotif("Jemput Customer");
                            }
                        }
                        verify.setVisibility(View.GONE);
                        submit.setText("Antar");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (transaksi.getHome().equals("4")) {
                                    /*if (verify.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Please enter verify code!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                        String finalDate = timeFormat.format(transaksi.getWaktuOrder());
                                        rlprogress.setVisibility(View.VISIBLE);
                                        verify(verify.getText().toString(), pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);

                                    }*/
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                    String finalDate = timeFormat.format(transaksi.getWaktuOrder());
                                    rlprogress.setVisibility(View.VISIBLE);
                                  //  verify(verify.getText().toString(), pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);
                                    start(pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);
                                } else {
                                    start(pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, String.valueOf(transaksi.getWaktuOrder()));
                                }
                            }
                        });
                    } else if (onsubmit.equals("3")) {
                        Status = "3";
                        OnProgress(Status);
                        PerbaruiStatus(0, idtrans, idpelanggan, transaksi.getBiaya_akhir(), String.valueOf(IsWallet), fitur, "3");
                        CancelOrder.setVisibility(View.GONE);
                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("Pengantaran");
                            SendNotif("Antar Pesanan");
                        } else {
                            layanandesk.setText(getString(R.string.notification_start));
                            if (fitur.equals("17")) {
                                SendNotif("Antar Paket / Dokumen");
                            } else if (fitur.equals("15") | fitur.equals("16")) {
                                SendNotif("Antar Customer");
                            }
                        }
                        verify.setVisibility(View.GONE);
                        submit.setText("Selesai");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (transaksi.isPakaiWallet()) {
                                    android.util.Log.d("Metode", "With Wallet");
                                    UpdateSaldoUser(transaksi.getIdPelanggan());
                                    finish(pelanggan, transaksi.token_merchant);

                                } else {
                                    // UpdateSaldo(loginUser.getId());
                                    android.util.Log.d("Metode", "With Cash");
                                    finish(pelanggan, transaksi.token_merchant);
                                }

                            }
                        });
                    }
                    //--------------------------------------------------------- end status trx --------------------------------------
                    fitur = transaksi.getOrderFitur();
                    Utility.currencyTXT(Potongan, String.valueOf(transaksi.getKreditPromo()), context);
                    if (transaksi.getHome().equals("3")) {
                        lldestination.setVisibility(View.GONE);
                        lldistance.setVisibility(View.GONE);
                        fiturtext.setText(transaksi.getEstimasi());
                    } else if (transaksi.getHome().equals("4")) {

                        llorderdetail.setVisibility(View.VISIBLE);
                        llmerchantdetail.setVisibility(View.VISIBLE);
                        llmerchantinfo.setVisibility(View.VISIBLE);
                        Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), context);
                        Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), context);
                        CekHarga = transaksi.getTotal_biaya();
                        namamerchant.setText(transaksi.getNama_merchant());
                        itemPesananItem = new ItemPesananItem(responsedata.body().getItem(), R.layout.item_pesanan);
                        itemPesananItem.setLayanan(String.valueOf(transaksi.getTotal_biaya()));
                        itemPesananItem.setTransaksi(transaksi.getId());
                        itemPesananItem.setResponse(onsubmit);
                        rvmerchantnear.setAdapter(itemPesananItem);
                        if (itemPesananItem.getItemCount() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Daftar Menu Kosong Silahkan Batalkan Pesanan.!")
                                    .setCancelable(false)
                                    .setPositiveButton("OKE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            cancelOrder();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        phonemerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Customer");
                                alertDialogBuilder.setMessage("You want to call Merchant (+" + transaksi.getTeleponmerchant() + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+" + transaksi.getTeleponmerchant()));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        chatmerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("senderid", loginUser.getId());
                                intent.putExtra("receiverid", transaksi.getId_merchant());
                                intent.putExtra("tokendriver", loginUser.getToken());
                                intent.putExtra("tokenku", transaksi.getToken_merchant());
                                intent.putExtra("name", transaksi.getNama_merchant());
                                intent.putExtra("pic", Constants.IMAGESMERCHANT + transaksi.getFoto_merchant());
                                startActivity(intent);
                            }
                        });

                    } else if (fitur.equalsIgnoreCase("5")) {
                        lldetailsend.setVisibility(View.VISIBLE);
                        produk.setText(transaksi.getNamaBarang());
                        sendername.setText(transaksi.namaPengirim);
                        receivername.setText(transaksi.namaPenerima);

                        senderphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPengirim));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        receiverphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPenerima));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                    }

                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                    parsedata(transaksi, pelanggan);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<DetailTransResponseJson> call, @NonNull Throwable t) {
                TextRespon.setText("5");
            }
        });


    }

    //-------------------------------------------------------------------------------

    private void shimmertutup() {
        shimmerlayanan.stopShimmerAnimation();
        shimmerpickup.stopShimmerAnimation();
        shimmerdestination.stopShimmerAnimation();
        shimmerfitur.stopShimmerAnimation();
        shimmerdistance.stopShimmerAnimation();
        shimmerprice.stopShimmerAnimation();

        shimmerlayanan.setVisibility(View.GONE);
        shimmerpickup.setVisibility(View.GONE);
        shimmerdestination.setVisibility(View.GONE);
        shimmerfitur.setVisibility(View.GONE);
        shimmerdistance.setVisibility(View.GONE);
        shimmerprice.setVisibility(View.GONE);

        layanan.setVisibility(View.VISIBLE);
        layanandesk.setVisibility(View.VISIBLE);
        pickUpText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        distanceText.setVisibility(View.VISIBLE);
        fiturtext.setVisibility(View.VISIBLE);
        priceText.setVisibility(View.VISIBLE);
    }

    private void parsedata(TransaksiModel request, final PelangganModel pelanggan) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());
        Picasso.get()
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .resize(160, 200)
                .transform(new BackgroundColorTransform(ContextCompat.getColor(getContext(), R.color.white)))
                .placeholder(R.drawable.nocamera)
                .error(R.drawable.nocamera)
                .into(foto);

        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (type.equals("4")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            long StrPotongan = Long.parseLong(request.getKreditPromo());
            long HargaTotal = Long.parseLong(String.valueOf(request.getHarga()));
            long HasilPotongan = HargaTotal - StrPotongan;
            Utility.currencyTXT(priceText, String.valueOf(HasilPotongan + totalbiaya), context);
            TxtBiaya.setText(String.valueOf(HasilPotongan + totalbiaya));
            Constants.mBiaya = TxtBiaya.getText().toString();
            UpdateTransaksi(idtrans, String.valueOf(HasilPotongan + totalbiaya));
            mCustomer = pelanggan.getFullnama();
            lokasiA = destinationLatLng.latitude;
            lokasiB = destinationLatLng.longitude;
        } else {
            long StrPotongan = Long.parseLong(request.getKreditPromo());
            long HargaTotal = Long.parseLong(String.valueOf(request.getHarga()));
            long HasilPotongan = HargaTotal - StrPotongan;
            Utility.currencyTXT(priceText, String.valueOf(HasilPotongan), context);
            TxtBiaya.setText(String.valueOf(HasilPotongan));
            Constants.mBiaya = TxtBiaya.getText().toString();
            UpdateTransaksi(idtrans, String.valueOf(HasilPotongan));
            mCustomer = pelanggan.getFullnama();
            lokasiA = pickUpLatLng.latitude;
            lokasiB = pickUpLatLng.longitude;
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Customer");
                alertDialogBuilder.setMessage("You want to call Costumer (+" + pelanggan.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:+" + pelanggan.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", pelanggan.getId());
                intent.putExtra("tokendriver", loginUser.getToken());
                intent.putExtra("tokenku", pelanggan.getToken());
                intent.putExtra("name", pelanggan.getFullnama());
                intent.putExtra("pic", Constants.IMAGESUSER + pelanggan.getFoto());
                startActivity(intent);
            }
        });


    }
    private void start(final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.startrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        onsubmit = "3";
                        getData(idtrans, idpelanggan);
                        //     UpdateLokasiHosting(mLastLocation, "3");
                        TextRespon.setText("3");
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "3";
                        String Biaya = TxtBiaya.getText().toString();
                        String Wallets = String.valueOf(iswallet);
                        PerbaruiStatus(0, idtrans, idpelanggan, Biaya, Wallets, fitur, "3");
                        if (type.equals("4")) {
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-" + idtrans + idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            orderfcm.desc = "driver delivers the order";
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        //  UpdateLokasiHosting(mLastLocation, "1");
                        TextRespon.setText("1");
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("Tema", Warna);
                        i.putExtra("IsWorking", false);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void verify(String verificode, final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        VerifyRequestJson param = new VerifyRequestJson();
        param.setId(loginUser.getNoTelepon());
        param.setIdtrans(idtrans);
        param.setVerifycode(verificode);
        userService.verifycode(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull final Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        start(pelanggan, tokenmerchant, idtransmerchant, waktuorder);

                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Toast.makeText(context, "verifycode not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void finish(final PelangganModel pelanggan, final String tokenmerchant) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.finishrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        //UpdateLokasiHosting(mLastLocation, "4");

                        //private void PerbaruiStatus(String idtrans,String idpel,String biaya,String wallet,String fitur,String respon)
                        String Biaya = TxtBiaya.getText().toString();
                        String Wallets = String.valueOf(iswallet);
                        PerbaruiStatus(1, idtrans, idpelanggan, Biaya, Wallets, fitur, "4");
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("IsWorking", false);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "4";
                        orderfcm.desc = getString(R.string.notification_finish);

                        TextRespon.setText("4");
                        if (type.equals("4")) {
                            sendMessageToDriver(tokenmerchant, orderfcm);
                            sendMessageToDriver(pelanggan.getToken(), orderfcm);
                        } else {
                            sendMessageToDriver(pelanggan.getToken(), orderfcm);
                        }
                        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses("com.google.android.apps.maps");
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
                        mDatabase.child(loginUser.getId()).removeValue();
                        SendNotif("Pesanan Selesai");
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        //  UpdateLokasiHosting(mLastLocation, "1");
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("Tema", Warna);
                        i.putExtra("IsWorking", false);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                        TextRespon.setText("1");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    //Update Saldo Driver
    private void UpdateSaldo(String Id) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        String iddriver = Id;
        String Saldo = String.valueOf(loginUser.getWalletSaldo());
        int angka1;
        int angka2;
        int hasil;
        int mkomisi = Integer.parseInt(Komisi);
        angka1 = Integer.parseInt(Saldo);
        angka2 = Integer.parseInt(TxtTotal.getText().toString());
        hasil = angka1 - angka2;
        int Diskon = hasil * mkomisi / 100;
        int Total = hasil - Diskon;
        String Tukarkan = String.valueOf(Total);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<PointRespon> update = api.updateSaldo(iddriver, Tukarkan);
        update.enqueue(new Callback<PointRespon>() {
            @Override
            public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                android.util.Log.d("Retro", "Response");
            }

            @Override
            public void onFailure(Call<PointRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");
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
                android.util.Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    //hapus database
    private void HapusDB() {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        if (loginUser != null) {
            String IdDriver = loginUser.getId();
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Driver").child(IdDriver);
            dR.removeValue();
            dR.removeValue();
        }

    }

    //------------------------ Cancel Order -------------------------------------------------
    private void cancelOrder() {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        CancelBookRequestJson requestcancel = new CancelBookRequestJson();
        requestcancel.id = loginUser.getId();
        requestcancel.id_transaksi = idtrans;

        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        service.cancelOrder(requestcancel).enqueue(new Callback<CancelBookResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<CancelBookResponseJson> call, @NonNull Response<CancelBookResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).mesage.equals("canceled")) {
                        String Biaya = TxtBiaya.getText().toString();
                        String Wallets = String.valueOf(iswallet);
                        PerbaruiStatus(0, idtrans, idpelanggan, Biaya, Wallets, fitur, "1");
                        rlprogress.setVisibility(View.GONE);
                        fcmcancel();
                        fcmcancelmerchant();
                        //SendNotif("Orderan Telah Dibatalkan!");
                        SendNotif("Pesanan Dibatalkan");
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("Tema", Warna);
                        i.putExtra("IsWorking", false);
                        startActivity(i);
                    } else {
                        SendNotif("Gagal Membatalkan Pesanan");
                        //SendNotif("Orderan Gagal Dibatalkan!");
                        rlprogress.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelBookResponseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void fcmcancel() {
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(DriverResponse.REJECT);

        FCMMessage message = new FCMMessage();
        message.setTo(tokenPelanggan);
        message.setData(response);

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

    private void fcmcancelmerchant() {
        DriverResponse response = new DriverResponse();
        response.type = ORDER;
        response.setIdTransaksi(idtrans);
        response.setResponse(String.valueOf(Constants.CANCEL));

        FCMMessage message = new FCMMessage();
        message.setTo(tokenmerchant);
        message.setData(response);


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

    //notifikasi
    private void SendNotif(String Pesan) {
        //Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.notifikasi);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notifikasi);
            mChannel = new NotificationChannel("notify_002", "Point", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(Pesan);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(sound, audioAttributes);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_002")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                .setTicker(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Pesan))
                .setContentText(Pesan)
                .setAutoCancel(true)
                .setLights(0xff0000ff, 300, 1000) // blue color
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mBuilder.setSound(null);
        }

        int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void intentCancel() {
        Intent toMain = new Intent(context, MainActivity.class);
        toMain.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
    }


    //------------------------------------- New Update Lokasi Background------------------------------------------
    private void UpdateLokasiBG(double Lat,double Lng,float bearing, String Status) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateLocationRequestJson param = new UpdateLocationRequestJson();
        param.setId(loginUser.getId());
        param.setLatitude(String.valueOf(Lat));
        param.setLongitude(String.valueOf(Lng));
        param.setStatus(OnStatus);
        param.setRespon(Status);
        param.setBearing(String.valueOf(bearing));
        service.updatelocation(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    android.util.Log.e("location", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });

    }
    //-----------------------------------------------------------------------------------------------
    private void UpdateLokasiHosting(Location location, String Status) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateLocationRequestJson param = new UpdateLocationRequestJson();
        param.setId(loginUser.getId());
        param.setLatitude(String.valueOf(location.getLatitude()));
        param.setLongitude(String.valueOf(location.getLongitude()));
        param.setStatus(OnStatus);
        param.setRespon(Status);
        param.setBearing(String.valueOf(location.getBearing()));
        service.updatelocation(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    android.util.Log.e("location", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

    private void UpdateLokasi(Location location, String Status) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        String IdDriver = loginUser.getId();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (isWorking = true) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("MDriver").child(IdDriver);
            if (mDatabase == null) {
                DataDriver user = new DataDriver(IdDriver, location.getLatitude(), location.getLongitude(), location.getBearing(), Status);
                mDatabase.setValue(user);
            } else {
                DataDriver user = new DataDriver(IdDriver, location.getLatitude(), location.getLongitude(), location.getBearing(), Status);
                mDatabase.setValue(user);
            }

        }

    }

    private void CekKomisi(String idFitur) {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<KomisiResponse> getdata = api.cekKomisi(idFitur);
        getdata.enqueue(new Callback<KomisiResponse>() {
            @Override
            public void onResponse(Call<KomisiResponse> call, Response<KomisiResponse> response) {
                android.util.Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mFitur = response.body().getResult();
                String CKomisi = mFitur.get(0).getKomisi();
                Komisi = CKomisi;
            }

            @Override
            public void onFailure(Call<KomisiResponse> call, Throwable t) {
                android.util.Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }

    //----------------------------------------- Update Harga ------------------------------------
    private void UpdateTransaksi(String idtrans, String Biaya) {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<TransaksiRespon> update = api.updateBiaya(idtrans, Biaya);
        update.enqueue(new Callback<TransaksiRespon>() {
            @Override
            public void onResponse(Call<TransaksiRespon> call, Response<TransaksiRespon> response) {
                android.util.Log.d("Retro", "Response");

            }

            @Override
            public void onFailure(Call<TransaksiRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }

    //----------------------------------- Saldo Customers -----------------------------------------
    //Update Saldo Customers
    private void UpdateSaldoUser(String Id) {
        String iddriver = Id;
        String Saldo = TxtSaldoUser.getText().toString();
        int angka1;
        int angka2;
        int hasil;
        int mkomisi = Integer.parseInt(Komisi);
        angka1 = Integer.parseInt(Saldo);
        angka2 = Integer.parseInt(TxtTotal.getText().toString());
        hasil = angka1 - angka2;
        int Diskon = hasil * mkomisi / 100;
        int Total = hasil - Diskon;
        String Tukarkan = String.valueOf(Total);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<PointRespon> update = api.updateSaldo(iddriver, Tukarkan);
        update.enqueue(new Callback<PointRespon>() {
            @Override
            public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                rlprogress.setVisibility(View.VISIBLE);
                android.util.Log.d("Retro", "Response");
            }

            @Override
            public void onFailure(Call<PointRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }

    private void SaldoDriver(String IdUser) {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<SaldoResponse> getdata = api.cekSaldo(IdUser);
        getdata.enqueue(new Callback<SaldoResponse>() {
            @Override
            public void onResponse(Call<SaldoResponse> call, Response<SaldoResponse> response) {
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mSaldo = response.body().getResult();
                String CSaldo = mSaldo.get(0).getSaldo();
                SaldoUser = CSaldo;
                TxtSaldoUser.setText(CSaldo);
            }

            @Override
            public void onFailure(Call<SaldoResponse> call, Throwable t) {
                Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void startService() {
        Intent serviceIntent = new Intent(context, MWService.class);
        serviceIntent.putExtra("inputExtra", "input");
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(context, MWService.class);
        context.stopService(serviceIntent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onResume() {
        startService();
        super.onResume();

     //   KillFloatingWidget();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        if (Status == null) {
            Status = "1";
        }
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
        markerCount = 0;
     //   stopLocationUpdates();
    }

    private void CekKM(LatLng driver, LatLng tujuan) {
        Location locationA = new Location("Point A");
        locationA.setLatitude(driver.latitude);
        locationA.setLongitude(driver.longitude);
        Location locationB = new Location("Point B");
        locationB.setLatitude(tujuan.latitude);
        locationB.setLongitude(tujuan.longitude);
        double distance = locationA.distanceTo(locationB) / 1000;   // in km
        float distan = locationA.distanceTo(locationB) / 1000;
        LatLng From = new LatLng(driver.latitude, driver.longitude);
        LatLng To = new LatLng(tujuan.latitude, tujuan.longitude);
        int speedIs1KmMinute = 100;
        float estimatedDriveTimeInMinutes = distan / speedIs1KmMinute;
        int speed = 10; // [km/h]
        float time = (float) distan / (float) speed;
        int hours = (int) time;
        int minutes = (int) (60 * (time - hours));
        mJarak = String.valueOf(distance);
        mEstimasi = String.valueOf(minutes);

    }

    //--------------------------------------------------------------------------------------------------
    private void PointDriver() {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
                Call<PointRespon> getdata = api.cekPoint(loginUser.getId());
                getdata.enqueue(new Callback<PointRespon>() {
                    @Override
                    public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                        Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                        mPoint = response.body().getResult();
                        if (mPoint.size() != 0) {
                            String CPoint = mPoint.get(0).getPoint();
                            InitPoint = CPoint;
                            Constants.mPoint = InitPoint;
                            //Toast.makeText(ProgressActivity.this, CPoint, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<PointRespon> call, Throwable t) {
                        Log.d("RETRO", "FAILED : respon gagal");

                    }
                });
            }
        });

    }

    private void PerbaruiStatus(int rates, String idtrans, String idpel, String biaya, String wallet, String fitur, String respon) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateStatusRequest param = new UpdateStatusRequest();
        param.setId(idpel);
        param.setId_transaksi(idtrans);
        param.setId_driver(loginUser.getId());
        param.setTotal_biaya(biaya);
        param.setPakai_wallet(wallet);
        param.setFitur(fitur);
        param.setNama_driver(loginUser.getFullnama());
        param.setFoto_driver(loginUser.getFotodriver());
        param.setResponse(respon);
        param.setPoint_driver(InitPoint);
        param.setIsrate(rates);
        service.updateStatus(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    Log.e("UPDATE STATUS ", response.message());
                    //   Toast.makeText(ProgressActivity.this, "Update Status", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

    private void CancelStatus(int rates, String idtrans, String idpel, String biaya, String wallet, String fitur, String respon) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateStatusRequest param = new UpdateStatusRequest();
        param.setId(idpel);
        param.setId_transaksi(idtrans);
        param.setId_driver(loginUser.getId());
        param.setTotal_biaya(biaya);
        param.setPakai_wallet(wallet);
        param.setFitur(fitur);
        param.setNama_driver(loginUser.getFullnama());
        param.setFoto_driver(loginUser.getFotodriver());
        param.setResponse(respon);
        param.setPoint_driver(InitPoint);
        param.setIsrate(rates);
        service.updateStatus(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    Log.e("UPDATE STATUS ", response.message());
                    //   Toast.makeText(ProgressActivity.this, "Update Status", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void JobDriver() {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<JobResponse> getdata = api.cekJob(loginUser.getJob());
        getdata.enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mJob = response.body().getResult();
                int cJob = Integer.parseInt(mJob.get(0).getIcon());
                ArrayList<String> list = new ArrayList<String>();
                list.add("default");
                list.add("motor");
                list.add("icmobil");
                list.add("truck");
                list.add("deliverybike");
                list.add("hatchback");
                list.add("suv");
                list.add("van");
                list.add("bicycle");
                list.add("tuktuk");
                for (int i = 0; i < list.size(); i++) {
                    LinkIkon = BASE_JOB + list.get(cJob) + ".png";
                    Log.d("Ikon Job", list.get(cJob));
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }

    private void kirimnotif(final String regIDTujuan, final Notif notif) {

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

    //------------------------------------ Kebutuhan map ---------------------------------------------------------
    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " +
                mGoogleApiClient.isConnected());
        startLocationUpdates();
        displayLocation();


    }
    private void OnProgress(String Status){
        step1.setTitle("Pesanan Diterima");
        step1.setAnchor("Diterima");
        step1.setSubtitle("Proses Pesanan Customer Kamu Dengan Baik Yah.");
        step1.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

        step2.setTitle("Pesanan Diproses");
        step2.setAnchor("Proses");
        if(Constants.mHome != null){
            if(Constants.mHome.equals("4")){
                step2.setSubtitle("Memesan Pesanan Ke Mitra.");
            }else{
                step2.setSubtitle("Pergi Ke Lokasi Penjemputan.");
            }
        }
        step2.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

        step3.setTitle("Antar Pesanan");
        step3.setAnchor("Antar");
        step3.setSubtitle("Pergi Ke Lokasi Pengantaran.");
        step3.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

        step4.setTitle("Selesai");
        step4.setAnchor("Selesai");
        step4.setSubtitle("Pesanan Kamu Sudah Selesai.");
        step4.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

        switch(Status){
            case "2":
                step1.setActive(false);
                step2.setActive(true);
                step3.setActive(false);
                step4.setActive(false);
                if(Constants.mHome.equals("4")){
                    AsalA = String.valueOf(destinationLatLng.latitude);
                    AsalB = String.valueOf(destinationLatLng.longitude);
                }else{
                    AsalA = String.valueOf(pickUpLatLng.latitude);
                    AsalB = String.valueOf(pickUpLatLng.longitude);
                }
                break;
            case "3":
                step1.setActive(false);
                step2.setActive(false);
                step3.setActive(true);
                step4.setActive(false);
                if(Constants.mHome.equals("4")){
                    AsalA = String.valueOf(pickUpLatLng.latitude);
                    AsalB = String.valueOf(pickUpLatLng.longitude);
                }else{
                    AsalA = String.valueOf(destinationLatLng.latitude);
                    AsalB = String.valueOf(destinationLatLng.longitude);
                }
                break;
            case "4":
                step1.setActive(false);
                step2.setActive(false);
                step3.setActive(false);
                step4.setActive(true);
                break;
            default:
                Log.e("", "no case");
                return;
        }
        Navigasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + AsalA + "," + AsalB+"&mode=d&entry=fnls");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onConnectionSuspended(@NonNull int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
        if(mLastLocation != null){
            driverlatlng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            if(mLastLocation != null){
                UpdateLokasiHosting(mLastLocation, response);
            }

            if(Constants.mHome != null){

              if(Constants.mHome.equals("4")){
                switch(response) {
                case "2":
                    CekKM(driverlatlng,destinationLatLng);
                    tujuanLatLng = destinationLatLng;
                    break;
                case "3":
                    CekKM(driverlatlng,pickUpLatLng);
                    tujuanLatLng = pickUpLatLng;
                    break;
                default:
                    CekKM(pickUpLatLng,destinationLatLng);
                    tujuanLatLng = destinationLatLng;
                    break;
                }
            }else{
                switch(response) {
                case "2":
                    CekKM(driverlatlng,pickUpLatLng);
                    tujuanLatLng = pickUpLatLng;
                    break;
                case "3":
                    CekKM(driverlatlng,destinationLatLng);
                    tujuanLatLng = destinationLatLng;
                    break;
                default:
                    CekKM(pickUpLatLng,destinationLatLng);
                    tujuanLatLng = destinationLatLng;
                    break;
                }
              }
            }
            //--------------------------------- Rute driver ---------------------------------------
            if(driverlatlng != null && tujuanLatLng !=null){
           /*String fromFKIP = String.valueOf(driverlatlng.latitude) + "," + String.valueOf(driverlatlng.longitude);
            String toMonas = String.valueOf(tujuanLatLng.latitude) + "," + String.valueOf(tujuanLatLng.longitude);
            ApiServices apiServices = RetrofitClient.apiServices(context);
            apiServices.getDirection(fromFKIP, toMonas, MainActivity.apikey)
                    .enqueue(new Callback<DirectionResponses>() {
                        @Override
                        public void onResponse(@NonNull Call<DirectionResponses> call, @NonNull Response<DirectionResponses> response) {
                            if(response != null){
                                drawdriver(response);
                                Log.d("bisa dong oke", response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DirectionResponses> call, @NonNull Throwable t) {
                            Log.e("anjir error", t.getLocalizedMessage());
                        }
                    });*/
            }
            if(mLastLocation != null){
                    if(Constants.isBackground){
                        //log create
                    }else{
                        handler = new Handler();
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                    Address address = null;
                                    List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(),1);
                                    for(int index=0; index<addresses.size(); ++index)
                                    {
                                        address = addresses.get(index);
                                        String desa = address.getSubLocality();
                                        String kecamatan = address.getLocality();

                                    }
                                }
                                catch(Exception e){

                                }
                                handler.postDelayed(this, 1000);
                            }
                        };
                        handler.postDelayed(runnable, 1000);
                    }

            }


            //----------------------------- Save Firebase --------------------------------------------------------------------
            final User loginUser = BaseApp.getInstance(context).getLoginUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Driver");
            String userId = mDatabase.push().getKey();
            FcmDriver user = new FcmDriver(loginUser.getId(), response, mLastLocation.getLatitude(), mLastLocation.getLongitude(), mLastLocation.getBearing(), mEstimasi, mJarak);
            mDatabase.child(loginUser.getId()).setValue(user);
            distanceText.setText(mJarak);
            TxtWaktu.setText(mEstimasi + " Menit");
            UpdateLokasiHosting(mLastLocation, response);

        }
    }
    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------ marker --------------------------------------------
    public void drivermarker(GoogleMap googleMap, double lat, double lon) {

        try {

            if (markerCount == 1) {
                if (oldLocation != null) {
                    LatLng mylokasi = new LatLng(lat,lon);
                    new MaswendMarker(googleMap,1000, new UpdateLocationCallBack() {
                        @Override
                        public void onUpdatedLocation(Location updatedLocation) {
                            oldLocation = updatedLocation;
                        }
                    }).animateMarker(mLastLocation,mylokasi, oldLocation.getBearing(), marker);
                } else {
                    oldLocation = mLastLocation;
                }
            } else if (markerCount == 0) {
                if (marker != null) {
                    marker.remove();
                }
                mMap = googleMap;

                LatLng latLng = new LatLng(lat, lon);
                int height = 150;
                int width = 150;
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, true);
                BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                        .icon(smallMarkerIcon));
                mMap.setPadding(0, 750, 0, 0);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f));
                markerCount = 1;

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayLocation() {
        try {

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // Check Permissions Now
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {


                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null && mLastLocation.getLongitude() != 0.0 && mLastLocation.getLongitude() != 0.0) {

                    if (mMap != null) {
                      //  addMarker(mMap, mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------- Rute ---------------------------------------------------
    private void drawPolyline(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {
            String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
            int height = 50;
            int width = 50;
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.cap);
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, true);
            BitmapDescriptor smallcap = BitmapDescriptorFactory.fromBitmap(smallMarker);
            PolylineOptions po = new PolylineOptions()
                    .addAll(PolyUtil.decode(shape));
            po.color(ContextCompat.getColor(context,R.color.BlueDirection)).width(16);
            Polyline mpolyline = mMap.addPolyline(po);
            po.color(ContextCompat.getColor(context,R.color.BlueinDirection)).width(10);
            po.startCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.cap), 50));
            po.endCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.cap), 50));
            mpolyline = mMap.addPolyline(po);
            List<LatLng> mPoints=mpolyline.getPoints();
            if(mPoints.size() > 0){
                for (int i = 0; i < mPoints.size(); i++)
                {
                    //here
                }
            }
        }
    }
    //-------------------------------- In rute driver ----------------------------------------------
    private void drawdriver(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {
            if(response.body().getRoutes().size() > 0){
                String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                PolylineOptions po = new PolylineOptions()
                        .addAll(PolyUtil.decode(shape));
                List<LatLng> mPoints=po.getPoints();
                if(mPoints.size() > 0){
                    for (int i = 0; i < mPoints.size(); i++)
                    {
                        if (mMap != null) {
                         //   drivermarker(mMap, mPoints.get(0).latitude, mPoints.get(0).longitude);
                        }
                    }
                }
            }

        }
    }
    //----------------------------------------------------------------------------------------------
    private interface ApiServices {
        @GET("maps/api/directions/json")
        Call<DirectionResponses> getDirection(@Query("origin") String origin,
                                              @Query("destination") String destination,
                                              @Query("key") String apiKey);
    }

    private static class RetrofitClient {
        static ApiServices apiServices(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getResources().getString(R.string.base_url))
                    .build();

            return retrofit.create(ApiServices.class);
        }
    }

}