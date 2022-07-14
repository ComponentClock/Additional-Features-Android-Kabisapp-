package com.radjago.drivergo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.angads25.toggle.LabeledSwitch;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.constants.VersionChecker;
import com.radjago.drivergo.fragment.HistoryFragment;
import com.radjago.drivergo.fragment.HomeFragment;
import com.radjago.drivergo.fragment.OrderFragment;
import com.radjago.drivergo.fragment.ProfileFragment;
import com.radjago.drivergo.fragment.TopupFragment;
import com.radjago.drivergo.json.FcmKeyResponse;
import com.radjago.drivergo.json.GetHomeRequestJson;
import com.radjago.drivergo.json.GetHomeResponseJson;
import com.radjago.drivergo.json.MapKeyResponse;
import com.radjago.drivergo.json.ResponseJson;
import com.radjago.drivergo.json.UpdateLocationRequestJson;
import com.radjago.drivergo.models.DataDriver;
import com.radjago.drivergo.models.FcmKeyModel;
import com.radjago.drivergo.models.MainBGModel;
import com.radjago.drivergo.models.MapKeyModel;
import com.radjago.drivergo.models.PayuModel;
import com.radjago.drivergo.models.TransaksiModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.mui.MIUIUtils;
import com.radjago.drivergo.service.MWService;
import com.radjago.drivergo.service.MaswendService;
import com.radjago.drivergo.service.Restarter;
import com.radjago.drivergo.service.SensorService;
import com.radjago.drivergo.utils.BackgroundService;
import com.radjago.drivergo.utils.HomeWatcher;
import com.radjago.drivergo.utils.MyLocationService;
import com.radjago.drivergo.utils.SettingPreference;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;
    private static final String TAG = "MainActivity";
    public static boolean isWorking = true;

    public static String Warna = "#7309A8";
    public static String Email;
    public static String Password;
    public static String apikey;
    public static String fcmkey;
    public static String strArea;
    public static LatLng LokasiDriver;
    public static Float BearingDriver;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2323;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;
    long mBackPressed;
    List<MainBGModel> mainbglist;
    //service
    Intent mServiceIntent;
    private MWService mService;
    LinearLayout mAdViewLayout;
    BottomNavigationView navigation;
    int previousSelect = 0;
    SettingPreference sp;
    OrderFragment orderFragment;
    HomeFragment homeFragment;
    LocationRequest locationRequest;
    RelativeLayout rlprogress;
    ProgressBar Progressbar;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean canceled;
    List<MapKeyModel> mapkeylist;
    List<FcmKeyModel> fcmkeylist;
    private SensorService mSensorService;
    //end service
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    private FragmentManager fragmentManager;
    private LabeledSwitch onoff;
    private String statusdriver, saldodriver;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Menu menu = navigation.getMenu();
            menu.findItem(R.id.home).setIcon(R.drawable.ic_home_hitam);
            menu.findItem(R.id.order).setIcon(R.drawable.ic_list_hitam);
            menu.findItem(R.id.wallet).setIcon(R.drawable.ic_wallet_hitam);
            menu.findItem(R.id.profile).setIcon(R.drawable.ic_user_hitam);

            String gray = "#b6b6b6";



            TransaksiModel transaksi = new TransaksiModel();
            switch (item.getItemId()) {
                case R.id.home:
                    canceled = false;
                    navigationItemSelected(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Warna == null) {
                            Warna = "#7309A8";
                        }
                        item.setIconTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
                    }
                    item.setIcon(R.drawable.ic_home_ungu);
                    if(!(menu.findItem(R.id.home).isChecked())) menu.findItem(R.id.home).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.order).isChecked())) menu.findItem(R.id.order).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.wallet).isChecked())) menu.findItem(R.id.wallet).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.profile).isChecked())) menu.findItem(R.id.profile).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    gethome();

                    // toolbar.setVisibility(View.GONE);
                    return true;
                case R.id.order:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    HistoryFragment listFragment = new HistoryFragment();
                    navigationItemSelected(1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Warna == null) {
                            Warna = "#7309A8";
                        }
                        item.setIconTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
                    }
                    item.setIcon(R.drawable.ic_list_ungu);
                    if(!(menu.findItem(R.id.home).isChecked())) menu.findItem(R.id.home).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.order).isChecked())) menu.findItem(R.id.order).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.wallet).isChecked())) menu.findItem(R.id.wallet).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.profile).isChecked())) menu.findItem(R.id.profile).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    loadFrag2(listFragment, getString(R.string.menu_home), fragmentManager, transaksi, "", "", false);
                    return true;
                case R.id.wallet:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    TopupFragment topupFragment = new TopupFragment();
                    navigationItemSelected(2);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Warna == null) {
                            Warna = "#7309A8";
                        }
                        item.setIconTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
                    }
                    item.setIcon(R.drawable.ic_wallet_ungu);
                    if(!(menu.findItem(R.id.home).isChecked())) menu.findItem(R.id.home).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.order).isChecked())) menu.findItem(R.id.order).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.wallet).isChecked())) menu.findItem(R.id.wallet).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.profile).isChecked())) menu.findItem(R.id.profile).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    loadFrag2(topupFragment, getString(R.string.menu_wallet), fragmentManager, transaksi, "", "", false);
                    return true;
                case R.id.profile:
                    canceled = true;
                    rlprogress.setVisibility(View.GONE);
                    ProfileFragment profilFragment = new ProfileFragment();
                    navigationItemSelected(3);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (Warna == null) {
                            Warna = "#7309A8";
                        }
                        item.setIconTintList(ColorStateList.valueOf(Color.parseColor(Warna)));
                    }
                    item.setIcon(R.drawable.ic_user_ungu);
                    if(!(menu.findItem(R.id.home).isChecked())) menu.findItem(R.id.home).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.order).isChecked())) menu.findItem(R.id.order).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.wallet).isChecked())) menu.findItem(R.id.wallet).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    if(!(menu.findItem(R.id.profile).isChecked())) menu.findItem(R.id.profile).setIconTintList(ColorStateList.valueOf(Color.parseColor(gray)));
                    loadFrag2(profilFragment, getString(R.string.menu_profile), fragmentManager, transaksi, "", "", false);
                    return true;

            }
            return false;
        }
    };

    public static MainActivity getInstance() {
        return mainActivity;
    }

    public static String getMyArea() {

        return strArea;
    }

    private void getmapkey() {
        MapKeyModel mwmodel = new MapKeyModel();
        mwmodel.setId(1);
        DriverService service = ServiceGenerator.createService(DriverService.class, "admin", "12345");
        service.mwapikey().enqueue(new Callback<MapKeyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MapKeyResponse> call, @NonNull Response<MapKeyResponse> response) {

                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("found")) {
                        mapkeylist = response.body().getData();
                        String getkey = mapkeylist.get(0).getMapkey();
                        apikey = getkey;
                        //apikey = getString(R.string.google_maps_key);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MapKeyResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getfcmkey() {
        FcmKeyModel mwmodel = new FcmKeyModel();
        mwmodel.setId(1);
        DriverService service = ServiceGenerator.createService(DriverService.class, "admin", "12345");
        service.fcmapikey().enqueue(new Callback<FcmKeyResponse>() {
            @Override
            public void onResponse(@NonNull Call<FcmKeyResponse> call, @NonNull Response<FcmKeyResponse> response) {

                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("found")) {
                        fcmkeylist = response.body().getData();
                        String getkey = fcmkeylist.get(0).getFcmkey();
                        fcmkey = getkey;
                        //apikey = getString(R.string.google_maps_key);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FcmKeyResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void UpdateLokasi(Location location) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        if (loginUser.getEmail() != null && loginUser.getPassword() != null) {
            DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
            UpdateLocationRequestJson param = new UpdateLocationRequestJson();
            param.setId(loginUser.getId());
            param.setLatitude(String.valueOf(location.getLatitude()));
            param.setLongitude(String.valueOf(location.getLongitude()));
            param.setStatus(String.valueOf(1));
            param.setRespon(String.valueOf(1));
            param.setBearing(String.valueOf(location.getBearing()));
            param.setKota("Offline");
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

    private void UpdateLokasi_old(Location location) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        String IdDriver = loginUser.getId();
        String MyApp = getString(R.string.app_name);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (isWorking = true) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("MDriver");
            DataDriver user = new DataDriver(IdDriver, location.getLatitude(), location.getLongitude(), location.getBearing(), "1");
            mDatabase.child(IdDriver).setValue(user);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        boolean CekWorking;
        String Tema = "#7309A8";
        mService = new MWService();
        mServiceIntent = new Intent(this, mService.getClass());
        if (!isMyServiceRunning(mService.getClass())) {
            startService(mServiceIntent);
        }
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Tema = "#7309A8";
                isWorking = false;
                Warna = Tema;
            } else {
                Tema = extras.getString("Tema");
                CekWorking = extras.getBoolean("IsWorking");
                isWorking = CekWorking;
                Warna = Tema;
            }
        } else {
            Tema = (String) savedInstanceState.getSerializable("Tema");
            // CekWorking = (Boolean) savedInstanceState.getSerializable("IsWorking");
            Warna = Tema;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRef = mDatabase.getRef().child("Driver");

        Progressbar = findViewById(R.id.Progressbar);
        mAdViewLayout = findViewById(R.id.adView);
        fragmentManager = getSupportFragmentManager();
        navigation = findViewById(R.id.navigation);
        sp = new SettingPreference(this);
        orderFragment = new OrderFragment();
        homeFragment = new HomeFragment();
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        getmapkey();
      //  apikey = getString(R.string.google_maps_key);
        getfcmkey();
        rlprogress = findViewById(R.id.rlprogress);
        canceled = false;
        User loginUser = BaseApp.getInstance(this).getLoginUser();
//        Constants.TOKEN = loginUser.getToken();
        //   Constants.USERID = loginUser.getId();
        startService(new Intent(this, BackgroundService.class));
        if (Build.VERSION.SDK_INT >= 19 && MIUIUtils.isMIUI() && !MIUIUtils.isFloatWindowOptionAllowed(this)) {
            Log.i(TAG, "MIUI DEVICE: Screen Overlay Not allowed");
            startActivityForResult(MIUIUtils.toFloatWindowPermission(this, getPackageName()), ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);

        } else if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Log.i(TAG, "SDK_INT > 23: Screen Overlay Not allowed");
            startActivityForResult(new Intent(
                            "android.settings.action.MANAGE_OVERLAY_PERMISSION",
                            Uri.parse("package:" + getPackageName()))
                    , ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE
            );

        } else {
            Log.i(TAG, "SKK_INT < 19 or Have overlay permission");

        }
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
              //  createFloatingWidget();
                Constants.isBackground = true;
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
        //service
        /*mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }*/
        //service
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Constants.versionname = Objects.requireNonNull(packageInfo).versionName;
        updatelocation();
        gethome();
        if (sp.getSetting()[19].equals("2") || sp.getSetting()[19].equals("3")) {
            navigation.setVisibility(View.GONE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Tema = "#7309A8";
            int greenColorValue = Color.parseColor(Tema);
            Progressbar.setIndeterminateTintList(ColorStateList.valueOf(greenColorValue));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    //hapus database
    private void HapusDB() {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        if (loginUser != null) {
            String IdDriver = loginUser.getId();
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Driver").child(IdDriver);
            dR.removeValue();
            dR.removeValue();
        }

    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
        //   HapusDB();
        // stopService(mServiceIntent);
    //    createFloatingWidget();
        Constants.isBackground = true;
        //stopTema();
        Log.i("MAINACT", "onDestroy!");
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  KillFloatingWidget();
        Constants.isBackground = false;
      //  startService(new Intent(this, MWLocation.class));
        //  startTema();
        //   Check_version();
        //  startService(mServiceIntent);
    }

    public void Check_version() {
        VersionChecker versionChecker = new VersionChecker(this);
        versionChecker.execute();
    }

    @Override
    public void onBackPressed() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                clickDone();

            }
        } else {
            super.onBackPressed();
        }
    }

    public void clickDone() {
        new AlertDialog.Builder(this, R.style.DialogStyle)
                .setIcon(R.mipmap.ic_launcher_driver)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //stopTema();
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void gethome() {

        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetHomeRequestJson param = new GetHomeRequestJson();
        param.setId(loginUser.getId());
        param.setPhone(loginUser.getNoTelepon());
        userService.home(param).enqueue(new Callback<GetHomeResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<GetHomeResponseJson> call, @NonNull final Response<GetHomeResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        PayuModel payu = response.body().getPayu().get(0);
                        Constants.CURRENCY = response.body().getCurrency();
                        sp.updateCurrency(response.body().getCurrency());
                        sp.updateabout(response.body().getAboutus());
                        sp.updateemail(response.body().getEmail());
                        sp.updatephone(response.body().getPhone());
                        sp.updateweb(response.body().getWebsite());
                        sp.updatePaypal(response.body().getPaypalkey());
                        sp.updatepaypalmode(response.body().getPaypalmode());
                        sp.updatepaypalactive(response.body().getPaypalactive());
                        sp.updatestripeactive(response.body().getStripeactive());
                        sp.updatecurrencytext(response.body().getCurrencytext());
                        sp.updatePayudebug(payu.getPayudebug());
                        sp.updatePayumerchantid(payu.getPayuid());
                        sp.updatePayusalt(payu.getPayusalt());
                        sp.updatePayumerchantkey(payu.getPayukey());
                        sp.updatePayuActive(payu.getActive());
                        sp.updateStatusdriver(response.body().getDriverstatus());
                        //      sp.updateJob(response.body().getJob());
                        //      sp.updateMapKey(response.body().getMapkey());
                        TransaksiModel transaksifake = new TransaksiModel();
                        if (!canceled) {
                            if (response.body().getDriverstatus().equals("3") || response.body().getDriverstatus().equals("2")) {
                                isWorking = true;
                                TransaksiModel transaksi = response.body().getTransaksi().get(0);
                                LatLng Asal = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                                LatLng Tujuan = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                                navigation.setVisibility(View.GONE);
                                loadFrag2(orderFragment, getString(R.string.menu_home), fragmentManager, transaksi, response.body().getSaldo(), response.body().getDriverstatus(), true);
                            } else {
                                isWorking = false;
                                navigation.setVisibility(View.VISIBLE);
                                loadFrag2(homeFragment, getString(R.string.menu_home), fragmentManager, transaksifake, response.body().getSaldo(), response.body().getDriverstatus(), true);
                            }
                        }
                        User user = response.body().getDatadriver().get(0);
                        saveUser(user);
                        if (mainActivity != null) {
                            Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                            User loginUser = BaseApp.getInstance(MainActivity.this).getLoginUser();
                            realm.beginTransaction();
                            loginUser.setWalletSaldo(Long.parseLong(response.body().getSaldo()));
                            realm.commitTransaction();
                        }
                        rlprogress.setVisibility(View.GONE);
                    } else {
                        Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                        realm.beginTransaction();
                        realm.delete(User.class);
                        realm.commitTransaction();
                        BaseApp.getInstance(MainActivity.this).setLoginUser(null);
                        startActivity(new Intent(MainActivity.this, IntroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                        Toast.makeText(MainActivity.this, "Akun Anda telah ditangguhkan, harap hubungi admin!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<GetHomeResponseJson> call, @NonNull Throwable t) {

            }
        });

    }

    private void gethomeold() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        if (Email != null | Password != null | strArea != null) {
            Email = "admin";
            Password = "12345";
        } else {
            Email = loginUser.getNoTelepon();
            Password = loginUser.getPassword();
            DriverService userService = ServiceGenerator.createService(
                    DriverService.class, Email, Password);
            GetHomeRequestJson param = new GetHomeRequestJson();
            param.setId(loginUser.getId());
            param.setPhone(loginUser.getNoTelepon());
            userService.home(param).enqueue(new Callback<GetHomeResponseJson>() {
                @Override
                public void onResponse(@NonNull Call<GetHomeResponseJson> call, @NonNull final Response<GetHomeResponseJson> response) {
                    if (response.isSuccessful()) {
                        if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                            PayuModel payu = response.body().getPayu().get(0);
                            Constants.CURRENCY = response.body().getCurrency();
                            sp.updateCurrency(response.body().getCurrency());
                            sp.updateabout(response.body().getAboutus());
                            sp.updateemail(response.body().getEmail());
                            sp.updatephone(response.body().getPhone());
                            sp.updateweb(response.body().getWebsite());
                            sp.updatePaypal(response.body().getPaypalkey());
                            sp.updatepaypalmode(response.body().getPaypalmode());
                            sp.updatepaypalactive(response.body().getPaypalactive());
                            sp.updatestripeactive(response.body().getStripeactive());
                            sp.updatecurrencytext(response.body().getCurrencytext());
                            sp.updatePayudebug(payu.getPayudebug());
                            sp.updatePayumerchantid(payu.getPayuid());
                            sp.updatePayusalt(payu.getPayusalt());
                            sp.updatePayumerchantkey(payu.getPayukey());
                            sp.updatePayuActive(payu.getActive());
                            sp.updateStatusdriver(response.body().getDriverstatus());
                            //      sp.updateJob(response.body().getJob());
                            //      sp.updateMapKey(response.body().getMapkey());
                            TransaksiModel transaksifake = new TransaksiModel();
                            if (!canceled) {
                                if (response.body().getDriverstatus().equals("3") || response.body().getDriverstatus().equals("2")) {
                                    isWorking = true;
                                    TransaksiModel transaksi = response.body().getTransaksi().get(0);
                                    LatLng Asal = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                                    LatLng Tujuan = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                                    navigation.setVisibility(View.GONE);
                                    loadFrag2(orderFragment, getString(R.string.menu_home), fragmentManager, transaksi, response.body().getSaldo(), response.body().getDriverstatus(), true);
                                } else {
                                    isWorking = false;
                                    navigation.setVisibility(View.VISIBLE);
                                    loadFrag2(homeFragment, getString(R.string.menu_home), fragmentManager, transaksifake, response.body().getSaldo(), response.body().getDriverstatus(), true);
                                }
                            }
                            User user = response.body().getDatadriver().get(0);
                            saveUser(user);
                            if (mainActivity != null) {
                                Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                                User loginUser = BaseApp.getInstance(MainActivity.this).getLoginUser();
                                realm.beginTransaction();
                                loginUser.setWalletSaldo(Long.parseLong(response.body().getSaldo()));
                                realm.commitTransaction();
                            }
                            rlprogress.setVisibility(View.GONE);
                        } else {
                            Realm realm = BaseApp.getInstance(MainActivity.this).getRealmInstance();
                            realm.beginTransaction();
                            realm.delete(User.class);
                            realm.commitTransaction();
                            BaseApp.getInstance(MainActivity.this).setLoginUser(null);
                            startActivity(new Intent(MainActivity.this, IntroActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                            Toast.makeText(MainActivity.this, "Your account has been suspended, please contact admin!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetHomeResponseJson> call, @NonNull Throwable t) {

                }
            });
        }

    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(MainActivity.this).setLoginUser(user);
    }

    public void loadFrag2(Fragment f1, String name, FragmentManager fm, TransaksiModel transaksi, String saldo, String status, Boolean SetWorking) {
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle args = new Bundle();
        args.putString("id_pelanggan", transaksi.getIdPelanggan());
        args.putString("id_transaksi", transaksi.getId());
        args.putString("response", String.valueOf(transaksi.status));
        args.putString("saldo", saldo);
        args.putString("status", status);
        args.putString("myarea", strArea);
        args.putDouble("AsalLat", transaksi.getStartLatitude());
        args.putDouble("AsalLng", transaksi.getStartLongitude());
        args.putDouble("TujuanLat", transaksi.getEndLatitude());
        args.putDouble("TujuanLng", transaksi.getEndLongitude());
        args.putBoolean("IsWorking", SetWorking);
        f1.setArguments(args);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.Container, f1, name);
        ft.commitAllowingStateLoss();
    }

    public void navigationItemSelected(int position) {
        previousSelect = position;
    }

    @SuppressLint("MissingPermission")
    private void updatelocation() {
        buildlocation();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildlocation() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void Updatelocationdata(final Location location) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                onLocationChanged(location);
            }
        });

    }
    //fungsi area
    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        String KotaSaya = loginUser.getKota();
        Locale localeID = new Locale("in", "ID");
        try {
            Geocoder geocoder = new Geocoder(context, localeID);
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String cityName = addresses.get(0).getSubAdminArea();
                String S1 = cityName.replaceAll("Kabupaten", "");
                String S2 = S1.replaceAll("Kabupatén", "");
                Log.d(TAG, "getAddress:  city" + S1);
                strArea = S2;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            if (isWorking = true) {
                UpdateLokasi(location);
            }
            User loginUser = BaseApp.getInstance(this).getLoginUser();
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            @SuppressLint("MissingPermission") Location Lokasi = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            getAddress(MainActivity.this, Lokasi.getLatitude(), Lokasi.getLongitude());
            DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
            UpdateLocationRequestJson param = new UpdateLocationRequestJson();
            param.setId(loginUser.getId());
          //  param.setLatitude(String.valueOf(location.getLatitude()));
          //  param.setLongitude(String.valueOf(location.getLongitude()));
            param.setStatus("1");
            param.setRespon("1");
            param.setKota("Offline");
          //  param.setBearing(String.valueOf(location.getBearing()));
            LokasiDriver = new LatLng(location.getLatitude(), location.getLongitude());
            BearingDriver = location.getBearing();
            service.updatelocation(param).enqueue(new Callback<ResponseJson>() {
                @Override
                public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                    if (response.isSuccessful()) {
                        Log.e("location", response.message());

                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ResponseJson> call, @NonNull Throwable t) {

                }
            });
        }
    }
    //----------------------------- Service -------------------------------------------------------
    public void createFloatingWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            startFloatingWidgetService();

    }
    private void startFloatingWidgetService() {
        this.startService(new Intent(this, MaswendService.class));

    }
    public void KillFloatingWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        } else
            stopFloatingWidgetService();

    }
    private void stopFloatingWidgetService() {
        this.stopService(new Intent(this, MaswendService.class));

    }
}
