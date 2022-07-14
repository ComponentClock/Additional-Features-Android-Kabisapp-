package com.radjago.drivergo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.radjago.drivergo.R;
import com.radjago.drivergo.activity.MainActivity;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.json.ItemRespon;
import com.radjago.drivergo.json.LayananRespon;
import com.radjago.drivergo.json.UpdateItemRespon;
import com.radjago.drivergo.models.ItemModel;
import com.radjago.drivergo.utils.api.MaswendServer;
import com.radjago.drivergo.utils.api.service.MaswendApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoItem {
    public static String IdMenu = "0";
    public static String IdTrans = "0";
    public static String TotJumlah = "0";
    public static String strHarga = "0";
    public static String strSubTotal = "0";
    public static String strHargaPromo = "0";
    public static String CekPromo = "0";
    public static String setHitung = "Kurang";
    public static int setHarga = 0;
    Context context;
    private List<ItemModel> mMenu = new ArrayList<ItemModel>();

    private static String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }
    //-------------------------------- cek Menu Item ---------------------------------------------

    public void showPopupWindow(final View view, String idMenu, String Layanan, String Transaksi, String Jumlah) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.layout_menu, null);
        context = view.getContext();
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        IdMenu = idMenu;
        IdTrans = Transaksi;
        ImageView imgClose = (ImageView) popupView.findViewById(R.id.imgclose);
        TextView tNama = (TextView) popupView.findViewById(R.id.txtnama);
        TextView tHarga = (TextView) popupView.findViewById(R.id.txtharga);
        ImageView imgmenu = (ImageView) popupView.findViewById(R.id.imagemenu);
        TextView tJumlah = (TextView) popupView.findViewById(R.id.txtjumlah);
        Button BtnMin = (Button) popupView.findViewById(R.id.btnMin);
        // Button BtnPlus = (Button) popupView.findViewById(R.id.btnPlus);
        Button BtnUpdate = (Button) popupView.findViewById(R.id.btnUpdate);
        Button BtnHapus = (Button) popupView.findViewById(R.id.btnHapus);
        int backgroundColor = ContextCompat.getColor(context, R.color.gray);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BtnUpdate.setEnabled(false);
            BtnUpdate.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        }
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<ItemRespon> getdata = api.cekItemMenu(idMenu);
        getdata.enqueue(new Callback<ItemRespon>() {
            @Override
            public void onResponse(Call<ItemRespon> call, Response<ItemRespon> response) {
                android.util.Log.d("RETRO", "RESPONSE : " + response.body().getKode());
                mMenu = response.body().getResult();
                String sNama = mMenu.get(0).getNama_item();
                String sHarga = mMenu.get(0).getHarga_item();
                String sHargaPromo = mMenu.get(0).getHarga_promo();
                String sFoto = mMenu.get(0).getFoto_item();
                String sStatus = mMenu.get(0).getStatus_promo();
                CekPromo = mMenu.get(0).getStatus_promo();
                String sJumlah = Jumlah;
                tNama.setText(sNama);
                tJumlah.setText(sJumlah);
                TotJumlah = tJumlah.getText().toString();
                if (sStatus.equals("0")) {
                    strHarga = sHarga;
                    int Harga = Integer.parseInt(strHarga);
                    int Qty = Integer.parseInt(sJumlah);
                    ;
                    int Hasils = Harga * Qty;
                    Double getprice = Double.valueOf(Hasils);
                    String zFormat = formatRupiah(getprice);
                    String ValFormat = zFormat.replaceAll(",00", "");
                    tHarga.setText(ValFormat);
                    strSubTotal = String.valueOf(Hasils);
                } else {
                    strHarga = sHargaPromo;
                    strHargaPromo = sHargaPromo;
                    int Harga = Integer.parseInt(strHarga);
                    int Qty = Integer.parseInt(sJumlah);
                    ;
                    int Hasils = Harga * Qty;
                    Double getprice = Double.valueOf(Hasils);
                    String zFormat = formatRupiah(getprice);
                    String ValFormat = zFormat.replaceAll(",00", "");
                    tHarga.setText(ValFormat);
                    strSubTotal = String.valueOf(Hasils);
                }

                Glide.get(context).clearMemory();
                clearGlideDiskCache();
                String baseurl = Constants.FOTO_MENU + sFoto;
                Glide.with(context)
                        .asBitmap()
                        .load(baseurl)
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.signatureOf(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                        .circleCrop()
                        .override(200, 200)
                        .listener(new RequestListener<Bitmap>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                                return false;
                            }
                        })
                        .apply(RequestOptions.placeholderOf(R.drawable.logo))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.signatureOf(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                        .into(imgmenu);

            }

            @Override
            public void onFailure(Call<ItemRespon> call, Throwable t) {
                android.util.Log.d("RETRO", "FAILED : respon gagal");

            }
        });

        BtnMin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                setHitung = "Kurang";
                // Snackbar.make(view, setHitung, Snackbar.LENGTH_LONG).show();
                int currentID = Integer.parseInt(tJumlah.getText().toString());
                currentID--;
                String Changed = String.valueOf(currentID--);
                tJumlah.setText(Changed);
                TotJumlah = tJumlah.getText().toString();
                int Harga = Integer.parseInt(strHarga);
                if (tJumlah.length() < 1) {
                    setHarga = Harga;
                } else {
                    setHarga = Harga * Integer.parseInt(tJumlah.getText().toString());
                    int backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        BtnUpdate.setEnabled(true);
                        BtnUpdate.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                    }
                }
                if (currentID < 0) {
                    tJumlah.setText("1");
                    TotJumlah = "1";
                    setHarga = Harga;
                    int backgroundColor = ContextCompat.getColor(context, R.color.gray);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        BtnUpdate.setEnabled(false);
                        BtnUpdate.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                    }
                } else {
                    int backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        BtnUpdate.setEnabled(true);
                        BtnUpdate.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                    }
                }

                Double getprice = Double.valueOf(setHarga);
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00", "");
                tHarga.setText(ValFormat);
                strSubTotal = String.valueOf(setHarga);
            }
        });

        /*BtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHitung = "Tambah";
                Snackbar.make(view, setHitung, Snackbar.LENGTH_LONG).show();
                int currentID  = Integer.parseInt(tJumlah.getText().toString());
                currentID++;
                String Changed = String.valueOf(currentID++);
                tJumlah.setText(Changed);

                int Harga = Integer.parseInt(strHarga);
                setHarga = Harga * Integer.parseInt(tJumlah.getText().toString());
                TotJumlah = tJumlah.getText().toString();
                Double getprice = Double.valueOf(setHarga);
                String zFormat = formatRupiah(getprice);
                String ValFormat = zFormat.replaceAll(",00","");
                tHarga.setText(ValFormat);
                int backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    BtnUpdate.setEnabled(true);
                    BtnUpdate.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                }
                strSubTotal = String.valueOf(setHarga);
            }
        });
*/

        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTransaksi(Transaksi, strHarga, Layanan);
                Snackbar.make(view, "Menu Berhasil Diperbarui.", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        BtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHitung = "Kurang";
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                HapusByItem(Transaksi, strSubTotal, Layanan);
                                HapusItem();
                                Snackbar.make(view, "Menu Berhasil Dihapus.", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Apakah Anda Yakin ?").setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    void clearGlideDiskCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }

    //----------------------------------------- Update Harga ------------------------------------
    private void UpdateTransaksi(String idtrans, String Biaya, String CurHarga) {
        String Saldo = CurHarga;
        int angka1;
        int angka2;
        int hasil;
        angka1 = Integer.parseInt(Saldo);
        angka2 = Integer.parseInt(Biaya);
        switch (setHitung) {
            case "Kurang":
                hasil = angka1 - angka2;
                break;
            case "Tambah":
                hasil = angka1 + angka2;
                break;
            default:
                hasil = Integer.parseInt(strHarga);
                break;
        }

        String UpdateBiaya = String.valueOf(hasil);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<LayananRespon> update = api.updateLayanan(idtrans, UpdateBiaya);
        update.enqueue(new Callback<LayananRespon>() {
            @Override
            public void onResponse(Call<LayananRespon> call, Response<LayananRespon> response) {
                android.util.Log.d("Retro", "Response");
                UpdateItem();
            }

            @Override
            public void onFailure(Call<LayananRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }

    private void UpdateItem() {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        String Jumlah = TotJumlah;
        String Subtotal = strSubTotal;
        Call<UpdateItemRespon> update = api.updateItem(IdMenu, IdTrans, Jumlah, Subtotal);
        update.enqueue(new Callback<UpdateItemRespon>() {
            @Override
            public void onResponse(Call<UpdateItemRespon> call, Response<UpdateItemRespon> response) {
                android.util.Log.d("Retro", "Response");
            }

            @Override
            public void onFailure(Call<UpdateItemRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }

    private void HapusByItem(String idtrans, String Biaya, String CurHarga) {
        String Saldo = CurHarga;
        int angka1;
        int angka2;
        int hasil;
        angka1 = Integer.parseInt(Saldo);
        angka2 = Integer.parseInt(Biaya);
        hasil = angka1 - angka2;
        String UpdateBiaya = String.valueOf(hasil);
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<LayananRespon> update = api.updateLayanan(idtrans, UpdateBiaya);
        update.enqueue(new Callback<LayananRespon>() {
            @Override
            public void onResponse(Call<LayananRespon> call, Response<LayananRespon> response) {
                android.util.Log.d("Retro", "Response");
                UpdateItem();
            }

            @Override
            public void onFailure(Call<LayananRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }

    private void HapusItem() {
        MaswendApi api = MaswendServer.getClient().create(MaswendApi.class);
        Call<UpdateItemRespon> update = api.hapusItem(IdMenu, IdTrans);
        update.enqueue(new Callback<UpdateItemRespon>() {
            @Override
            public void onResponse(Call<UpdateItemRespon> call, Response<UpdateItemRespon> response) {
                android.util.Log.d("Retro", "Response");

            }

            @Override
            public void onFailure(Call<UpdateItemRespon> call, Throwable t) {
                android.util.Log.d("Retro", "OnFailure");

            }
        });
    }
}
