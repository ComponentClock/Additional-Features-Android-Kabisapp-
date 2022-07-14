package slot.play.cuan88;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.play.asianbet.R;

import java.util.ArrayList;

public class ListGameAdapter extends RecyclerView.Adapter<ListGameAdapter.ListViewHolder> {
    private ArrayList<Game> listGame;

    public ListGameAdapter(ArrayList<Game> list) {
        this.listGame = list;
    }

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Game game = listGame.get(position);
        Glide.with(holder.itemView.getContext())
                .load(game.getPhoto())
                .into(holder.image);
        holder.tvJudul.setText(game.getJudul());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listGame.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGame.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Game data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvJudul;
        ListViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvJudul = itemView.findViewById(R.id.namagame);
        }
    }
}
