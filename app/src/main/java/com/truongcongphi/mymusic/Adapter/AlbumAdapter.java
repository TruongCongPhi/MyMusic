package com.truongcongphi.mymusic.Adapter;

import static android.webkit.URLUtil.isValidUrl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.truongcongphi.mymusic.Activity.ListSongActivity;

import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Class.Album;

import com.truongcongphi.mymusic.Fragment.AlbumFragment;
import com.truongcongphi.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    Context context;
    ArrayList<Album> listAlbum;

    public AlbumAdapter(Context context, ArrayList listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;

    }




    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home,parent,false);



        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = listAlbum.get(position);
        if(album == null){
            return;
        }

        boolean isValidUrl = isValidUrl(album.getAlbumURL());

        if (isValidUrl) {
            // Nếu URL hợp lệ, hiển thị ảnh từ URL bằng Glide
            Glide.with(holder.itemView.getContext())
                    .load(album.getAlbumURL())
                    .into(holder.imgAlbum);
        } else {
            // Nếu URL không hợp lệ, hiển thị ảnh từ Drawable
            holder.imgAlbum.setImageResource(R.drawable.music_note);
        }
        holder.tvAlbumName.setText(album.getAlbumName());
        holder.tvSingerName.setText(album.getSingerName());
    }

    @Override
    public int getItemCount() {
        if(listAlbum != null){
            return listAlbum.size();
        }
        return 0;
    }
    public void setData(ArrayList<Album> albums) {

        this.listAlbum = albums;
        notifyDataSetChanged();
    }


    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgAlbum;
        private TextView tvAlbumName, tvSingerName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.img_item);
            tvAlbumName = itemView.findViewById(R.id.tv_tilte1);
            tvSingerName = itemView.findViewById(R.id.tv_tilte2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ListSongActivity.class);
                        // Gắn dữ liệu album vào Intent
                        intent.putExtra("album", listAlbum.get(getAdapterPosition()));
                        v.getContext().startActivity(intent);
                    }
            });

//             hiệu ứng item
//            itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            // Thu nhỏ và làm mờ itemView khi chạm
//                            v.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.5f).setDuration(200).start();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
//                            // Đưa itemView trở lại kích thước và độ trong suốt ban đầu khi thả ra
//                            v.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
//                            break;
//                    }
//                    return true; // Chỉ ra rằng sự kiện chạm đã được xử lý
//                }
//            });
        }
    }

}



