package com.truongcongphi.mymusic.Adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<Song> mSongs;
    public void setData(ArrayList<Song> listArrSong) {
        this.mSongs = listArrSong;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mSongs.get(position);
        holder.tvSongName.setText(song.getSongName());
        holder.tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));
        Glide.with(holder.itemView.getContext())
                .load(song.getImageSong())
                .into(holder.imgSong);
    }

    @Override
    public int getItemCount() {
        if (mSongs != null) {
            return mSongs.size();
        }
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSong;
        private TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vitri = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
                    intent.putParcelableArrayListExtra("cacbaihat", mSongs);
                    intent.putExtra("vitribaihat",vitri);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}
