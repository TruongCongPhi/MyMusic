package com.truongcongphi.mymusic.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.truongcongphi.mymusic.R;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 113;

    private ImageView img_avt;
    private TextView txt_photo, tvSave;
    private ImageButton ic_exit;
    private SessionManager sessionManager;
private EditText edtName;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        addControls();
        loadUserData();
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
                saveProfile();
            }
        });
    }

    private void loadUserData() {

        if (currentUser != null) {
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.child("imageUser").getValue(String.class);
                        String name = dataSnapshot.child("name").getValue(String.class);
                        if (image != null) {
                            Glide.with(EditProfileActivity.this).load(image).into(img_avt);
                        }
                        if(name != null){
                            edtName.setText(name);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi truy cập cơ sở dữ liệu
                }
            });
        }
    }

    private void xuLyLayHinh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap hinh = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    img_avt.setImageBitmap(hinh);
                } catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        }
    }


    private void uploadImageToFirebase(Uri imageUri) {
        if (currentUser != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("avatars");
            StorageReference imageRef = storageRef.child("avatars" + currentUser.getUid());

            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                String imageDownloadUrl = downloadUri.toString();
                                sessionManager.saveUserImage(imageDownloadUrl);
                                userRef.child("imageUser").setValue(imageDownloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Glide.with(EditProfileActivity.this).load(imageDownloadUrl).into(img_avt);
                                                } else {
                                                    Toast.makeText(EditProfileActivity.this, "Cập nhật hình ảnh thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Lỗi khi tải lên hình ảnh", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfileActivity", "Lỗi khi tải lên hình ảnh: " + task.getException().getMessage());
                    }
                }
            });
        }
    }


    private void saveProfile() {
        if (selectedImageUri != null) {
            uploadImageToFirebase(selectedImageUri);
        }
        if(edtName.getText() !=null){
            uploadNameToFirebase();
        }
        Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();



    }

    private void uploadNameToFirebase() {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = String.valueOf(edtName.getText());
                    sessionManager.saveNameImage(name);
                    userRef.child("name").setValue(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy cập cơ sở dữ liệu
            }
        });
    }

    private void addControls() {
        img_avt = findViewById(R.id.img_avt);
        txt_photo = findViewById(R.id.txt_photo);
        ic_exit = findViewById(R.id.ic_exit);
        tvSave = findViewById(R.id.txt_save);
        edtName = findViewById(R.id.edt_name);
        sessionManager = new SessionManager(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}
