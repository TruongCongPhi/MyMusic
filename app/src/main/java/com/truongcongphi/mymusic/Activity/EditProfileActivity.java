package com.truongcongphi.mymusic.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.truongcongphi.mymusic.Class.LoadingDialog;
import com.truongcongphi.mymusic.Class.SessionManager;
import com.truongcongphi.mymusic.R;

public class EditProfileActivity extends AppCompatActivity {

    private boolean checkImage = false;
    private boolean isProfileUpdated = false;

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
    LoadingDialog loadingDialog;
    String nameCurrent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        loadingDialog = new LoadingDialog(this);
        addControls();
        loadUserData();

        addEvents();
    }

    private void addEvents() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện thay đổi trước khi người dùng nhập
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra xem người dùng đã thay đổi EditText hay chưa
                String newName = s.toString().trim();
                if (!newName.equals(sessionManager.getName())) {
                    isProfileUpdated = true;
                } else {
                    isProfileUpdated = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện thay đổi sau khi người dùng nhập
            }
        });

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
                if (isProfileUpdated ) {
                    saveProfile();
                } else {
                    // Hiển thị thông báo cho người dùng rằng không có gì để cập nhật
                    Toast.makeText(EditProfileActivity.this, "Không có gì để cập nhật", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showChooseSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh thay đổi hồ sơ");
        boolean hasImage = (imageView.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_user).getConstantState());

        if (hasImage) {
            builder.setItems(new CharSequence[]{"Chụp ảnh", "Chọn ảnh từ thư viện", "Xóa ảnh"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if (kiemTraCapQuyenCamera()) {
                            chupAnh();
                        } else {
                            yeuCauCapQuyenCamera();
                        }
                    } else if (which == 1) {
                        if (kiemTraCapQuyenBoNho()) {
                            layAnhThuVien();
                        } else {
                            yeuCauCapQuyenBoNho();
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
                        if (kiemTraCapQuyenCamera()) {
                            chupAnh();
                        } else {
                            yeuCauCapQuyenCamera();
                        }
                    } else if (which == 1) {
                        if (kiemTraCapQuyenBoNho()) {
                            layAnhThuVien();
                        } else {
                            yeuCauCapQuyenBoNho();
                        }
                    }
                }
            });
        }

        builder.show();
    }

    private void deleteImage() {
        isProfileUpdated=true;
        imageView.setImageResource(R.drawable.ic_user);
        selectedImageUri = null;
        checkImage = true;

    }

    private boolean kiemTraCapQuyenCamera() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private boolean kiemTraCapQuyenBoNho() {
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return storagePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void yeuCauCapQuyenCamera() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private void yeuCauCapQuyenBoNho() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
    }

    private void chupAnh() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            isProfileUpdated = true;
        } else {
            Toast.makeText(this, "Không tìm thấy ứng dụng Camera.", Toast.LENGTH_SHORT).show();
        }
    }

    private void layAnhThuVien() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn hình"), REQUEST_IMAGE_PICK);
        isProfileUpdated = true;
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
                isProfileUpdated = true;
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    selectedImageUri = data.getData();
                    imageView.setImageURI(selectedImageUri);
                }
                isProfileUpdated = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chupAnh();
            } else {
                Toast.makeText(this, "Không được cấp quyền truy cập Camera.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                layAnhThuVien();
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
                        nameCurrent = name;
                        if (image != null) {
                            Glide.with(EditProfileActivity.this).load(image).error(R.drawable.ic_user).into(imageView);
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

    private void saveProfile() {
        loadingDialog.showDialog();
        final int tasksCount = calculateTasksCount();;
        final int[] tasksCompleted = {0};

        //lưu ảnh
        if (checkImage) {
            userRef.child("imageUser").setValue(null, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    sessionManager.saveUserImage(null);
                    tasksCompleted[0]++;
                    checkAndCloseActivity(tasksCount, tasksCompleted[0]);
                }
            });
        }
        if (selectedImageUri != null) {
            if (currentUser != null) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("avatars");
                StorageReference imageRef = storageRef.child(currentUser.getUid());

                UploadTask uploadTask = imageRef.putFile(selectedImageUri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String imageDownloadUrl = downloadUri.toString();
                                    sessionManager.saveUserImage(imageDownloadUrl);
                                    userRef.child("imageUser").setValue(imageDownloadUrl);
                                    tasksCompleted[0]++;
                                    checkAndCloseActivity(tasksCount, tasksCompleted[0]);
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
        //lưu tên
        if (isProfileUpdated) {
            if (edtName.getText() != null) {
                String name = String.valueOf(edtName.getText());
                userRef.child("name").setValue(name);
                sessionManager.saveUserName(name);
                tasksCompleted[0]++;
                checkAndCloseActivity(tasksCount, tasksCompleted[0]);
            }
        }

    }

    private int calculateTasksCount() {
        int count = 0;
        if (selectedImageUri != null) {
            count++;
        }
        if (isProfileUpdated) {
            count++;
        }
        if (checkImage) {
            count++;
        }
        return count;
    }

    private void checkAndCloseActivity(int tasksCount, int tasksCompleted) {
        if (tasksCount == tasksCompleted) {
            loadingDialog.dismissDialog();
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
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
