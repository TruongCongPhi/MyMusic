package com.truongcongphi.mymusic.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.truongcongphi.mymusic.R;

public class EditProfileActivity extends AppCompatActivity {
    public static final int CAMERA_ACTION_CODE = 1;
    ImageView img_avt;
    TextView txt_photo;
    ImageButton ic_exit;
    Button btn_chonanh, btn_chupanh;
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
                final Dialog dialog = new Dialog (EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_feeback);
                dialog.show();

                btn_chonanh = dialog.findViewById(R.id.btn_chonanh);
                btn_chupanh = dialog.findViewById(R.id.btn_chupanh);

                btn_chonanh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xuLyLayHinh();
                        dialog.cancel();
                    }
                });
            }
        });
        txt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog (EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_feeback);
                dialog.show();

                btn_chonanh = dialog.findViewById(R.id.btn_chonanh);
                btn_chupanh = dialog.findViewById(R.id.btn_chupanh);

                btn_chonanh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xuLyLayHinh();
                        dialog.cancel();
                    }
                });
                btn_chupanh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        xuLyChupHinh();
                    }
                });
            }
        });


        ic_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void xuLyChupHinh() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAMERA_ACTION_CODE);
        }else {
            Toast.makeText(EditProfileActivity.this, "There is no app that suport this action", Toast.LENGTH_SHORT).show();

        }
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
        if(requestCode == CAMERA_ACTION_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap Photo = (Bitmap) bundle.get("data");
            img_avt.setImageBitmap(Photo);
        }
    }

    private void addControls() {
        img_avt = (ImageView) findViewById(R.id.img_avt);
        txt_photo = (TextView)  findViewById(R.id.txt_photo);
        ic_exit = (ImageButton) findViewById(R.id.ic_exit);

    }
}
