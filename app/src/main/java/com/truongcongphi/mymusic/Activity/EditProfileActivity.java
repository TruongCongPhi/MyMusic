package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.truongcongphi.mymusic.R;

public class EditProfileActivity extends AppCompatActivity {
    ImageView img_avt;
    TextView txt_photo;
    ImageButton ic_exit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        addControls();
        addEvents();
    }

    private void addEvents() {
        img_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLayHinh();
            }
        });
        txt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLayHinh();
            }
        });

        ic_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void xuLyLayHinh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"chọn hình"), 113);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 113 && resultCode == RESULT_OK){
            try {
                Uri imgURI = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        imgURI);
                img_avt.setImageBitmap(hinh);
            }
            catch (Exception ex)
            {
                Log.e("Error", ex.toString());
            }
        }
    }

    private void addControls() {
        img_avt = (ImageView) findViewById(R.id.img_avt);
        txt_photo = (TextView)  findViewById(R.id.txt_photo);
        ic_exit = (ImageButton) findViewById(R.id.ic_exit);
    }
}
