package com.truongcongphi.mymusic.Class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.truongcongphi.mymusic.Activity.HomeLoginActivity;

public class SessionManager {
    private static final String PREF_NAME = "MyMusicPref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGGED_IN = "logged_in";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserCredentials(String email, String password, String image, String name) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_NAME, name);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public String getImage() {
        return sharedPreferences.getString(KEY_IMAGE, "");
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public User getLoggedInUser() {
        User user = new User();
        user.setName(getName());
        user.setEmail(getEmail());
        user.setPassword(getPassword());
        user.setImageUser(getImage());
        return user;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false) && !getEmail().isEmpty() && !getPassword().isEmpty();
    }

    public void clearUserCredentials() {
        editor.remove(KEY_EMAIL)
                .remove(KEY_PASSWORD)
                .remove(KEY_IMAGE)
                .remove(KEY_NAME)
                .remove(KEY_LOGGED_IN)
                .commit();
    }

    public void logoutUser() {
        clearUserCredentials();

        Intent intent = new Intent(context, HomeLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
