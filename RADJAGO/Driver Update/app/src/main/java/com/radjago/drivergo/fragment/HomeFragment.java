package com.radjago.drivergo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.radjago.drivergo.R;
import com.radjago.drivergo.activity.ActivityClosed;
import com.radjago.drivergo.activity.ActivityPoint;
import com.radjago.drivergo.activity.ActivityScan;
import com.radjago.drivergo.activity.IntroActivity;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.item.BanklistItem;
import com.radjago.drivergo.json.AreaRequestJson;
import com.radjago.drivergo.json.AreaResponseJson;
import com.radjago.drivergo.json.GetHomeRequestJson;
import com.radjago.drivergo.json.GetHomeResponseJson;
import com.radjago.drivergo.json.GetOnRequestJson;
import com.radjago.drivergo.json.PointRespon;
import com.radjago.drivergo.json.ResponseJson;
import com.radjago.drivergo.json.UpdateLocationRequestJson;
import com.radjago.drivergo.json.UpdateTokenRequestJson;
import com.radjago.drivergo.models.AreaModel;
import com.radjago.drivergo.models.AreaModels;
import com.radjago.drivergo.models.BankModels;
import com.radjago.drivergo.models.PointModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.CircleTransform;
import com.radjago.drivergo.utils.NetworkManager;
import com.radjago.drivergo.utils.SettingPreference;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.radjago.drivergo.activity.MainActivity.isWorking;
import static com.radjago.drivergo.constants.Constants.IMAGESDRIVER;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Home";
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    //end maswend sdk
    private final static int LOCATION_REQUEST_CODE = 23;
    public static Location mLastLocation;
    public static String Warna;
    public static String Email;
    public static String Password;
    public static String strArea;
    public static Double Latitude;
    public static Double Longitude;
    public static Float Bearing;
    private final int REQUEST_CODE_PERMISSION_MULTIPLE = 123;
    Timer timer = new Timer();
    LatLng driverLatLng;
    List<AreaModel> joblist;
    BottomSheetBehavior sheetBehavior;
    private Handler handler;
    private GoogleApiClient googleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;
    private TextView saldo, namadriver, tArea, TxtPoint;
    private RelativeLayout rlprogress;
    private Button uangbelanja;
    // private Toolbar toolbar;
    private LabeledSwitch onoff, autobid;
    private SettingPreference sp;
    private ArrayList<BankModels> mList;
    private String statusdriver, saldodriver, AreaSaya;
    private ImageView fotoprofile;
    private Button SetBelanja, ViewPoint, qrdriver, btnscan;
    //   ImageView ikonjob;
    private RatingBar DriverRate;
    //map
    //maswend sdk
    private GoogleMap googleMapHomeFrag;
    private double[] latLng = new double[2];
    private boolean isDeninedRTPs = true;       // initially true to prevent anim(2)
    private boolean showRationaleRTPs = false;
    private float start_rotation;
    private Picasso picassomark;
    //firebase
    private DatabaseReference mDatabase;
    //end firebase
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private LinearLayout layoutBottomSheet;
    private LatLng defaultLocation;
    private Marker originMarker = null;
    private Marker destinationMarker = null;
    private Polyline grayPolyline =null;
    private Polyline blackPolyline= null;
    private Marker movingCabMarker= null;
    private LatLng previousLatLng = null;
    private LatLng currentLatLng= null;
  //  private Handler handler  = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    //------------------- Get Area ----------------------------------------------
    private Runnable InfoArea = new Runnable() {
        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Locale localeID = new Locale("in", "ID");
                        Geocoder gcd = new Geocoder(context, localeID);
                        List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                                mLastLocation.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            String countryName = addresses.get(0).getSubAdminArea();
                            String rCity = countryName.replaceAll("Kabupatén|Kabupaten| ", "");
                            AreaRequestJson param = new AreaRequestJson();
                            final DriverService service = ServiceGenerator.createService(DriverService.class, "admin", "12345");
                            param.setId(rCity);
                            service.listarea(param).enqueue(new Callback<AreaResponseJson>() {
                                @Override
                                public void onResponse(@NonNull Call<AreaResponseJson> call, @NonNull Response<AreaResponseJson> response) {
                                    if (response.isSuccessful()) {
                                        if (Objects.requireNonNull(response.body()).getData().size() > 0) {
                                            final AreaModels dataArea = Objects.requireNonNull(response.body()).getData().get(0);
                                            if (dataArea != null && response.body().getData().size() > 0) {
                                                if (dataArea.getStatus().equals("1")) {
                                                    android.util.Log.e("Cek Area", "Terdaftar");
                                                } else {
                                                    if (getActivity() == null || !isAdded()) return;
                                                    Intent i = new Intent(context, ActivityClosed.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    i.putExtra("Tema", Warna);
                                                    i.putExtra("IsWorking", false);
                                                    startActivity(i);

                                                }

                                            }
                                        } else {
                                            if (getActivity() == null || !isAdded()) return;
                                            Intent i = new Intent(context, ActivityClosed.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("Tema", Warna);
                                            i.putExtra("IsWorking", false);
                                            startActivity(i);
                                            handler.removeCallbacks(InfoArea);
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<AreaResponseJson> call, @NonNull Throwable t) {


                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (NetworkManager.isConnectToInternet(context)) {
                                try {
                                    Locale localeID = new Locale("in", "ID");
                                    Geocoder gcd = new Geocoder(context, localeID);
                                    List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                                            mLastLocation.getLongitude(), 1);
                                    if (addresses.size() > 0) {
                                        String countryName = addresses.get(0).getSubAdminArea();
                                        String rCity = countryName.replaceAll("Kabupatén|Kabupaten| ", "");
                                        AreaRequestJson param = new AreaRequestJson();
                                        final DriverService service = ServiceGenerator.createService(DriverService.class, "admin", "12345");
                                        param.setId(rCity);
                                        service.listarea(param).enqueue(new Callback<AreaResponseJson>() {
                                            @Override
                                            public void onResponse(@NonNull Call<AreaResponseJson> call, @NonNull Response<AreaResponseJson> response) {
                                                if (response.isSuccessful()) {
                                                    if (Objects.requireNonNull(response.body()).getData().size() > 0) {
                                                        final AreaModels dataArea = Objects.requireNonNull(response.body()).getData().get(0);
                                                        if (dataArea != null && response.body().getData().size() > 0) {
                                                            if (dataArea.getStatus().equals("1")) {
                                                                android.util.Log.e("Cek Area", "Terdaftar");
                                                            } else {
                                                                if (getActivity() == null || !isAdded())
                                                                    return;
                                                                Intent i = new Intent(context, ActivityClosed.class);
                                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                i.putExtra("Tema", Warna);
                                                                i.putExtra("IsWorking", false);
                                                                startActivity(i);

                                                            }

                                                        }
                                                    } else {
                                                        if (getActivity() == null || !isAdded())
                                                            return;
                                                        Intent i = new Intent(context, ActivityClosed.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        i.putExtra("Tema", Warna);
                                                        i.putExtra("IsWorking", false);
                                                        startActivity(i);
                                                        handler.removeCallbacks(InfoArea);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<AreaResponseJson> call, @NonNull Throwable t) {


                                            }
                                        });
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 0, 3000);
                }
            }).start();
        }
    };
    private List<PointModel> mPoint = new ArrayList<PointModel>();

    private static List<BankModels> getPeopleData(Context ctx) {
        List<BankModels> items = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.list_maximum);
        String[] name_arr = ctx.getResources().getStringArray(R.array.list_maximum);

        for (int i = 0; i < drw_arr.length(); i++) {
            BankModels obj = new BankModels();
            obj.setText(name_arr[i]);
            items.add(obj);
        }
        return items;
    }

    public static String getMyArea() {
        return strArea;
    }

    @SuppressLint({"SetTextI18n", "MissingPermission"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        RelativeLayout detail = getView.findViewById(R.id.detail);
        layoutBottomSheet = getView.findViewById(R.id.bottom_sheet);
        tArea = getView.findViewById(R.id.tArea);
        qrdriver = getView.findViewById(R.id.myqrcode);
        //   toolbar = getView.findViewById(R.id.toolbar);
        btnscan = getView.findViewById(R.id.btnscan);
        TxtPoint = getView.findViewById(R.id.TxtPoint);
        ViewPoint = getView.findViewById(R.id.viewPoint);
        saldo = getView.findViewById(R.id.saldo);
        autobid = getView.findViewById(R.id.autobid);
        uangbelanja = getView.findViewById(R.id.maks);
        SetBelanja = getView.findViewById(R.id.setbelanja);
        onoff = getView.findViewById(R.id.onoff);
        namadriver = getView.findViewById(R.id.namadriver);
        fotoprofile = getView.findViewById(R.id.fotoprofile);
        //  ikonjob = getView.findViewById(R.id.ikonjob);
        DriverRate = getView.findViewById(R.id.driverrate);
        mList = new ArrayList<>();
        sp = new SettingPreference(context);
        rlprogress = getView.findViewById(R.id.rlprogress);
        //maswend sdk
        strArea = "offline";
        checkAndRequestRunTimePermissions();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
      //  createFloatingWidget();
        //maswend sdk

        PointDriver();
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //  btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        layoutBottomSheet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        ViewPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityPoint.class);
                startActivity(i);

            }
        });
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityScan.class);
                startActivity(i);

            }
        });
        sp.updateNotif("OFF");
        SetBelanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();

            }
        });
        qrdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverqr popUpClass = new driverqr();

                popUpClass.showPopupWindow(v);
            }
        });
        //tema


        if (sp.getSetting()[0].equals("OFF")) {
            autobid.setOn(false);
        } else {
            autobid.setOn(true);
        }
        //rlprogress.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            statusdriver = bundle.getString("status");
            saldodriver = bundle.getString("saldo");
            AreaSaya = bundle.getString("AreaKu");

        }

        if (statusdriver.equals("1")) {
            rlprogress.setVisibility(View.GONE);
            sp.updateKerja("ON");
            onoff.setOn(true);
        } else if (statusdriver.equals("4")) {
            rlprogress.setVisibility(View.GONE);
            onoff.setOn(false);
            sp.updateKerja("OFF");
        }
        onoff.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                getturnon();
            }
        });
        if (sp.getSetting()[1].isEmpty()) {
            Utility.currencyTXT(uangbelanja, "1000", context);
        } else if (sp.getSetting()[1].equals("Unlimited")) {
            uangbelanja.setText(sp.getSetting()[1]);
        } else {
            Utility.currencyTXT(uangbelanja, sp.getSetting()[1], context);
        }
        Warna = "#209405";
        String BGColor = Warna;
        int colorCodeDark = Color.parseColor(BGColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // toolbar.setBackgroundColor(Color.parseColor(Warna));
            // ViewPoint.setColorFilter(Color.parseColor(Warna));
            // qrdriver.setColorFilter(Color.parseColor(Warna));
            //  ikonjob.setImageTintList(ColorStateList.valueOf(Color.parseColor(Warna)));

            //     onoff.setColorOn(Color.parseColor(Warna));
        }
        List<BankModels> items = getPeopleData(context);
        mList.addAll(items);
        return getView;
    }

    private void GetProfil() {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        String FotoUrl = IMAGESDRIVER + loginUser.getFotodriver();
        String Nama = loginUser.getFullnama();
        String Job = loginUser.getJob();
        float Rating = Float.parseFloat(loginUser.getRating());
        DriverRate.setRating(Rating);
        ;
        namadriver.setText(Nama);
        String IdDriver = loginUser.getMapkey();
        /*if (Job.equals("7") | Job.equals("13") | Job.equals("14")) {
            ikonjob.setImageDrawable(getResources().getDrawable(R.drawable.motor));
        } else {
            ikonjob.setImageDrawable(getResources().getDrawable(R.drawable.mobil));
        }*/
        Picasso.get()
                .load(Constants.IMAGESDRIVER + loginUser.getFotodriver())
                .resize(160, 200)
                .transform(new CircleTransform())
                .placeholder(R.drawable.nocamera)
                .error(R.drawable.nocamera)
                .into(fotoprofile);
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
        // StartInfoArea();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
      //    StopInfoArea();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //    StopInfoArea();
     //   startFloatingWidgetService();
    }

    @Override
    public void onPause() {
        super.onPause();
       //   StopInfoArea();
    }

    @Override
    public void onResume() {
        super.onResume();
        //startFloatingWidgetService();
        GetProfil();
        autobid.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (sp.getSetting()[0].equals("OFF")) {
                    sp.updateAutoBid("ON");
                    autobid.setOn(true);
                    Toast.makeText(context, "Auto Bid ON", Toast.LENGTH_SHORT).show();
                } else {
                    sp.updateAutoBid("OFF");
                    autobid.setOn(false);
                    Toast.makeText(context, "Auto Bid OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                  UpdateToken(token);
            }
        });
       //    StartInfoArea();

        Utility.currencyTXT(saldo, saldodriver, context);
    }

    private void getturnon() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetOnRequestJson param = new GetOnRequestJson();
        param.setId(loginUser.getId());
        if (statusdriver.equals("1")) {
            param.setOn(false);
        } else {
            param.setOn(true);
        }

        userService.turnon(param).enqueue(new Callback<ResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    rlprogress.setVisibility(View.GONE);
                    statusdriver = Objects.requireNonNull(response.body()).getData();
                    if (response.body().getData().equals("1")) {
                        sp.updateKerja("ON");
                        onoff.setOn(true);
                    } else if (response.body().getData().equals("4")) {
                        sp.updateKerja("OFF");
                        onoff.setOn(false);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void dialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bank);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final ImageView close = dialog.findViewById(R.id.close);
        final RecyclerView list = dialog.findViewById(R.id.recycleview);


        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(context, 1));

        BanklistItem bankItem = new BanklistItem(context, mList, R.layout.item_petunjuk, new BanklistItem.OnItemClickListener() {
            @Override
            public void onItemClick(BankModels item) {
                if (item.getText().equals("Unlimited")) {
                    uangbelanja.setText(item.getText());
                } else {
                    Utility.currencyTXT(uangbelanja, item.getText(), context);
                }
                sp.updateMaksimalBelanja(item.getText());
                dialog.dismiss();
            }
        });

        list.setAdapter(bankItem);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    //-------------------------- Load Map -------------------------------------------------
    private void enable_location() {
        EnableLlocationFragment enable_llocationFragment = new EnableLlocationFragment();
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
        transaction.replace(R.id.splash, enable_llocationFragment).addToBackStack(null).commit();
    }

    private void checkAndRequestRunTimePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                enable_location();
                //Toast.makeText(SplashActivity.this, "Aktifkan Akses Untuk Pertamakali nya.\nDan Buka Ulang Aplikasi.", LENGTH_LONG).show();

            } else {
                return;
            }
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_PERMISSION_MULTIPLE);

            }
        }


        onRunTimePermissionGranted();
    }

    private void onRunTimePermissionGranted() {
        isDeninedRTPs = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_MULTIPLE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        isDeninedRTPs = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            showRationaleRTPs = shouldShowRequestPermissionRationale(permission);
                        }

                        break;
                    }

                }
                onRunTimePermissionDenied();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /******************************************************/

    private void onRunTimePermissionDenied() {
        if (isDeninedRTPs) {
            if (!showRationaleRTPs) {
                //goToSettings();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_CODE_PERMISSION_MULTIPLE);
                }
            }
        } else {
            onRunTimePermissionGranted();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            googleMapHomeFrag = googleMap;
            googleMapHomeFrag.getUiSettings().setAllGesturesEnabled(true);
            googleMapHomeFrag.getUiSettings().setScrollGesturesEnabled(true);
            googleMapHomeFrag.getUiSettings().setCompassEnabled(false);
            googleMapHomeFrag.getUiSettings().setMapToolbarEnabled(false);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            googleMapHomeFrag.setMyLocationEnabled(false);
            googleMapHomeFrag.getUiSettings().setMyLocationButtonEnabled(false);

            if (driverLatLng != null) {
                if (googleMapHomeFrag != null) {
                    googleMapHomeFrag.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLatLng, 17.0f));
                    googleMapHomeFrag.getUiSettings().setZoomControlsEnabled(false);

                }
            }
        }
        updateLastLocation();
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        googleMapHomeFrag.setMyLocationEnabled(true);

        if (lastKnownLocation != null) {
            googleMapHomeFrag.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 17f)
            );
            googleMapHomeFrag.animateCamera(CameraUpdateFactory.zoomTo(17f));
        }
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
//        String KotaSaya = loginUser.getKota();
        Locale localeID = new Locale("in", "ID");
        try {
            Geocoder geocoder = new Geocoder(context, localeID);
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String cityName = addresses.get(0).getSubAdminArea();
                String S1 = cityName.replaceAll("Kabupaten", "");
                String S2 = S1.replaceAll("Kabupatén", "");
                Log.d(TAG, "getAddress:  city" + S1);
                if (S2.isEmpty()) {
                    strArea = "offline";
                    tArea.setText(strArea);
                    ;
                } else {
                    strArea = S2;
                    tArea.setText(strArea);
                    ;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000 * 5);
        mLocationRequest.setFastestInterval(1000 * 3);
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            checkAndRequestRunTimePermissions();
            return;
        } else {
            LocationServices.getFusedLocationProviderClient(context)
                    .requestLocationUpdates(mLocationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    // do work here
                                    onLocationChanged(locationResult.getLastLocation());
                                }
                            },
                            Looper.myLooper());

        }
        updateLastLocation();
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            mLastLocation = location;
            driverLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMapHomeFrag.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(20)
                    .bearing(location.getBearing())
                    .build();
            googleMapHomeFrag.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
          //  showMovingCab(driverLatLng);
            if (isWorking = false) {

            }
           // getAddress(context, location.getLatitude(), location.getLongitude());
            if(location != null){
                try{
                    UpdateLokasi(location);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    //  UpdateToken(token);
                }
            });
           /* try {
                Locale localeID = new Locale("in", "ID");
                Geocoder gcd = new Geocoder(context, localeID);
                List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String countryName = addresses.get(0).getSubAdminArea();
                    String rCity = countryName.replaceAll("Kabupatén|Kabupaten| ","");
                    AreaRequestJson param = new AreaRequestJson();
                    final DriverService service = ServiceGenerator.createService(DriverService.class, "admin", "12345");
                    param.setId(rCity);
                    service.listarea(param).enqueue(new Callback<AreaResponseJson>() {
                        @Override
                        public void onResponse(@NonNull Call<AreaResponseJson> call, @NonNull Response<AreaResponseJson> response) {
                            if (response.isSuccessful()) {
                                if(Objects.requireNonNull(response.body()).getData().size() > 0){
                                    final AreaModels dataArea = Objects.requireNonNull(response.body()).getData().get(0);
                                    if(dataArea != null && response.body().getData().size() > 0){
                                        if(dataArea.getStatus().equals("1")){
                                            android.util.Log.e("Cek Area", "Terdaftar");
                                        }else{
                                            if (getActivity() == null || !isAdded()) return;
                                            Intent i = new Intent(context, ActivityClosed.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("Tema", Warna);
                                            i.putExtra("IsWorking", false);
                                            startActivity(i);

                                        }

                                    }
                                }else{
                                    if (getActivity() == null || !isAdded()) return;
                                    Intent i = new Intent(context, ActivityClosed.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("Tema", Warna);
                                    i.putExtra("IsWorking", false);
                                    startActivity(i);
                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AreaResponseJson> call, @NonNull Throwable t) {


                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }*/
            com.radjago.drivergo.utils.Log.e("Lokasi Saya", location.getLatitude() + "," + location.getLongitude());

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        updateLastLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        updateLastLocation();
    }

    //-----------------------------------------------------------------------------------------------
    private void UpdateToken(String Token) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateTokenRequestJson param = new UpdateTokenRequestJson();
        param.setId(loginUser.getId());
        param.setReg_id(Token);
        service.updateToken(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    android.util.Log.e("Token", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    private void logout() {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        GetHomeRequestJson request = new GetHomeRequestJson();
        request.setId(loginUser.getId());
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        service.logout(request).enqueue(new Callback<GetHomeResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<GetHomeResponseJson> call, @NonNull Response<GetHomeResponseJson> response) {
                if (response.isSuccessful()) {
                    rlprogress.setVisibility(View.GONE);
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        Realm realm = BaseApp.getInstance(context).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        BaseApp.getInstance(context).setLoginUser(null);
                        startActivity(new Intent(getContext(), IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        Objects.requireNonNull(getActivity()).finish();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetHomeResponseJson> call, @NonNull Throwable t) {
                rlprogress.setVisibility(View.GONE);
                t.printStackTrace();
                Toast.makeText(context, "error connection!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //notifikasi
    private void SendNotif(String Pesan) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //  Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.notifpoint);
            try {
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notifpoint");
                Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel;
            mChannel = new NotificationChannel("notify_002", "Point", NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(Pesan);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            // mChannel.setSound(soundUri, audioAttributes);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_002")
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo))
                    .setTicker("Radjago")
                    .setContentTitle("Radjago")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Pesan))
                    .setContentText(Pesan)
                    .setAutoCancel(true)
                    .setLights(0xff0000ff, 300, 1000) // blue color
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            int NOTIFICATION_ID = 1; // Causes to update the same notification over and over again.
            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }

    }

    private void UpdateLokasi(Location location) {
        if(location != null){
            User loginUser = BaseApp.getInstance(context).getLoginUser();
            if(loginUser.getEmail() != null && loginUser.getPassword() != null){
                DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
                UpdateLocationRequestJson param = new UpdateLocationRequestJson();
                param.setId(loginUser.getId());
                param.setLatitude(String.valueOf(location.getLatitude()));
                param.setLongitude(String.valueOf(location.getLongitude()));
                param.setStatus(String.valueOf(1));
                param.setRespon(String.valueOf(1));
                param.setBearing(String.valueOf(location.getBearing()));
                param.setKota(strArea);
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

        }

    }

    //------------------------------------------- Float Service --------------------------------------


    private void StartInfoArea() {
        handler = new Handler();
        handler.postDelayed(InfoArea, 3000);
    }

    private void StopInfoArea() {
        handler.removeCallbacks(InfoArea);
    }

    private void PointDriver() {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        String iddriver = loginUser.getId();
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<PointRespon> getdata = api.cekPoint(iddriver);
        getdata.enqueue(new Callback<PointRespon>() {
            @Override
            public void onResponse(Call<PointRespon> call, Response<PointRespon> response) {
                Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mPoint = response.body().getResult();
                String CPoint = mPoint.get(0).getPoint();
                TxtPoint.setText("Poin Saya " + CPoint);
                //  TPoint.setText(CPoint);
            }

            @Override
            public void onFailure(Call<PointRespon> call, Throwable t) {
                Log.d("RETRO", "FAILED : respon gagal");

            }
        });
    }

}
