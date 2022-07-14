package com.radjago.drivergo.item;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.radjago.drivergo.R;
import com.radjago.drivergo.fragment.InfoItem;
import com.radjago.drivergo.models.ItemPesananModel;

/**
 * Created by ourdevelops team on 3/24/2019.
 */

public class ItemPesananItem extends RecyclerView.Adapter<ItemPesananItem.ItemRowHolder> implements RecyclerView.OnItemTouchListener {
    GestureDetector mGestureDetector;
    String foods, layanan, transaksi, jumlah, response;
    private Context ctx;
    private OnItemClickListener mListener;
    private List<ItemPesananModel> dataList;
    private int rowLayout;

    public ItemPesananItem(List<ItemPesananModel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.rowLayout = rowLayout;

    }

    public ItemPesananItem(Context context, RecyclerView rvmerchantnear, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    private static String formatRupiah(Double number) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final ItemPesananModel singleItem = dataList.get(position);
        holder.name.setText(singleItem.getNama_item() + " (" + singleItem.getJumlah_item() + ")");
        holder.qty.setText(singleItem.getJumlah_item());
        Double getprice = Double.valueOf(singleItem.getTotal_harga());
        String zFormat = formatRupiah(getprice);
        String ValFormat = zFormat.replaceAll(",00", "");
        String IdMenu = singleItem.getId_item();
        holder.harga.setText(ValFormat);
        holder.itemView.setTag(position);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFoods(IdMenu);
                String GetRespon = getResponse();
                if (IdMenu != null) {
                    // LoadMenu(idMenu);
                    if (GetRespon.equals("2")) {
                        InfoItem popUpClass = new InfoItem();
                        popUpClass.showPopupWindow(view, IdMenu, getLayanan(), getTransaksi(), singleItem.getJumlah_item());
                    }


                }
                notifyDataSetChanged();
            }
        });
       /* holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position);
            }
        });
*/
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
        this.foods = foods;
    }

    public String getLayanan() {
        return layanan;
    }

    public void setLayanan(String layanan) {
        this.layanan = layanan;
    }

    public String getTransaksi() {
        return transaksi;
    }

    public void setTransaksi(String transaksi) {
        this.transaksi = transaksi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private void deleteItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView name, qty, harga;
        ImageView remove;

        ItemRowHolder(View itemView) {
            super(itemView);
            harga = itemView.findViewById(R.id.harga);
            qty = itemView.findViewById(R.id.qty);
            name = itemView.findViewById(R.id.namaitem);
        }
    }
}
