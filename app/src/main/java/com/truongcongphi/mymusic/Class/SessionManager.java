package com.truongcongphi.mymusic.Class;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SessionManager {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    public SessionManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void login(String email, String password, final OnLoginListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            listener.onLoginSuccess();
                        } else {
                            listener.onLoginFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
        currentUser = null;
    }

    public FirebaseUser getLoggedInUser() {
        return currentUser;
    }

    public interface OnLoginListener {
        void onLoginSuccess();
        void onLoginFailure(String errorMessage);
    }
}
