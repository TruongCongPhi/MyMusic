package com.truongcongphi.mymusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Random;

public class PlaySongActivity extends AppCompatActivity {

    TextView tvAlbumName;
    TextView tvSongName;
    TextView tvSongEndTime;
    TextView tvSingerName;
    static TextView tvSongStartTime;
    static SeekBar seekBarTime;
    ImageButton imgRepeat, imgPlay, imgNext, imgPre, imgTym, imgRandom;
    ImageView imgSongBack;
    ViewPager viewPagerPlaySong;
    public static ArrayList<Song> songArrayList = new ArrayList<>();
    Song selectedSong;
    public static ViewPagerPlaylistSong adapterSong;
    FragmentSongBefore fragmentSongBefore;
    FragmentSongCurrent fragmentSongCurrent;
    FragmentSongLater fragmentSongLater;
    static MediaPlayer mediaPlayer;
    static int position = 0;
    boolean repeat = false;
    boolean checkRandom = false;
    static boolean next = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initView();
        getDataFromIntent();
        int numberOfSongs = songArrayList.size();
        String message = "Số phần tử trong mảng: " + numberOfSongs;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        resetBack();

        eventClick();

    }

    private void stopCurrentSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void resetBack() {
        imgSongBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                songArrayList.clear();
            }
        });

    }

    private void eventClick() {
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        imgPlay.setImageResource(R.drawable.icon_pause);
                    } else {
                        mediaPlayer.start();
                        imgPlay.setImageResource(R.drawable.icon_play);
                    }
                }
            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false) {
                    if (checkRandom == true) {
                        checkRandom = false;
                        imgRepeat.setImageResource(R.drawable.icon_repeat);
                        imgRandom.setImageResource(R.drawable.icon_random);
                    }
                    imgRepeat.setImageResource(R.drawable.icon_repeat);
                    repeat = true;
                } else {
                    imgRepeat.setImageResource(R.drawable.icon_repeat_black);
                    repeat = false;
                }
            }
        });

        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRandom == false) {
                    if (repeat == true) {
                        checkRandom = false;
                        imgRepeat.setImageResource(R.drawable.icon_repeat_black);
                        imgRandom.setImageResource(R.drawable.icon_ramdom_white);
                    }
                    imgRandom.setImageResource(R.drawable.icon_ramdom_white);
                    checkRandom = true;
                } else {
                    imgRandom.setImageResource(R.drawable.icon_random);
                    checkRandom = false;
                }

            }
        });

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size()>0){          //có bài hát đang phát thì dừng
                    if(mediaPlayer.isPlaying() || mediaPlayer !=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer=null;
                    }
                }
                if(position<(songArrayList.size())){
                    position++;
                    if(position>(songArrayList.size()-1)){
                        position=0;
                    }
                    playSong(position);
                    updateTimeSong();
                }


                imgPre.setClickable(false);
                imgNext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 5000);
            }
        });


        imgPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size()>0){          //có dữ liệu
                    if(mediaPlayer.isPlaying() || mediaPlayer !=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer=null;
                    }
                }
                if(position < songArrayList.size()){
                    position--;
                    if( position < 0){
                        position = songArrayList.size()-1;
                    }
                }
                new PlayMp3().execute(songArrayList.get(position).getUrl());
                tvSongName.setText(songArrayList.get(position).getSongName());
                imgPlay.setImageResource(R.drawable.icon_play);
                updateTimeSong();

                imgPre.setClickable(false);
                imgNext.setClickable(false);
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgPre.setClickable(true);
                        imgNext.setClickable(true);
                    }
                }, 5000);
            }
        });
    }

    private void kiemtra() {
        if (mediaPlayer.isPlaying()) {
            Toast.makeText(PlaySongActivity.this, "Đang phát", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PlaySongActivity.this, "Không phát", Toast.LENGTH_SHORT).show();
        }
    }

    class PlayMp3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PlaySongActivity", "Lỗi phát nhạc", e);
            }
            mediaPlayer.start();
            TimeSong();
            kiemtra();
            updateTimeSong();
        }
    }


    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        tvSongEndTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarTime.setMax(mediaPlayer.getDuration());
    }

    public void updateTimeSong(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
                    tvSongStartTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next=true;
                            try {
                                Thread.sleep(1000);//ngủ 1s rồi chuyển bài
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        },300);

        Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next==true){
                    if(position<(songArrayList.size())){
                        position++;
                        if(position>(songArrayList.size()-1)){
                            position=0;
                        }
                        new PlayMp3().execute(songArrayList.get(position).getUrl());

                    }
                    imgNext.setEnabled(false);// tránh người dùng click quá nhanh dẫn đến lỗi
                    imgPre.setEnabled(false);
                    Handler handler1=new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgNext.setEnabled(true);
                            imgPre.setEnabled(true);
                        }
                    },3000);
                    next=false;
                    handler1.removeCallbacks(this);
                }
                else{
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }

    public void playSong(int po) {
        if (songArrayList.size() > 0) {
            stopCurrentSong();
            new PlayMp3().execute(songArrayList.get(position).getUrl());
            tvSongName.setText(songArrayList.get(position).getSongName());
            imgPlay.setImageResource(R.drawable.icon_play);
        }
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("cacbaihat")) {
            songArrayList = intent.getParcelableArrayListExtra("cacbaihat");
        }
        if (intent.hasExtra("baihat")) {
            selectedSong = intent.getParcelableExtra("baihat");
        }
        if(intent.hasExtra("vitribaihat")){
            position=intent.getIntExtra("vitribaihat",0);
        }

    }

    private void initView() {
        tvAlbumName = findViewById(R.id.tv_album_name);
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
        imgRandom = findViewById(R.id.imgbtn_random);
        imgSongBack = findViewById(R.id.img_song_back);
        viewPagerPlaySong = findViewById(R.id.viewpager_play_song);

        fragmentSongBefore = new FragmentSongBefore();
        fragmentSongLater = new FragmentSongLater();
        fragmentSongCurrent = new FragmentSongCurrent();

        adapterSong = new ViewPagerPlaylistSong(getSupportFragmentManager());
        adapterSong.addFragment(fragmentSongBefore);
        adapterSong.addFragment(fragmentSongCurrent);
        adapterSong.addFragment(fragmentSongLater);
        viewPagerPlaySong.setAdapter(adapterSong);
        fragmentSongCurrent = (FragmentSongCurrent) adapterSong.getItem(1);
        playSong(position);

    }
}