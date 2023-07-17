package com.truongcongphi.mymusic.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Song implements Parcelable {
    private String albumID;
    private String duration;
    private String imageSong;
    private String like;
    private String mixId;
    private List<String> singerName;
    private String songName;
    private String url;
    private String songID;

    public Song() {
    }

    protected Song(Parcel in) {
        albumID = in.readString();
        duration = in.readString();
        imageSong = in.readString();
        like = in.readString();
        mixId = in.readString();
        singerName = in.createStringArrayList();
        songName = in.readString();
        url = in.readString();
        songID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumID);
        dest.writeString(duration);
        dest.writeString(imageSong);
        dest.writeString(like);
        dest.writeString(mixId);
        dest.writeStringList(singerName);
        dest.writeString(songName);
        dest.writeString(url);
        dest.writeString(songID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageSong() {
        return imageSong;
    }

    public void setImageSong(String imageSong) {
        this.imageSong = imageSong;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getMixId() {
        return mixId;
    }

    public void setMixId(String mixId) {
        this.mixId = mixId;
    }

    public List<String> getSingerName() {
        return singerName;
    }

    public void setSingerName(List<String> singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }
}
