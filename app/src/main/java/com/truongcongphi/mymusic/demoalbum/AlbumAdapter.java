package com.truongcongphi.mymusic.demoalbum;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> mAlbums;

    public void setData(List<Album> list){
        this.mAlbums = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = mAlbums.get(position);
        if(album == null){
            return;
        }
        Glide.with(holder.itemView.getContext())
                .load(album.getAlbumURL())
                .into(holder.imgAlbum);
        holder.tvAlbumName.setText(album.getAlbumName());
        holder.tvSingerName.setText(album.getSingerName());
    }

    @Override
    public int getItemCount() {
        if(mAlbums != null){
            return mAlbums.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView tvAlbumName, tvSingerName;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_album);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);

        }
    }
}
