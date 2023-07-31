package com.truongcongphi.mymusic.Class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.truongcongphi.mymusic.Activity.HomeLoginActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionManager {
    private static final String PREF_NAME = "MyMusicPref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_LIKED_SONGS = "liked_songs";
    private static final String KEY_PLAYLISTS = "playlists";
    private static final String KEY_MY_PLAYLIST = "myplaylist";

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
    public void saveUserImage(String imageDownloadUrl) {
        editor.putString(KEY_IMAGE, imageDownloadUrl);
        editor.commit();
    }
    public void saveUserName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }
    public void saveLikedSongs(List<String> likedSongs) {
        Set<String> songSet = new HashSet<>(likedSongs);
        editor.putStringSet(KEY_LIKED_SONGS, songSet);
        editor.commit();
    }

    public List<String> getLikedSongs() {
        Set<String> songSet = sharedPreferences.getStringSet(KEY_LIKED_SONGS, new HashSet<>());
        return new ArrayList<>(songSet);
    }
    public void saveMyPlaylist(List<String> myPlaylist) {
        Set<String> playlistSet = new HashSet<>(myPlaylist);
        editor.putStringSet(KEY_MY_PLAYLIST, playlistSet);
        editor.commit();
    }

    public List<String> getmyPlaylist() {
        Set<String> playlistSet = sharedPreferences.getStringSet(KEY_MY_PLAYLIST, new HashSet<>());
        return new ArrayList<>(playlistSet);
    }

    public void savePlaylist(List<String> playlists) {
        Set<String> playlistSet = new HashSet<>(playlists);
        editor.putStringSet(KEY_PLAYLISTS, playlistSet);
        editor.commit();
    }

    public List<String> getPlaylist() {
        Set<String> playlistSet = sharedPreferences.getStringSet(KEY_PLAYLISTS, new HashSet<>());
        return new ArrayList<>(playlistSet);
    }
    public void removePlaylist(String playlistName) {
        List<String> myPlaylist = getmyPlaylist();
        if (myPlaylist != null) {
            myPlaylist.remove(playlistName);
            saveMyPlaylist(myPlaylist);
        }
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


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false) && !getEmail().isEmpty() && !getPassword().isEmpty();
    }

    public void clearUserCredentials() {
        editor.clear().commit();
    }

    public void logoutUser() {
        clearUserCredentials();
        Intent intent = new Intent(context, HomeLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
