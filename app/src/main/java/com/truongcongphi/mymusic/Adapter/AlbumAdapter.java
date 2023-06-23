package com.truongcongphi.mymusic.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Activity.ListAlbumActivity;
import com.truongcongphi.mymusic.Class.Album;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

import java.io.Serializable;
import java.util.ArrayList;
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

        // áp dụng hiệu ứng chạm


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

            // HIỆU ỨNG ITEM
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ListAlbumActivity.class);
                                        intent.putExtra("songs", (CharSequence) mAlbums.get(getAdapterPosition()));

                                        // Khởi chạy Activity mới
                                        v.getContext().startActivity(intent);
//                    Log.d("AlbumViewHolder", "onClick");
//
////                     Lấy ra album tương ứng với vị trí của itemView
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//
//                        // Lấy ra album tương ứng với vị trí của itemView
//                        Album album = mAlbums.get(position);
//
//
//
//
//                        // Truy xuất cơ sở dữ liệu Firebase Realtime để lấy ra tất cả các bài hát có cùng albumID
//                        DatabaseReference songsRef = FirebaseDatabase.getInstance().getReference("songs");
//                        songsRef.orderByChild("albumID").equalTo(album.getAlbumID())
//                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        List<Song> songs = new ArrayList<>();
//                                        for (DataSnapshot child : snapshot.getChildren()) {
//                                            Song song = child.getValue(Song.class);
//                                            songs.add(song);
//                                        }
//
//                                        // Khởi tạo một Intent để khởi chạy Activity mới
//                                        Intent intent = new Intent(v.getContext(), ListAlbumActivity.class);
//                                        intent.putExtra("songs", (Serializable) songs);
//
//                                        // Khởi chạy Activity mới
//                                        v.getContext().startActivity(intent);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                    }else {
//                        // Hiển thị thông báo bằng Toast
//                        Toast.makeText(itemView.getContext(), "Không thể lấy được vị trí của ViewHolder", Toast.LENGTH_SHORT).show();
//                    }
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Thu nhỏ và làm mờ itemView khi chạm
                            v.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.5f).setDuration(200).start();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // Đưa itemView trở lại kích thước và độ trong suốt ban đầu khi thả ra
                            v.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(200).start();
                            break;
                    }
                    return true; // Chỉ ra rằng sự kiện chạm đã được xử lý
                }
            });






        }
    }
}
