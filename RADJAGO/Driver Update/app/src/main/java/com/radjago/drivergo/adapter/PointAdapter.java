package com.radjago.drivergo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.radjago.drivergo.R;
import com.radjago.drivergo.activity.ActivityPoint;
import com.radjago.drivergo.activity.MainActivity;
import com.radjago.drivergo.constants.Constants;
import com.radjago.drivergo.models.PointModel;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.HolderData> {
    public static String Warna = "#1AC463";
    private List<PointModel> mList;
    private Context ctx;
    private OnItemClicked mListener;

    public PointAdapter(Context ctx, List<PointModel> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    public PointAdapter(Context context, OnItemClicked listener, List<PointModel> fileNames) {
        this.mListener = listener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_point, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    void clearGlideDiskCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(ctx).clearDiskCache();
            }
        }).start();
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        PointModel dm = mList.get(position);
        //tema
        String GetWarna = MainActivity.Warna;
        if (GetWarna != null) {
            Warna = GetWarna;
        } else {
            Warna = "#1AC463";
        }
        String BGColor = Warna;
        int colorCodeDark = Color.parseColor(BGColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.thumbnail.setBackgroundColor(colorCodeDark);
            holder.icontgl.setImageTintList(ColorStateList.valueOf(colorCodeDark));
            holder.iconuang.setImageTintList(ColorStateList.valueOf(colorCodeDark));
        }
        Glide.get(ctx).clearMemory();
        clearGlideDiskCache();
        String baseurl = Constants.IMAGEVOUCHER + dm.getImage_promo();
        Glide.with(ctx)
                .asBitmap()
                .load(baseurl)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.signatureOf(new ObjectKey(String.valueOf(System.currentTimeMillis()))))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
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
                .into(holder.thumbnail);
        holder.nama.setText(dm.getNama_promo());
        holder.point.setText("Diperlukan : " + dm.getPoint() + " Point");
        holder.reward.setText(dm.getNominal_promo());
        holder.tipe.setText("Tipe Penukaran : " + dm.getType_promo());
        //---------------------------- Exp ----------------------------
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expdate = null;
        try {
            expdate = newDateFormat.parse(dm.getExpired());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newDateFormat.applyPattern("dd MMM yyyy");
        String myDateString = newDateFormat.format(expdate);
        newDateFormat.setLenient(false);
        Date expiry = null;
        try {
            expiry = newDateFormat.parse(myDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean expired = Objects.requireNonNull(expiry).before(new Date());
        if (expired == true) {
            holder.tanggal.setTextColor(Color.parseColor("#E70B0B"));
            holder.tanggal.setText("Tidak Tersedia.");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tanggal.setText("Berlaku Sampai " + myDateString);
                holder.tanggal.setTextColor(Color.parseColor(BGColor));
            }

        }

        holder.dm = dm;

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView nama, point, reward, tipe, tanggal;
        Button btnTukar, btnKirim;
        PointModel dm;
        ImageView thumbnail, icontgl, iconuang;
        LinearLayout IdPoint;

        public HolderData(View v) {
            super(v);
            icontgl = (ImageView) v.findViewById(R.id.icwaktu);
            iconuang = (ImageView) v.findViewById(R.id.icduit);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            tanggal = (TextView) v.findViewById(R.id.tvTanggal);
            nama = (TextView) v.findViewById(R.id.tvNama);
            point = (TextView) v.findViewById(R.id.tvPoint);
            reward = (TextView) v.findViewById(R.id.tvReward);
            tipe = (TextView) v.findViewById(R.id.tvTipe);
            IdPoint = (LinearLayout) v.findViewById(R.id.IdPoint);
            IdPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dm.getType_promo().equals("point")) {
                        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date expdate = null;
                        try {
                            expdate = newDateFormat.parse(dm.getExpired());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        newDateFormat.applyPattern("dd MMM yyyy");
                        String myDateString = newDateFormat.format(expdate);
                        newDateFormat.setLenient(false);
                        Date expiry = null;
                        try {
                            expiry = newDateFormat.parse(myDateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        boolean expired = Objects.requireNonNull(expiry).before(new Date());
                        if (expired == true) {
                            Toast.makeText(ctx, "Sudah Tidak Tersedia.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Intent goInput = new Intent(ctx, ActivityPoint.class);
                            goInput.putExtra("nomor", dm.getId_promo());
                            goInput.putExtra("nama", dm.getNama_promo());
                            goInput.putExtra("point", dm.getPoint());
                            goInput.putExtra("reward", dm.getNominal_promo());
                            goInput.putExtra("tipe", dm.getType_promo());
                            goInput.putExtra("status", dm.getStatus());
                            ctx.startActivity(new Intent(goInput)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                    } else {
                        Toast.makeText(ctx, "Tipe Voucher Hanya Untuk App Customers.",
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}