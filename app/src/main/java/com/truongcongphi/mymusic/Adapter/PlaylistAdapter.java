package com.truongcongphi.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.ListSongActivity;
import com.truongcongphi.mymusic.Class.PlayList;
import com.truongcongphi.mymusic.Class.Top;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHoler> {
    Context context;
    ArrayList<PlayList> playLists;
    public PlaylistAdapter(Context context, ArrayList list) {
        this.context = context;
        this.playLists = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public PlaylistViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song,parent,false);
        return new PlaylistViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHoler holder, int position) {
        PlayList playList = playLists.get(position);
        if(playList == null){
            return;
        }
        Glide.with(context).load(playList.getImg()).into(holder.imgPlaylist);
        holder.tvPlaylist.setText(playList.getName());

    }

    @Override
    public int getItemCount() {
        if(playLists != null){
            return playLists.size();
        }
        return 0;
    }
    public void setData(ArrayList<PlayList> playLists) {
        this.playLists = playLists;
        notifyDataSetChanged();
    }

    public class PlaylistViewHoler extends RecyclerView.ViewHolder{
        private ImageView imgPlaylist;
        private TextView tvPlaylist;

        public PlaylistViewHoler(@NonNull View itemView) {
            super(itemView);
            imgPlaylist = itemView.findViewById(R.id.img_song);
            tvPlaylist = itemView.findViewById(R.id.tv_song_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
