package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.Fragment.FragmentSongBefore;
import com.truongcongphi.mymusic.Fragment.FragmentSongCurrent;
import com.truongcongphi.mymusic.Fragment.FragmentSongLater;
import com.truongcongphi.mymusic.R;
import com.truongcongphi.mymusic.ViewPagerPlaylistSong;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlaySongActivity extends AppCompatActivity {
    Toolbar toolbarPlaySong;
    TextView tvSongName;
    TextView tvSingerName;
    TextView tvSongStartTime;
    TextView tvSongEndTime;
   SeekBar seekBarTime;
    ImageButton imgRepeat, imgPlay, imgNext, imgPre, imgTym;
    ViewPager viewPagerPlaySong;
    public static ArrayList<Song> songArrayList = new ArrayList<>();
    public static ViewPagerPlaylistSong adapterSong;
    FragmentSongBefore fragmentSongBefore;
    FragmentSongCurrent fragmentSongCurrent;
    FragmentSongLater fragmentSongLater;
    static MediaPlayer mediaPlayer;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        initView();
        getDataFromIntent();
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);


//        playSong();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        String url = songArrayList.get(0).getUrl();
//        Toast.makeText(PlaySongActivity.this, url, Toast.LENGTH_LONG).show();

    }
//    class PlayMp3 extends AsyncTask<String,Void,String>{
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//
//        @Override
//        protected void onPostExecute(String baihat) {
//            super.onPostExecute(baihat);
//            try {
//                mediaPlayer=new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//                mediaPlayer.setDataSource(baihat);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.start();
//            TimeSong();
//
//
//        }
//    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
        tvSongEndTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarTime.setMax(mediaPlayer.getDuration());

    }
//    private void playSong() {
//
//            new PlayMp3().execute(url);
//            imgPlay.setImageResource(R.drawable.icon_pause);
////
//
//
//    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("bai_hat")) {
            Song song = intent.getParcelableExtra("bai_hat");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("songs");
            databaseReference.child(song.getSongID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (song != null) {
                        songArrayList.add(song);
                        for (Song song : songArrayList) {
                             url = song.getUrl();
                            Toast.makeText(PlaySongActivity.this,url,Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi tại đây
                }
            });
        }
    }

    private void initView() {
        toolbarPlaySong = findViewById(R.id.toolbar_play_Song);
        tvSongName = findViewById(R.id.tv_play_song_name);
        tvSingerName = findViewById(R.id.tv_song_singer_name);
        tvSongEndTime = findViewById(R.id.tv_song_end_time);
        tvSongStartTime = findViewById(R.id.tv_song_start_time);
        seekBarTime = findViewById(R.id.seekbar_time_Song);
        imgRepeat = findViewById(R.id.imgbtn_repeat_song);
        imgPlay = findViewById(R.id.imgbtn_play_Song);
        imgNext = findViewById(R.id.img_next_song);
        imgPre = findViewById(R.id.imgbtn_pre_Song);
        imgTym = findViewById(R.id.imgbtn_tym);
        viewPagerPlaySong = findViewById(R.id.viewpager_play_song);

        fragmentSongBefore = new FragmentSongBefore();
        fragmentSongLater = new FragmentSongLater();
        fragmentSongCurrent = new FragmentSongCurrent();

        adapterSong = new ViewPagerPlaylistSong(getSupportFragmentManager());

        adapterSong.addFragment(fragmentSongBefore);
        adapterSong.addFragment(fragmentSongLater);
        adapterSong.addFragment(fragmentSongCurrent);
        viewPagerPlaySong.setAdapter(adapterSong);

    }

}
