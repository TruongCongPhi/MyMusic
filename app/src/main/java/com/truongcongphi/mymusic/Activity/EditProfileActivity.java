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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.Class.User;
import com.truongcongphi.mymusic.R;

public class EditProfileActivity extends AppCompatActivity {
    ImageView img_avt;
    TextView txt_photo,tvSave;
    ImageButton ic_exit;
    private SessionManager sessionManager;


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
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String image = dataSnapshot.child("imageUser").getValue(String.class);
                                User user1;
                                user1 = sessionManager.getLoggedInUser();
                                sessionManager.saveUserCredentials(null, null, image, null);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                        }
                    });
                }

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
        if (requestCode == 113 && resultCode == RESULT_OK) {
            try {
                Uri imgURI = data.getData();
                Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);
                img_avt.setImageBitmap(hinh);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference("avatars");
                // Tạo tham chiếu đến tệp tin trong Firebase Storage
                StorageReference imageRef = storageRef.child("avatars" + user.getUid());

                // Tải lên hình ảnh lên Firebase Storage
                UploadTask uploadTask = imageRef.putFile(imgURI);

                // Lắng nghe sự kiện khi tải lên hoàn thành
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lấy đường dẫn tải xuống của hình ảnh từ Firebase Storage
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String imageDownloadUrl = downloadUri.toString();

                                    // Lưu đường dẫn hình ảnh vào Firebase Realtime Database
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("users").child(user.getUid()).child("imageUser").setValue(imageDownloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Lưu thành công
                                                        Log.d("EditProfileActivity", "Đã lưu đường dẫn hình ảnh vào cơ sở dữ liệu.");
                                                    } else {
                                                        // Lưu thất bại
                                                        Log.e("EditProfileActivity", "Lỗi khi lưu đường dẫn hình ảnh: " + task.getException().getMessage());
                                                    }
                                                }
                                            });
                                }
                            });
                        } else {
                            // Tải lên thất bại
                            Log.e("EditProfileActivity", "Lỗi khi tải lên hình ảnh: " + task.getException().getMessage());
                        }
                    }
                });
            } catch (Exception ex) {
                Log.e("Error", ex.toString());
            }
        }
    }

    private void addControls() {
        img_avt = (ImageView) findViewById(R.id.img_avt);
        txt_photo = (TextView)  findViewById(R.id.txt_photo);
        ic_exit = (ImageButton) findViewById(R.id.ic_exit);
        tvSave = findViewById(R.id.txt_save);
        sessionManager = new SessionManager(this);
    }
}
