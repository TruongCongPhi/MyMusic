package com.truongcongphi.mymusic.Adapter;


import static android.webkit.URLUtil.isValidUrl;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.truongcongphi.mymusic.Activity.ListSongActivity;
import com.truongcongphi.mymusic.ButtonAnimator.ButtonAnimator;
import com.truongcongphi.mymusic.Class.Artist;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<Artist> listArtist;

    public void setData(List<Artist> list) {
        this.listArtist = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = listArtist.get(position);
        if (artist == null) {
            return;
        }

        // Nếu URL hợp lệ, hiển thị ảnh từ URL bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(artist.getImgURL())
                .apply(new RequestOptions()
                        .transform(new CenterCrop())
                        .transform(new RoundedCorners(1000)))
                .error(R.drawable.music_note)
                .into(holder.imgItem);


        // Hiển thị tên ca sĩ
        holder.tvArtistName.setText(artist.getName());
        holder.tvArtistName.setTextColor(Color.WHITE);


    }

    @Override
    public int getItemCount() {
        if (listArtist != null) {
            return listArtist.size();
        }
        return 0;
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvArtistName;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
            tvArtistName = itemView.findViewById(R.id.tv_tilte1);
            LinearLayout linearLayout = itemView.findViewById(R.id.linear_layout);
            linearLayout.setGravity(Gravity.CENTER);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ListSongActivity.class);
                    intent.putExtra("artist", listArtist.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);

                }
            });


        }
    }


}
