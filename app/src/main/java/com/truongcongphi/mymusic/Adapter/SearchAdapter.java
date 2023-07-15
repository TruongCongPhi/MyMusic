package com.truongcongphi.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static Context context;
    private static ArrayList<Song> listSong;
    private ArrayList<Song> filteredList;

    public SearchAdapter(ArrayList<Song> listSong) {
        this.listSong = listSong;
        this.filteredList = new ArrayList<>(listSong);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = filteredList.get(position);
        holder.itemTitle1.setText(song.getSongName());
        holder.itemTitle2.setText(TextUtils.join(", ", song.getSingerName()));
        Glide.with(holder.itemView.getContext())
                .load(song.getImageSong())
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView itemTitle1, itemTitle2;

        public ViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_song);
            itemTitle1 = itemView.findViewById(R.id.tv_song_name);
            itemTitle2 = itemView.findViewById(R.id.tv_singer_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song=listSong.get(getAdapterPosition());
                    listSong.add(song);
                    int vitri=getAdapterPosition();
                    Intent intent=new Intent(context, PlaySongActivity.class);
                    intent.putExtra("vitribaihat",vitri);
                    intent.putExtra("cacbaihat",listSong);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setFilteredList(ArrayList<Song> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}
