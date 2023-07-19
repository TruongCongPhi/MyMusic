package com.truongcongphi.mymusic.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;

import com.truongcongphi.mymusic.R;

public class EditProfileFragment extends AppCompatActivity {
    ImageView img_avt;
    TextView txt_photo;
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
    }

    private void xuLyLayHinh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Chọn hình"), 111);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && requestCode == RESULT_OK){
            try {
                Uri imgURI = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(
                getContentResolver(),
                imgURI);
                img_avt.setImageBitmap(hinh);
            }
            catch (Exception ex){
                Log.e("loi", ex.toString());
            }
        }
    }

    private void addControls() {
        img_avt = (ImageView) findViewById(R.id.img_avt);
        txt_photo = (TextView)  findViewById(R.id.txt_photo);
    }
}
