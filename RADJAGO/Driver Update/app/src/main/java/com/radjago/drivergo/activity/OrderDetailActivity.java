package com.radjago.drivergo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;
import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.item.ItemPesananItem;
import com.radjago.drivergo.json.DetailRequestJson;
import com.radjago.drivergo.json.DetailTransResponseJson;
import com.radjago.drivergo.models.PelangganModel;
import com.radjago.drivergo.models.TransaksiModel;
import com.radjago.drivergo.models.User;
import com.radjago.drivergo.utils.BackgroundColorTransform;
import com.radjago.drivergo.utils.Log;
import com.radjago.drivergo.utils.Utility;
import com.radjago.drivergo.utils.api.ServiceGenerator;
import com.radjago.drivergo.utils.api.service.DriverService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    public static String Warna;
    @Nullable
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
    @BindView(R.id.llchat)
    LinearLayout llchat;
    @BindView(R.id.background)
    CircleImageView foto;
    @BindView(R.id.pickUpText)
    TextView pickUpText;
    @BindView(R.id.destinationText)
    TextView destinationText;
    @BindView(R.id.fitur)
    TextView fiturtext;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.namamerchant)
    TextView namamerchant;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.phonenumber)
    ImageView phone;
    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.llchatmerchant)
    LinearLayout llchatmerchant;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.senddetail)
    LinearLayout lldetailsend;
    @BindView(R.id.produk)
    TextView produk;
    @BindView(R.id.sendername)
    TextView sendername;
    @BindView(R.id.orderid)
    TextView OrderID;
    @BindView(R.id.receivername)
    TextView receivername;
    @BindView(R.id.senderphone)
    Button senderphone;
    //  @BindView(R.id.ikonjob)
    //  ImageView IkonJob;
    @BindView(R.id.receiverphone)
    Button receiverphone;
    @BindView(R.id.scroller)
    NestedScrollView scrollView;
    @BindView(R.id.ratingView)
    RatingView ratingView;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.deliveryfee)
    TextView deliveryfee;
    @BindView(R.id.orderdetail)
    LinearLayout llorderdetail;
    @BindView(R.id.merchantdetail)
    LinearLayout llmerchantdetail;
    @BindView(R.id.merchantinfo)
    LinearLayout llmerchantinfo;
    @BindView(R.id.shimmerlayanan)
    ShimmerFrameLayout shimmerlayanan;
    @BindView(R.id.shimmerpickup)
    ShimmerFrameLayout shimmerpickup;
    @BindView(R.id.shimmerdestination)
    ShimmerFrameLayout shimmerdestination;
    @BindView(R.id.shimmerfitur)
    ShimmerFrameLayout shimmerfitur;
    @BindView(R.id.shimmerprice)
    ShimmerFrameLayout shimmerprice;
    @BindView(R.id.merchantnear)
    RecyclerView rvmerchantnear;
    String idtrans, idpelanggan, response, fitur;
    ItemPesananItem itemPesananItem;
    TextView totaltext;
    private GoogleMap gMap;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private Polyline directionLine2;

    @Override
    @Optional
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
//        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
       // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
       // behavior.setPeekHeight(300);
        ratingView.setVisibility(View.VISIBLE);
        llchat.setVisibility(View.GONE);
        llchatmerchant.setVisibility(View.GONE);
        totaltext = findViewById(R.id.totaltext);
        shimmerload();
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent intent = getIntent();
        idpelanggan = intent.getStringExtra("id_pelanggan");
        idtrans = intent.getStringExtra("id_transaksi");
        response = intent.getStringExtra("response");
        if (Objects.equals(response, "2")) {
            llchat.setVisibility(View.VISIBLE);
            layanandesk.setText(getString(R.string.notification_accept));
        } else if (Objects.equals(response, "3")) {
            llchat.setVisibility(View.VISIBLE);

            layanandesk.setText(getString(R.string.notification_start));
        } else if (Objects.equals(response, "4")) {
            scrollView.setPadding(0, 0, 0, 10);
            llchat.setVisibility(View.GONE);

            layanandesk.setText(getString(R.string.notification_finish));
        } else if (Objects.equals(response, "5")) {
            scrollView.setPadding(0, 0, 0, 10);
            llchat.setVisibility(View.GONE);

            layanandesk.setText(getString(R.string.notification_cancel));
        }
        Warna = "#1AC463";
        String BGColor = Warna;
    }

    private void getData(final String idtrans, final String idpelanggan) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdPelanggan(idpelanggan);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(@NonNull Call<DetailTransResponseJson> call, @NonNull Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    shimmertutup();
                    Log.e("", String.valueOf(Objects.requireNonNull(responsedata.body()).getData().get(0)));
                    final TransaksiModel transaksi = responsedata.body().getData().get(0);
                    PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);
                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                    OrderID.setText(transaksi.getId());
                    fitur = transaksi.getOrderFitur();
                    String Job = loginUser.getJob();

                    switch (transaksi.getHome()) {
                        case "3":
                            lldestination.setVisibility(View.GONE);
                            fiturtext.setText(transaksi.getEstimasi());
                            break;
                        case "4":
                            llorderdetail.setVisibility(View.VISIBLE);
                            llmerchantdetail.setVisibility(View.VISIBLE);
                            llmerchantinfo.setVisibility(View.VISIBLE);
                            Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), OrderDetailActivity.this);
                            Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), OrderDetailActivity.this);
                            namamerchant.setText(transaksi.getNama_merchant());

                            itemPesananItem = new ItemPesananItem(responsedata.body().getItem(), R.layout.item_pesanan);
                            rvmerchantnear.setAdapter(itemPesananItem);
                            break;
                        case "2":
                            lldetailsend.setVisibility(View.VISIBLE);
                            produk.setText(transaksi.getNamaBarang());
                            sendername.setText(transaksi.namaPengirim);
                            receivername.setText(transaksi.namaPenerima);

                            senderphone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDetailActivity.this, R.style.DialogStyle);
                                    alertDialogBuilder.setTitle("Call Driver");
                                    alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
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
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDetailActivity.this, R.style.DialogStyle);
                                    alertDialogBuilder.setTitle("Call Driver");
                                    alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                    alertDialogBuilder.setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
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

                            break;
                    }
                    parsedata(transaksi, pelanggan);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<DetailTransResponseJson> call, @NonNull Throwable t) {

            }
        });


    }

    private void shimmerload() {
        shimmerlayanan.startShimmerAnimation();
        shimmerpickup.startShimmerAnimation();
        shimmerdestination.startShimmerAnimation();
        shimmerfitur.startShimmerAnimation();
        shimmerprice.startShimmerAnimation();

        layanan.setVisibility(View.GONE);
        layanandesk.setVisibility(View.GONE);
        pickUpText.setVisibility(View.GONE);
        destinationText.setVisibility(View.GONE);
        fiturtext.setVisibility(View.GONE);
        priceText.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        shimmerlayanan.stopShimmerAnimation();
        shimmerpickup.stopShimmerAnimation();
        shimmerdestination.stopShimmerAnimation();
        shimmerfitur.stopShimmerAnimation();
        shimmerprice.stopShimmerAnimation();

        shimmerlayanan.setVisibility(View.GONE);
        shimmerpickup.setVisibility(View.GONE);
        shimmerdestination.setVisibility(View.GONE);
        shimmerfitur.setVisibility(View.GONE);
        shimmerprice.setVisibility(View.GONE);

        layanan.setVisibility(View.VISIBLE);
        layanandesk.setVisibility(View.VISIBLE);
        pickUpText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        fiturtext.setVisibility(View.VISIBLE);
        priceText.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void parsedata(TransaksiModel request, final PelangganModel pelanggan) {
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());
        Picasso.get()
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .transform(new BackgroundColorTransform(ContextCompat.getColor(getApplicationContext(), R.color.white)))
                .placeholder(R.drawable.nocamera)
                .error(R.drawable.nocamera)
                .into(foto);

        if (request.isPakaiWallet()) {
            totaltext.setText("Total (Wallet)");
        } else {
            totaltext.setText("Total (Cash)");
        }

        if (!request.getRate().isEmpty()) {
            ratingView.setRating(Float.parseFloat(request.getRate()));
        }

        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (request.getHome().equals("4")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga() + totalbiaya), this);
        } else {
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()), this);
        }

    }

    @Override
    public void onResume() {
        getData(idtrans, idpelanggan);
        super.onResume();

    }
    @Override
    public void onConnected(@androidx.annotation.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
