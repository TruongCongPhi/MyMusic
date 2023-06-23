package com.truongcongphi.mymusic.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.R;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<Artist> mArtist;

    public void setData(List<Artist> list){
        this.mArtist = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = mArtist.get(position);
        if (artist == null) {
            return;
        }

        // Hiển thị ảnh ca sĩ
        Glide.with(holder.itemView.getContext())
                .load(artist.getImgURL())
                .into(holder.imgAlbum);

        // Hiển thị tên ca sĩ
        holder.tvSingerName.setText(artist.getName());
    }

    @Override
    public int getItemCount() {
        if(mArtist != null){
            return mArtist.size();
        }
        return 0;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView tvAlbumName, tvSingerName;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_album);
            tvAlbumName = itemView.findViewById(R.id.tv_album_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);

        }
    }


}
