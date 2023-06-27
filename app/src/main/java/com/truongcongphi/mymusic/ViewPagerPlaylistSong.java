package com.truongcongphi.mymusic;

import static android.webkit.URLUtil.isValidUrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import com.truongcongphi.mymusic.Class.Song;

import java.util.ArrayList;


public class ViewPagerPlaylistSong extends PagerAdapter {
    ArrayList<Song> listSong;
    int positionSong;
    Context context;
    private LayoutInflater inflater;
        public ViewPagerPlaylistSong(Context context, ArrayList<Song> songs, int vitri){
            this.context = context;
            this.listSong = songs;
             this.inflater = LayoutInflater.from(context);
             this.positionSong = vitri;
        }

        @Override
        public int getCount() {
            return listSong.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.item_play_song, container, false);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = view.findViewById(R.id.img_song_play2);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = view.findViewById(R.id.tv_tieude);

            boolean isValidUrl = isValidUrl(listSong.get(position).getImageSong());
            if (isValidUrl) {
                // Nếu URL hợp lệ, hiển thị ảnh từ URL bằng Glide
                Glide.with(context)
                        .load(listSong.get(position).getImageSong())
                        .into(imageView);
            } else {
                // Nếu URL không hợp lệ, hiển thị ảnh từ Drawable
                imageView.setImageResource(R.drawable.music_note);
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }




