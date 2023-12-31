package com.truongcongphi.mymusic.Adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Fragment.MyBottomSheetDialogSongFragment;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ArrayList<Song> mSongs;
    String playlistName;
    public void setData(ArrayList<Song> listArrSong, String playlistName) {
        this.mSongs = listArrSong;
        this.playlistName = playlistName;
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
        List<String> topName = new ArrayList<>();
        topName.add("Top 50 Việt Nam");
        topName.add("Top 50 Toàn cầu");
        topName.add("Top 50 Korea");
        topName.add("Top hits 2023");
        if(topName.contains(playlistName)){
            holder.tvBXH.setVisibility(View.VISIBLE);
            holder.tvBXH.setText(String.valueOf(position + 1));
        }


        holder.tvSongName.setText(song.getSongName());
        holder.tvSingerName.setText(TextUtils.join(", ", song.getSingerName()));

        Glide.with(holder.itemView.getContext())
                .load(song.getImageSong())
                .error(R.drawable.music_note)
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
        private ImageView imgSong, songOptions;
        private TextView tvSongName, tvSingerName, tvBXH;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
            songOptions = itemView.findViewById(R.id.song_options);
            tvBXH = itemView.findViewById(R.id.tv_bxh);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vitri = getAdapterPosition();
                    Song song = mSongs.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
                    intent.putParcelableArrayListExtra("cacbaihat", mSongs);
                    intent.putExtra("baihat", song);
                    intent.putExtra("vitribaihat", vitri);
                    intent.putExtra("playlistname",playlistName);

                    v.getContext().startActivity(intent);

                    // Show notification when a song is played
                }
            });

            songOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Song song = mSongs.get(getAdapterPosition());
                    MyBottomSheetDialogSongFragment bottomSheetDialog = MyBottomSheetDialogSongFragment.newInstance(song);
                    bottomSheetDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetDialog.getTag());
                }
            });
        }
    }

}
