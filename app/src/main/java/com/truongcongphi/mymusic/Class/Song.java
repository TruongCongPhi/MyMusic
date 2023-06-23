package com.truongcongphi.mymusic.Class;

import java.io.Serializable;
import java.util.List;

public class Song implements Serializable {
    private String albumID;
    private String duration;
    private String imageSong;
    private String like;
    private List<String> singerName;
    private String songName;
    private String url;

    public Song() {
    }

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
}