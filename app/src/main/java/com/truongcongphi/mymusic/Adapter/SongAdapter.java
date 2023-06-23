package com.truongcongphi.mymusic.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> mSongs;

    public void setData(List<Song> list) {
        this.mSongs = list;
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
        if (song == null) {
            return;
        }
        holder.tvSongName.setText(song.getSongName());
        holder.tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));
//        holder.tvDuration.setText(song.getDuration());
//        holder.tvLike.setText(song.getLike());
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
        private TextView tvSongName, tvSingerName, tvDuration, tvLike;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
//            tvDuration = itemView.findViewById(R.id.tv_duration);
//            tvLike = itemView.findViewById(R.id.tv_like);
        }
    }
}
