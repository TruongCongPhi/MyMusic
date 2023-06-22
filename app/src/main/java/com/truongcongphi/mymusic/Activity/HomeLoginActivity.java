package com.truongcongphi.mymusic.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.truongcongphi.mymusic.ButtonAnimator.ButtonAnimator;
import com.truongcongphi.mymusic.R;


public class HomeLoginActivity extends AppCompatActivity {
    Button btnLoginFB, btnLoginPhoneNumber, btnRegister, btnLoginGoogle, btnLogin1;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);
        addViews();
        addEvents();
        addButtonAnimator();
        mAuth = FirebaseAuth.getInstance();

    }



        //set các hiệu cho button
        public void addButtonAnimator () {
            btnRegister.setOnTouchListener(new ButtonAnimator(btnRegister));
            btnLoginGoogle.setOnTouchListener(new ButtonAnimator(btnLoginGoogle));
            btnLoginFB.setOnTouchListener(new ButtonAnimator(btnLoginFB));
            btnLogin1.setOnTouchListener(new ButtonAnimator(btnLogin1));
        }

// chuyển trang đăng kí
        public void addEvents () {

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSignUp = new Intent(HomeLoginActivity.this, SignUpActivity.class);
                    startActivity(intentSignUp);

                    // Định nghĩa các animation cho quá trình chuyển đổi giữa các activity
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });

            btnLogin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentLogin = new Intent(HomeLoginActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });


        }
        // yeu cầu thoat ứng dụng
        public void onBackPressed () {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeLoginActivity.this);
            builder.setTitle("Bạn có chắc chắn muốn thoát");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }

        private void addViews () {
            btnLoginFB = (Button) findViewById(R.id.btn_loginFacebook);
            btnRegister = (Button) findViewById(R.id.btn_register);
            btnLoginGoogle = (Button) findViewById(R.id.btn_login_accountgoogle);
            btnLogin1 = (Button) findViewById(R.id.btn_login1);
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }


}
