package slot.play.cuan88.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.play.asianbet.R;
import slot.play.cuan88.WebviewActivity;
import slot.play.cuan88.constanst.Constants;
import slot.play.cuan88.models.MenuModel;
import slot.play.cuan88.utils.PicassoTrustAll;

import java.util.List;

public class MenuItem extends RecyclerView.Adapter<MenuItem.ItemRowHolder>{

    private List<MenuModel> dataList;
    private Context mContext;
    private int rowLayout;

    public MenuItem(Context context, List<MenuModel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;
    }

    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final MenuModel singleItem = dataList.get(position);
        holder.namagame.setText(singleItem.getNama_menu());
        PicassoTrustAll.getInstance(mContext)
                .load(Constants.IMAGESGAME + singleItem.getIcon())
                .resize(100, 100)
                .into(holder.image);

        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, WebviewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("url", singleItem.getLink());
                mContext.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView namagame;
        ImageView image;
        CardView background;

        ItemRowHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            image = itemView.findViewById(R.id.image);
            namagame = itemView.findViewById(R.id.namagame);
        }
    }
}
