package com.truongcongphi.mymusic.Adapter;

import static com.truongcongphi.mymusic.Activity.MyNotification.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Fragment.MyBottomSheetDialogSongFragment;
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
        private ImageView imgSong, songOptions;
        private TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
            songOptions = itemView.findViewById(R.id.song_options);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int vitri = getAdapterPosition();
                    Song song = mSongs.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), PlaySongActivity.class);
                    intent.putParcelableArrayListExtra("cacbaihat", mSongs);
                    intent.putExtra("baihat", song);
                    intent.putExtra("vitribaihat", vitri);
                    v.getContext().startActivity(intent);
                    Bitmap bitmap = BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.ic_user);

                    MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(v.getContext(), "tag");

                    Notification notification = new NotificationCompat.Builder(v.getContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.musicnote)
                            .setContentTitle("Tên Bài Hát")
                            .setContentText("Tên ca sĩ")
                            .setLargeIcon(bitmap)
                            .addAction(R.drawable.icon_previous, "Previous", null)
                            .addAction(R.drawable.icon_play, "Play", null)
                            .addAction(R.drawable.icon_skip_next, "Next", null)
                            .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(1 /* #1: pause button */)
                                    .setMediaSession(mediaSessionCompat.getSessionToken()))
                            .build();

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(v.getContext());
                    managerCompat.notify(1, notification);
                    Toast.makeText(v.getContext(),"đã chạy",Toast.LENGTH_SHORT).show();
                }
            });
            songOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int vitri = getAdapterPosition();
                    Song song = mSongs.get(getAdapterPosition());
                    MyBottomSheetDialogSongFragment bottomSheetDialog = MyBottomSheetDialogSongFragment.newInstance(song);
                    bottomSheetDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), bottomSheetDialog.getTag());
                }
            });

        }
    }
    private void sendNotificationMedia() {



    }


}
