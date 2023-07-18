package com.truongcongphi.mymusic.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private boolean checkImage = false;
    private boolean isImageUpdated = false;

    private ImageView imageView;
    private TextView txt_photo, tvSave;
    private ImageButton ic_exit;
    private SessionManager sessionManager;
    private EditText edtName;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    private Uri selectedImageUri;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        addControls();
        loadUserData();
        addEvents();
    }

    private void addEvents() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseSourceDialog();
            }
        });
        txt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseSourceDialog();
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

    private void showChooseSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");

        boolean hasImage = (imageView.getDrawable() != null);

        if (hasImage) {
            builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh từ thư viện", "Xóa ảnh"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if (checkCameraPermission()) {
                            dispatchTakePictureIntent();
                        } else {
                            requestCameraPermission();
                        }
                    } else if (which == 1) {
                        if (checkStoragePermission()) {
                            dispatchPickImageIntent();
                        } else {
                            requestStoragePermission();
                        }
                    } else if (which == 2) {
                        deleteImage();
                    }
                }
            });
        } else {
            builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh từ thư viện"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if (checkCameraPermission()) {
                            dispatchTakePictureIntent();
                        } else {
                            requestCameraPermission();
                        }
                    } else if (which == 1) {
                        if (checkStoragePermission()) {
                            dispatchPickImageIntent();
                        } else {
                            requestStoragePermission();
                        }
                    }
                }
            });
        }

        builder.show();
    }

    private void deleteImage() {
        imageView.setImageResource(R.drawable.ic_user);
        selectedImageUri = null;
        checkImage = true;
    }

    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStoragePermission() {
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng Camera.", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchPickImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình"), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                    selectedImageUri = null; // Set selectedImageUri to null if using camera capture
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    selectedImageUri = data.getData();
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Không được cấp quyền truy cập Camera.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickImageIntent();
            } else {
                Toast.makeText(this, "Không được cấp quyền truy cập bộ nhớ.", Toast.LENGTH_SHORT).show();
            }
        }
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
                            Glide.with(EditProfileActivity.this).load(image).into(imageView);
                        }
                        if (name != null) {
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
                                Glide.with(EditProfileActivity.this).load(sessionManager.getImage()).into(imageView);

                                userRef.child("imageUser").setValue(imageDownloadUrl);
                                isImageUpdated = true;
                                checkAndCloseActivity();
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

    private void saveProfile() {
        if (selectedImageUri != null) {
            uploadImageToFirebase(selectedImageUri);
        }
        if (edtName.getText() != null) {
            uploadNameToFirebase();
        }
        if(checkImage == true){
            userRef.child("imageUser").setValue(null);
            sessionManager.saveUserImage(null);
            isImageUpdated = true;
        }
        checkAndCloseActivity();
    }
    private void checkAndCloseActivity() {
        if (isImageUpdated) {
            // Hiển thị Toast
            Toast.makeText(EditProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private void addControls() {
        imageView = findViewById(R.id.img_avt);
        txt_photo = findViewById(R.id.txt_photo);
        ic_exit = findViewById(R.id.ic_exit);
        tvSave = findViewById(R.id.txt_save);
        edtName = findViewById(R.id.edt_name);
        sessionManager = new SessionManager(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}
